package br.com.dbc.vemser.iShirts.controller;

import br.com.dbc.vemser.iShirts.controller.interfaces.CarrinhoControllerInterface;
import br.com.dbc.vemser.iShirts.dto.carrinho.CarrinhoCreateDTO;
import br.com.dbc.vemser.iShirts.dto.carrinho.CarrinhoDTO;
import br.com.dbc.vemser.iShirts.dto.item.ItemCreateDTO;
import br.com.dbc.vemser.iShirts.dto.item.ItemUpdateQuantidadeDTO;
import br.com.dbc.vemser.iShirts.exceptions.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.dbc.vemser.iShirts.service.CarrinhoService;

import javax.validation.Valid;


@RestController
@RequestMapping("/carrinho")
@RequiredArgsConstructor
public class CarrinhoController implements CarrinhoControllerInterface {

    private final CarrinhoService carrinhoService;

    @GetMapping
    public ResponseEntity<CarrinhoDTO> buscarCarrinho() throws RegraDeNegocioException {
        CarrinhoDTO carrinho = carrinhoService.buscarCarrinho();
        return ResponseEntity.ok(carrinho);
    }
    @PostMapping
    public ResponseEntity<CarrinhoDTO> createCarrinho(@Valid @RequestBody CarrinhoCreateDTO carrinho) throws RegraDeNegocioException {
        CarrinhoDTO carrinhoDTO = carrinhoService.criarCarrinho(carrinho);
        return ResponseEntity.status(HttpStatus.CREATED).body(carrinhoDTO);
    }

    @PutMapping
    public ResponseEntity<CarrinhoDTO> atualizarCarrinho() throws RegraDeNegocioException {
        CarrinhoDTO carrinhoAtualizado = carrinhoService.atualizarCarrinho();
        return ResponseEntity.ok(carrinhoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarrinho(@PathVariable Integer id) throws RegraDeNegocioException {
        carrinhoService.deleteCarrinho(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping()
    public ResponseEntity<String> limparCarrinho() throws RegraDeNegocioException {
        carrinhoService.limparCarrinho();
        return ResponseEntity.status(HttpStatus.OK).body("Itens do carrinho deletado com sucesso!");
    }

    @PostMapping("/adicionar-item")
    public ResponseEntity<CarrinhoDTO> adicionarItemCarrinho(@Valid @RequestBody ItemCreateDTO itemCreateDTO) throws RegraDeNegocioException {
        CarrinhoDTO carrinho = carrinhoService.adicionarItemCarrinho(itemCreateDTO);
        return ResponseEntity.ok(carrinho);
    }

    @PutMapping("/atualiza-item/{idItem}")
    public ResponseEntity<CarrinhoDTO> atualizaQuantidadeItem(@PathVariable Integer idItem, @Valid  @RequestBody ItemUpdateQuantidadeDTO quantidadeDTO) throws RegraDeNegocioException {
        CarrinhoDTO carrinho = carrinhoService.atualizaQuantidadeItem(idItem, quantidadeDTO);
        return ResponseEntity.ok(carrinho);
    }

    @DeleteMapping("/remover-item/{idItem}")
    public ResponseEntity<CarrinhoDTO> removerItemCarrinho(@PathVariable Integer idItem) throws RegraDeNegocioException {
        CarrinhoDTO carrinho = carrinhoService.removerItemCarrinho(idItem);
        return ResponseEntity.ok(carrinho);
    }

    @DeleteMapping("/remover-uma-unidade/{idItem}")
    public ResponseEntity<CarrinhoDTO> removerUmaUnidadeItemCarrinho(@PathVariable Integer idItem) throws RegraDeNegocioException {
        CarrinhoDTO carrinho = carrinhoService.removerUmaUnidadeItemCarrinho(idItem);
        return ResponseEntity.ok(carrinho);
    }
}
