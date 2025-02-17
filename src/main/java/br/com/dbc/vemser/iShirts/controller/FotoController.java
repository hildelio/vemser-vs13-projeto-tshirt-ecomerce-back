package br.com.dbc.vemser.iShirts.controller;

import br.com.dbc.vemser.iShirts.controller.interfaces.FotoControllerInterface;
import br.com.dbc.vemser.iShirts.dto.foto.FotoDTO;
import br.com.dbc.vemser.iShirts.exceptions.RegraDeNegocioException;
import br.com.dbc.vemser.iShirts.service.FotoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Tag(name = "Foto", description = "Controller responsável pelas operações relacionadas à foto.")
@Validated
@RequestMapping("/foto")
@RequiredArgsConstructor
public class FotoController implements FotoControllerInterface {
    private final FotoService fotoService;

    @PostMapping("/{idVariacao}")
    public ResponseEntity<FotoDTO> criarFoto(@PathVariable("idVariacao") Integer idVariacao,
            @RequestBody(required = true) MultipartFile arquivo) throws Exception {
        return new ResponseEntity<>(fotoService.criar(arquivo, idVariacao), HttpStatus.CREATED);
    }

    @PutMapping("/{idFoto}")
    public ResponseEntity<FotoDTO> atualizarFoto(@PathVariable("idFoto") Integer idFoto,
                                          @RequestBody(required = true) MultipartFile arquivo) throws IOException, RegraDeNegocioException {
        return new ResponseEntity<>(fotoService.atualizar(idFoto, arquivo), HttpStatus.OK);
    }

    @GetMapping("/{idFoto}")
    public ResponseEntity<FotoDTO> obterFotoPorId(@PathVariable("idFoto") Integer idFoto) throws RegraDeNegocioException {
        return new ResponseEntity<>(fotoService.obterPorId(idFoto), HttpStatus.OK);
    }

    @DeleteMapping("/{idFoto}")
    public ResponseEntity<Void> deletarFoto(@PathVariable("idFoto") Integer idFoto) throws RegraDeNegocioException {
        fotoService.deletar(idFoto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
