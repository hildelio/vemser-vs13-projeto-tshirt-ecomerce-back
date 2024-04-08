package br.com.dbc.vemser.iShirts.service;

import br.com.dbc.vemser.iShirts.dto.cargo.CargoCreateDTO;
import br.com.dbc.vemser.iShirts.dto.cargo.CargoDTO;
import br.com.dbc.vemser.iShirts.exceptions.RegraDeNegocioException;
import br.com.dbc.vemser.iShirts.model.Cargo;
import br.com.dbc.vemser.iShirts.repository.CargoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;
    private final ObjectMapper objectMapper;


    public void criarCargo(CargoCreateDTO cargoCreateDTO) throws RegraDeNegocioException {
        String descricao = cargoCreateDTO.getDescricao();

        Optional<Cargo> cargoExistente = cargoRepository.findByDescricao(descricao);

        if (cargoExistente.isEmpty()) {
            Cargo cargo = new Cargo();
            cargo.setDescricao(descricao);
            cargoRepository.save(cargo);
        } else {
           throw new RegraDeNegocioException("Esse cargo já existe!");
        }
    }


    public void deletarCargo(Integer idCargo) {
        cargoRepository.deleteById(idCargo);
    }

    public List<CargoDTO> listarCargos() throws Exception {
        List<Cargo> cargos = cargoRepository.findAll();
        if (cargos.isEmpty()) {
            throw new RegraDeNegocioException("Nenhum cargo encontrado!");
        }
        return cargos.stream()
                .map(this::retornarDTO)
                .toList();
    }

    public Cargo buscarCargoPorId(Integer idCargo) throws RegraDeNegocioException {
        return cargoRepository.findById(idCargo)
                .orElseThrow(() ->
                        new RegraDeNegocioException("Cargo não encontrado!"));
    }

    public CargoDTO retornarDTO(Cargo cargo) {
        return objectMapper.convertValue(cargo, CargoDTO.class);
    }

    public Cargo buscarCargoPorDescricao(String cliente) throws RegraDeNegocioException {
        return cargoRepository.findByDescricao(cliente)
                .orElseThrow(() ->
                        new RegraDeNegocioException("Cargo não encontrado!"));
    }
}
