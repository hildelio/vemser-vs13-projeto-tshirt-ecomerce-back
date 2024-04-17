package br.com.dbc.vemser.iShirts.dto.variacao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class VariacaoCreateDTO {

    @NotNull
    @Schema(description = "Id do Produto onde a Variação será acoplada", required = true, example = "1")
    private Integer idProduto;

    @Schema(description = "SKU da Variação", required = true, example = "CAM-BRA-G-BSC-002")
    @NotBlank(message = "O SKU não pode estar em branco")
    private String sku;

    @Schema(description = "Cor da Variação", required = true, example = "Branca")
    @NotBlank(message = "A cor não pode estar em branco")
    private String cor;

    @Schema(description = "Tamanho da Variação", required = true, example = "G")
    @NotBlank(message = "O tamanho não pode estar em branco")
    private String tamanho;

    @Schema(description = "Preço da Variação", required = true, example = "100")
    @NotNull(message = "O preço não pode ser nulo")
    private BigDecimal preco;

    @Schema(description = "Taxa de Desconto da Variação", required = true, example = "10")
    @NotNull(message = "A taxa de desconto não pode ser nula")
    private Integer taxaDesconto;
}
