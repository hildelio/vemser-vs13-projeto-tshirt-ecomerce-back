package br.com.dbc.vemser.iShirts.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FAVORITOS")
public class Favoritos {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FAVORITOS")
    @SequenceGenerator(name = "SEQ_FAVORITOS",sequenceName = "SEQ_FAVORITOS", allocationSize = 1)
    @Column(name = "ID_FAVORITOS")
    private Integer idFavoritos;

    @Column(name = "ID_USUARIO", insertable = false, updatable = false)
    private Integer idUsuario;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")
    @JsonIgnore
    private Usuario usuario;

    @Column(name = "ID_VARIACAO", insertable = false, updatable = false)
    private Integer idVariacao;

    @ManyToOne
    @JoinColumn(name = "ID_VARIACAO", referencedColumnName = "ID_VARIACAO")
    @JsonIgnore
    private Variacao variacao;

    @Column(name = "CRIADO")
    private Timestamp criadoEm;

    @Column(name = "EDITADO")
    private Timestamp editadoEm;
}
