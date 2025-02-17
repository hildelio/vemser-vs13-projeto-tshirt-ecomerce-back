package br.com.dbc.vemser.iShirts.dto.variacao;

import br.com.dbc.vemser.iShirts.model.Foto;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class VariacaoDTO {

    private Integer idVariacao;

    private Integer idProduto;

    private List<Foto> fotos;

    private String sku;

    private String cor;

    private String tamanho;

    private Double preco;

    private Integer taxaDesconto;

    private String ativo;

    private Timestamp criado;

    private Timestamp editado;
}
