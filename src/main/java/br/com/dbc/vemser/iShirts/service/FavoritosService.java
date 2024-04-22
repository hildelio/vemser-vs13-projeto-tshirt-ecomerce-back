package br.com.dbc.vemser.iShirts.service;

import br.com.dbc.vemser.iShirts.dto.favoritos.FavoritosCreateDTO;
import br.com.dbc.vemser.iShirts.dto.favoritos.FavoritosDTO;
import br.com.dbc.vemser.iShirts.exceptions.RegraDeNegocioException;
import br.com.dbc.vemser.iShirts.model.Favoritos;
import br.com.dbc.vemser.iShirts.model.Usuario;
import br.com.dbc.vemser.iShirts.model.Variacao;
import br.com.dbc.vemser.iShirts.repository.FavoritosRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FavoritosService {

    private final FavoritosRepository favoritosRepository;
    private final ObjectMapper objectMapper;
    private final UsuarioService usuarioService;
    private final VariacaoService variacaoService;


    public FavoritosDTO criarFavoritos(FavoritosCreateDTO favoritosCreateDTO) throws RegraDeNegocioException {
        Usuario usuarioLogado = usuarioService.buscarUsuarioLogadoEntity();
        Variacao variacao = variacaoService.buscarPorId(favoritosCreateDTO.getIdVariacao());

        if (verificaFavoritoExiste(usuarioLogado, variacao)) {
            throw new RegraDeNegocioException("Favorito já existe");
        }

        Favoritos favoritosEntity = objectMapper.convertValue(favoritosCreateDTO, Favoritos.class);
        favoritosEntity.setUsuario(usuarioLogado);
        favoritosEntity.setVariacao(variacao);
        Favoritos favoritosSalvo = favoritosRepository.save(favoritosEntity);

        return converterFavoritosDTO(favoritosSalvo);
    }

    public List<FavoritosDTO> listarFavoritos() throws RegraDeNegocioException {
        Usuario usuarioLogado = usuarioService.buscarUsuarioLogadoEntity();
        List<Favoritos> favoritos = favoritosRepository.findAllByUsuario(usuarioLogado);
        return favoritos.stream()
                .map(this::converterFavoritosDTO)
                .toList();
    }

    public void deletarFavoritos(Integer idFavoritos) throws RegraDeNegocioException {
        Favoritos favoritos = buscarFavoritosPorId(idFavoritos);
        favoritosRepository.delete(favoritos);
    }

    public Favoritos buscarFavoritosPorId(Integer idFavoritos) throws RegraDeNegocioException {
        return favoritosRepository.findById(idFavoritos).orElseThrow(() -> new RegraDeNegocioException("Favoritos não encontrado"));
    }

    private boolean verificaFavoritoExiste(Usuario usuario, Variacao variacao) {
        return favoritosRepository.existsByUsuarioAndVariacao(usuario, variacao);
    }

    private FavoritosDTO converterFavoritosDTO(Favoritos favoritos) {
        FavoritosDTO favoritosDTO = new FavoritosDTO();
        favoritosDTO.setIdFavoritos(favoritos.getIdFavoritos());
        favoritosDTO.setIdUsuario(favoritos.getUsuario().getIdUsuario());
        favoritosDTO.setIdVariacao(favoritos.getVariacao().getIdVariacao());
        return favoritosDTO;
    }
}
