package br.com.dbc.vemser.iShirts.dto.cupom;

import br.com.dbc.vemser.iShirts.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CupomCreateDTO {


    @NotNull
    @Schema(description = "Código do cupom", example = "987654", required = true)
    private Integer codigo;

    @NotNull
    @Schema(description = "Valor do cupom", example = "250", required = true)
    private Double valor;

    @NotNull
    @Future
    @Schema(description = "Validade do cupom", example = "2024-05-30T23:59:59", required = true)
    private LocalDateTime validade;

    @NotNull
    @Min(value = 1)
    @Schema(description = "Limite de uso do cupom", example = "1000", required = true)
    private Integer limiteUso;

    @NotNull
    @Min(value = 1)
    @Schema(description = "Valor mínimo para uso do cupom", example = "500", required = true)
    private Double valorMinimo;

}
