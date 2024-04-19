package br.com.dbc.vemser.iShirts.model;

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

    @Column(name = "ID_USUARIO")
    private Integer idUsuario;

    @Column(name = "ID_VARIACAO")
    private Integer idVariacao;

    @Column(name = "CRIADO")
    private Timestamp criadoEm;

    @Column(name = "EDITADO")
    private Timestamp editadoEm;
}
