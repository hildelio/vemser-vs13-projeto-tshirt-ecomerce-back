package br.com.dbc.vemser.iShirts.controller;

import br.com.dbc.vemser.iShirts.dto.favoritos.FavoritosCreateDTO;
import br.com.dbc.vemser.iShirts.dto.favoritos.FavoritosDTO;
import br.com.dbc.vemser.iShirts.exceptions.RegraDeNegocioException;
import br.com.dbc.vemser.iShirts.service.FavoritosService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/favoritos")
@Validated
@RequiredArgsConstructor
@Tag(name = "Favoritos", description = "Endpoint de Favoritos")
public class FavoritosController {

    private final FavoritosService favoritosService;

    @GetMapping
    public ResponseEntity<List<FavoritosDTO>> listarFavoritos() throws RegraDeNegocioException {
        return new ResponseEntity<>(favoritosService.listarFavoritos(), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<FavoritosDTO> criarFavoritos(@RequestBody @Valid FavoritosCreateDTO favoritosCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(favoritosService.criarFavoritos(favoritosCreateDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{idFavoritos}")
    public ResponseEntity<Void> deletarFavoritos(@PathVariable("idFavoritos") Integer idFavoritos) throws RegraDeNegocioException {
        favoritosService.deletarFavoritos(idFavoritos);
        return ResponseEntity.noContent().build();
    }
}
