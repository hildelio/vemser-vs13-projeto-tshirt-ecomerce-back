package br.com.dbc.vemser.iShirts.controller;

import br.com.dbc.vemser.iShirts.controller.interfaces.EnderecoControllerInterface;
import br.com.dbc.vemser.iShirts.dto.endereco.EnderecoCreateDTO;
import br.com.dbc.vemser.iShirts.dto.endereco.EnderecoDTO;
import br.com.dbc.vemser.iShirts.exceptions.RegraDeNegocioException;
import br.com.dbc.vemser.iShirts.service.EnderecoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/endereco")
@RequiredArgsConstructor
public class EnderecoController implements EnderecoControllerInterface {

    private final EnderecoService enderecoService;
    @Override
   public ResponseEntity<Page<EnderecoDTO>> listarTodos(@RequestParam Integer tamanhoPagina,@RequestParam  Integer paginaSolicitada ){
    return ResponseEntity.ok(this.enderecoService.listarTodos(tamanhoPagina,paginaSolicitada));
   }
    @Override
   public ResponseEntity<EnderecoDTO> buscarPorIdDto(@PathVariable Integer idEndereco) throws RegraDeNegocioException {
        return ResponseEntity.ok(this.enderecoService.buscarPorIdDto(idEndereco));
   }

    @Override
    public ResponseEntity<Page<EnderecoDTO>> listarTodos(@PathVariable Integer idPessoa, @RequestParam Integer tamanhoPagina, @RequestParam Integer paginaSolicitada) throws RegraDeNegocioException {
        Page<EnderecoDTO> enderecos = this.enderecoService.listarPorPessoa(idPessoa, tamanhoPagina, paginaSolicitada);
        return ResponseEntity.ok(enderecos);
    }
    @Override
    public ResponseEntity<EnderecoDTO> salvarEndereco(@RequestBody @Valid EnderecoCreateDTO dto) throws RegraDeNegocioException {
       try {
           Integer.parseInt(dto.getNumero());
           return new ResponseEntity<>(this.enderecoService.salvarEndereco(dto), HttpStatus.CREATED);
       } catch (NumberFormatException e) {
           throw new RegraDeNegocioException("O campo número deve ser um número inteiro");
       }
    }
    @Override
    public ResponseEntity<EnderecoDTO> editarEndereco(@RequestBody @Valid EnderecoCreateDTO dto,@PathVariable Integer idEndereco) throws RegraDeNegocioException {
        return ResponseEntity.ok().body(this.enderecoService.editarEndereco(dto,idEndereco));
    }
    @Override
    public ResponseEntity<Void> deletar(@PathVariable Integer idEndereco) throws RegraDeNegocioException {
        this.enderecoService.deletarEndereco(idEndereco);
        return ResponseEntity.noContent().build();
    }
}
