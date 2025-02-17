package br.com.dbc.vemser.iShirts.service;

import br.com.dbc.vemser.iShirts.dto.carrinho.CarrinhoCreateDTO;
import br.com.dbc.vemser.iShirts.dto.carrinho.CarrinhoDTO;
import br.com.dbc.vemser.iShirts.dto.item.ItemCreateDTO;
import br.com.dbc.vemser.iShirts.dto.item.ItemDTO;
import br.com.dbc.vemser.iShirts.dto.item.ItemUpdateQuantidadeDTO;
import br.com.dbc.vemser.iShirts.exceptions.RegraDeNegocioException;
import br.com.dbc.vemser.iShirts.model.Carrinho;
import br.com.dbc.vemser.iShirts.model.Item;
import br.com.dbc.vemser.iShirts.model.Usuario;
import br.com.dbc.vemser.iShirts.repository.CarrinhoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final ItemService itemService;
    private final UsuarioService usuarioService;

    public Carrinho buscarCarrinhoPorId(Integer id) throws RegraDeNegocioException {
        return carrinhoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Carrinho não encontrado"));
    }

    public Carrinho buscarCarrinhoUsuarioLogado() throws RegraDeNegocioException {
        Usuario usuario = usuarioService.buscarUsuarioLogadoEntity();
        Carrinho carrinho = carrinhoRepository.findByUsuario(usuario);
        if(carrinho == null){
            carrinho = new Carrinho();
            carrinho.setUsuario(usuario);
            carrinho.setTotal(BigDecimal.ZERO);
            carrinho = carrinhoRepository.save(carrinho);
        }
        return carrinho;
    }

    public CarrinhoDTO criarCarrinho(CarrinhoCreateDTO carrinhoCreateDTO) throws RegraDeNegocioException {
        if (carrinhoCreateDTO.getItens() == null || carrinhoCreateDTO.getItens().isEmpty()) {
            throw new RegraDeNegocioException("Não é possível criar um carrinho sem itens");
        }

        Carrinho carrinho = buscarCarrinhoUsuarioLogado();
        List<Item> itens = itemService.criarItens(carrinhoCreateDTO.getItens());
        carrinho.setItens(itens);
        calcularTotal(carrinho);
        return converterDTO(carrinhoRepository.save(carrinho));
    }


    public CarrinhoDTO atualizarCarrinho() throws RegraDeNegocioException {
        Carrinho carrinho = buscarCarrinhoUsuarioLogado();
        itemService.atualizarItens(carrinho.getItens());
        calcularTotal(carrinho);
        return converterDTO(carrinhoRepository.save(carrinho));
    }

    public void deleteCarrinho() throws RegraDeNegocioException {
        Carrinho carrinho = buscarCarrinhoUsuarioLogado();
        carrinhoRepository.delete(carrinho);
    }

    public CarrinhoDTO adicionarItemCarrinho(ItemCreateDTO itemCreateDTO) throws RegraDeNegocioException {
        Carrinho carrinho = buscarCarrinhoUsuarioLogado();
        Item item = itemService.salvarItem(itemService.criarItem(itemCreateDTO));

        Optional<Item> itemExistente = carrinho.getItens().stream()
                .filter(i -> i.getVariacao().getProduto().getIdProduto().equals(item.getVariacao().getProduto().getIdProduto()))
                .findFirst();

        if (itemExistente.isPresent()) {
            Item itemAtual = itemExistente.get();
            itemAtual.setQuantidade(itemAtual.getQuantidade() + item.getQuantidade());
            itemService.calcularSubTotal(itemAtual);
        } else {
            carrinho.getItens().add(item);
        }

        calcularTotal(carrinho);
        return converterDTO(carrinhoRepository.save(carrinho));
    }

    public CarrinhoDTO removerItemCarrinho(Integer idItem) throws RegraDeNegocioException {
        Carrinho carrinho = buscarCarrinhoUsuarioLogado();

        Item item = itemService.buscarItemPorId(idItem);

        if (!carrinho.getItens().contains(item)) {
            throw new RegraDeNegocioException("Não é possível deletar o item de outro usuário");
        }

        carrinho.getItens().remove(item);
        itemService.delete(item);
        calcularTotal(carrinho);

        return converterDTO(carrinhoRepository.save(carrinho));
    }

    public CarrinhoDTO removerUmaUnidadeItemCarrinho(Integer idItem) throws RegraDeNegocioException {
        Carrinho carrinho = buscarCarrinhoUsuarioLogado();
        Item item = itemService.buscarItemPorId(idItem);

        Optional<Item> itemExistente = carrinho.getItens().stream()
                .filter(i -> i.getVariacao().getProduto().getIdProduto().equals(item.getVariacao().getProduto().getIdProduto()))
                .findFirst();

        if (itemExistente.isPresent()) {
            Item itemEncontrado = itemExistente.get();
            if (itemEncontrado.getQuantidade() > 1) {
                itemEncontrado.setQuantidade(itemEncontrado.getQuantidade() - 1);
                itemService.calcularSubTotal(itemEncontrado);
            } else {
                carrinho.getItens().remove(itemEncontrado);
                itemService.delete(itemEncontrado);
            }
        } else {
            throw new RegraDeNegocioException("Item não encontrado no carrinho");
        }
        calcularTotal(carrinho);

        return converterDTO(carrinhoRepository.save(carrinho));
    }

    public void limparCarrinho() throws RegraDeNegocioException {
        Carrinho carrinho = buscarCarrinhoUsuarioLogado();
        List<Item> itens = new ArrayList<>(carrinho.getItens());
        carrinho.getItens().clear();
        for (Item item : itens) {
            itemService.delete(item);
        }

        calcularTotal(carrinho);

        carrinhoRepository.save(carrinho);
    }
    public void limparCarrinhoPedidoFeito() throws RegraDeNegocioException {
        Carrinho carrinho = buscarCarrinhoUsuarioLogado();
        carrinho.getItens().clear();
        calcularTotal(carrinho);

        carrinhoRepository.save(carrinho);
    }

    public CarrinhoDTO atualizaQuantidadeItem(Integer idItem, ItemUpdateQuantidadeDTO quantidadeDTO) throws RegraDeNegocioException {
        Carrinho carrinho = buscarCarrinhoUsuarioLogado();
        Item item = carrinho.getItens().stream()
                .filter(itemCarr -> itemCarr.getIdItem().equals(idItem))
                .findFirst().orElseThrow(() -> new RegraDeNegocioException("Item não encontrado no carrinho"));

        item.setQuantidade(quantidadeDTO.getQuantidade());
        itemService.calcularSubTotal(item);
        calcularTotal(carrinho);
        return converterDTO(carrinhoRepository.save(carrinho));
    }
    public CarrinhoDTO buscarCarrinho() throws RegraDeNegocioException {
        return converterDTO(buscarCarrinhoUsuarioLogado());
    }

    public void calcularTotal(Carrinho carrinho) {
        BigDecimal total = BigDecimal.ZERO;
        List<Item> itens = carrinho.getItens();

        for (Item item : itens) {
            BigDecimal subTotal = item.getSubTotal();
            total = total.add(subTotal);
        }

        carrinho.setTotal(total);
    }

    public BigDecimal calcularValorBrutoTotal(Carrinho carrinho) {
        BigDecimal total = BigDecimal.ZERO;
        List<Item> itens = carrinho.getItens();
        log.info(total.toString());

        for (Item item : itens) {
            BigDecimal precoItem = BigDecimal.valueOf(item.getVariacao().getPreco());
            BigDecimal quantidade = BigDecimal.valueOf(item.getQuantidade());
            BigDecimal subTotal = precoItem.multiply(quantidade);
            total = total.add(subTotal);
        }
        log.info(total.toString());

        return total;
    }

    public CarrinhoDTO converterDTO(Carrinho carrinho){
        CarrinhoDTO carrinhoDTO = new CarrinhoDTO();
        carrinhoDTO.setIdUsuario(carrinho.getUsuario().getIdUsuario());
        List<ItemDTO> itensDTO = itemService.converterDTO(carrinho.getItens());
        carrinhoDTO.setItens(itensDTO);
        carrinhoDTO.setIdCarrinho(carrinho.getIdCarrinho());
        carrinhoDTO.setTotal(carrinho.getTotal());
        return carrinhoDTO;
    }

}
