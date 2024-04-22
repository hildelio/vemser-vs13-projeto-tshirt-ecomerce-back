package br.com.dbc.vemser.iShirts.dto.cupom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import br.com.dbc.vemser.iShirts.annotation.deserializer.LocalDateTimeDeserializer;
import br.com.dbc.vemser.iShirts.annotation.interfaces.FormatLocalDateTime;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CupomCreateDTO {


    @NotNull
    @Schema(description = "Código do cupom", example = "987654", required = true)
    private Integer codigo;

    @NotNull
    @Min(value = 1)
    @Schema(description = "Valor do cupom", example = "250", required = true)
    private Double valor;

    @NotNull
    @Future
    @Schema(description = "Validade do cupom", example = "2024-05-30T23:59:59", required = true)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @FormatLocalDateTime
    @Future
    private LocalDateTime validade;

    @NotNull
    @Min(value = 1)
    @Schema(description = "Limite de uso do cupom", example = "1000", required = true)
    private Integer limiteUso;

    @NotNull
    @Positive
    @Min(value = 1)
    @Schema(description = "Valor mínimo para uso do cupom", example = "500", required = true)
    private Double valorMinimo;

}
