package br.com.dbc.vemser.iShirts.repository;

import br.com.dbc.vemser.iShirts.model.Favoritos;
import br.com.dbc.vemser.iShirts.model.Usuario;
import br.com.dbc.vemser.iShirts.model.Variacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritosRepository extends JpaRepository<Favoritos, Integer> {
    boolean existsByUsuarioAndVariacao(Usuario usuario, Variacao variacao);

    List<Favoritos> findAllByUsuario(Usuario usuario);
}
