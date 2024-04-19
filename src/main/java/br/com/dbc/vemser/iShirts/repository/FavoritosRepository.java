package br.com.dbc.vemser.iShirts.repository;

import br.com.dbc.vemser.iShirts.model.Favoritos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritosRepository extends JpaRepository<Favoritos, Integer> {
}
