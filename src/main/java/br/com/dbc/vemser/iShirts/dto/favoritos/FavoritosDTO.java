package br.com.dbc.vemser.iShirts.dto.favoritos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FavoritosDTO {

    @Schema(description = "Id do Favorito", required = true, example = "1")
    private Integer idFavoritos;

    @Schema(description = "Id do Usuário que favoritou a Variação", required = true, example = "1")
    private Integer idUsuario;

    @Schema(description = "Id da Variação favoritada", required = true, example = "1")
    private Integer idVariacao;
}
