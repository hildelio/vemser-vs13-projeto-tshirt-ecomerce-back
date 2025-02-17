package br.com.dbc.vemser.iShirts.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "VARIACAO")
public class Variacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VARIACAO")
    @SequenceGenerator(name = "SEQ_VARIACAO",sequenceName = "SEQ_VARIACAO", allocationSize = 1)
    @Column(name = "ID_VARIACAO")
    private Integer idVariacao;

    @Column(name = "ID_PRODUTO", insertable = false, updatable = false)
    private Integer idProduto;

    @ManyToOne
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID_PRODUTO")
    @JsonIgnore
    private Produto produto;

    @OneToMany(mappedBy = "variacao", cascade = CascadeType.ALL)
    @Column(name = "ID_FOTO")
    private List<Foto> fotos = new ArrayList<>();

    @Column(name = "ID_FOTO", insertable = false)
    private Integer idFoto;

    @Column(name = "SKU")
    private String sku;

    @Column(name = "COR")
    private String cor;

    @Column(name = "TAMANHO")
    private String tamanho;

    @Column(name = "PRECO")
    private Double preco;

    @Column(name = "TAXA_DESCONTO")
    private Integer taxaDesconto;

    @Column(name = "ATIVO")
    private String ativo;

    @CreationTimestamp
    @Column(name = "CRIADO")
    private Timestamp criado;

    @UpdateTimestamp
    @Column(name = "EDITADO")
    private Timestamp editado;

    @OneToMany(mappedBy = "variacao", cascade = CascadeType.ALL)
    private List<Favoritos> favoritos;

}
