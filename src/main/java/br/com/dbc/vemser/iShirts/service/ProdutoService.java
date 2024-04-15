package br.com.dbc.vemser.iShirts.service;

import br.com.dbc.vemser.iShirts.dto.produto.ProdutoCreateDTO;
import br.com.dbc.vemser.iShirts.dto.produto.ProdutoDTO;
import br.com.dbc.vemser.iShirts.exceptions.RegraDeNegocioException;
import br.com.dbc.vemser.iShirts.model.Produto;
import br.com.dbc.vemser.iShirts.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.MediaSize;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ObjectMapper objectMapper;

    private static final String NAO_ENCONTRADO = "Produto não encontrado";

    public Page<ProdutoDTO> listarProdutos(Pageable page){
        Page<Produto> listaProdutos = produtoRepository.findAllByAtivo(page, "1");
        return listaProdutos.map(produto -> objectMapper.convertValue(produto, ProdutoDTO.class));
    }

    public ProdutoDTO listarPorID(Integer id) throws RegraDeNegocioException {
        Produto produto = buscarPorId(id);
        if(produto.getAtivo().equals("0")){
            throw new RegraDeNegocioException(NAO_ENCONTRADO);
        }
        return objectMapper.convertValue(produto, ProdutoDTO.class);
    }

    public ProdutoDTO criarProduto(ProdutoCreateDTO produto){
        Produto produtoEntity = objectMapper.convertValue(produto, Produto.class);
        produtoEntity.setAtivo("1");
        produtoEntity.setCategoria(produto.getCategoria());
        Produto produtoSalvo = produtoRepository.save(produtoEntity);

        return objectMapper.convertValue(produtoSalvo, ProdutoDTO.class);
    }

    public ProdutoDTO editarProduto(ProdutoCreateDTO produto, Integer id) throws RegraDeNegocioException {
        Produto produtoEntity = buscarPorId(id);
        produtoEntity.setTitulo(produto.getTitulo());
        produtoEntity.setDescricao(produto.getDescricao());
        produtoEntity.setCategoria(produto.getCategoria());
        produtoEntity.setTecido(produto.getTecido());
        Produto produtoSalvo = produtoRepository.save(produtoEntity);

        return objectMapper.convertValue(produtoSalvo, ProdutoDTO.class);
    }

    public void deletarProduto(Integer id) throws RegraDeNegocioException {
        Produto produto = buscarPorId(id);

        if (produto.getAtivo().equals("0")) {
            throw new RegraDeNegocioException("Produto já foi deletado");
        }

        produto.setAtivo("0");
        produtoRepository.save(produto);

    }

    public Produto buscarPorId(Integer id) throws RegraDeNegocioException {
        return produtoRepository.findById(id).orElseThrow(() -> new RegraDeNegocioException(NAO_ENCONTRADO));
    }

}
