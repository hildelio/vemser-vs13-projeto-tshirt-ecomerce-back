package br.com.dbc.vemser.iShirts.dto.favoritos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FavoritosCreateDTO {
        @NotNull
        @Schema(description = "Id do Usuário que favoritou a Variação", required = true, example = "1")
        private Integer idUsuario;

        @NotNull
        @Schema(description = "Id da Variação favoritada", required = true, example = "1")
        private Integer idVariacao;
}
