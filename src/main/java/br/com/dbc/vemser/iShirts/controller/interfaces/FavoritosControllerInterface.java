package br.com.dbc.vemser.iShirts.controller.interfaces;

import br.com.dbc.vemser.iShirts.dto.favoritos.FavoritosCreateDTO;
import br.com.dbc.vemser.iShirts.dto.favoritos.FavoritosDTO;
import br.com.dbc.vemser.iShirts.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Favoritos", description = "Controller responsável pelas operações relacionadas a favoritos.")
public interface FavoritosControllerInterface {

    @Operation(summary = "Listar todos os favoritos do usuário logado", description = "API para listar todos os favoritos do usuário logado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de favoritos retornada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção.")
    })
    ResponseEntity<List<FavoritosDTO>> listarFavoritos() throws RegraDeNegocioException;

    @Operation(summary = "Criar um novo favorito para o usuário logado", description = "API para criar um novo favorito para o usuário logado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Favorito criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção.")
    })
    @PostMapping
    ResponseEntity<FavoritosDTO> criarFavoritos(@RequestBody @Valid FavoritosCreateDTO favoritosCreateDTO) throws RegraDeNegocioException;

    @Operation(summary = "Deletar um favorito do usuário logado", description = "API para deletar um favorito do usuário logado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Favorito deletado com sucesso."),
            @ApiResponse(responseCode = "400", description = "ID do favorito não é válido."),
            @ApiResponse(responseCode = "404", description = "Favorito não encontrado."),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção.")
    })
    @DeleteMapping("/{idFavoritos}")
    ResponseEntity<Void> deletarFavoritos(@PathVariable("idFavoritos") Integer idFavoritos) throws RegraDeNegocioException;
}
