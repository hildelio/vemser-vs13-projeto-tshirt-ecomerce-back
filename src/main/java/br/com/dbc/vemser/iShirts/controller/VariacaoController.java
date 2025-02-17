package br.com.dbc.vemser.iShirts.controller;

import br.com.dbc.vemser.iShirts.controller.interfaces.VariacaoControllerInterface;
import br.com.dbc.vemser.iShirts.dto.variacao.VariacaoCreateDTO;
import br.com.dbc.vemser.iShirts.dto.variacao.VariacaoDTO;
import br.com.dbc.vemser.iShirts.exceptions.RegraDeNegocioException;
import br.com.dbc.vemser.iShirts.service.VariacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/variacao")
@Validated
@RequiredArgsConstructor
@Tag(name = "Variação", description = "Crud de variação")
public class VariacaoController implements VariacaoControllerInterface {

    private final VariacaoService variacaoService;

    @PostMapping
    public ResponseEntity<VariacaoDTO> criarVariacao(@RequestBody @Valid VariacaoCreateDTO variacaoCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(variacaoService.criarVariacao(variacaoCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/por-id/{id}")
    public ResponseEntity<VariacaoDTO> listarPorID(@PathVariable("id") Integer id) throws Exception {
        return new ResponseEntity<>(variacaoService.listarPorID(id), HttpStatus.OK);
    }

    @GetMapping("/todos-variacoes")
    public ResponseEntity<Page<VariacaoDTO>> listarVariacoes (@ParameterObject @PageableDefault(size = 20, page = 0, sort = "idVariacao", direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(variacaoService.listarVariacoes(pageable), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VariacaoDTO> editarVariacao(@PathVariable("id") Integer id, @RequestBody VariacaoDTO variacaoDTO) throws Exception {
        return new ResponseEntity<>(variacaoService.editarVariacao(id, variacaoDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVariacao(@PathVariable("id") Integer id) throws Exception {
        variacaoService.deletarVariacao(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/desativar/{idProduto}")
    public ResponseEntity<String> desativarVariacao(@PathVariable("idProduto") Integer idProduto) throws RegraDeNegocioException {
        variacaoService.desativarVariacao(idProduto);
        return ResponseEntity.ok("Variação desativada com sucesso");
    }

    @DeleteMapping("/ativar/{idProduto}")
    public ResponseEntity<String> ativarVariacao(@PathVariable("idProduto") Integer idProduto) throws RegraDeNegocioException {
        variacaoService.ativarVariacao(idProduto);
        return ResponseEntity.ok("Variação ativada com sucesso");
    }

}
