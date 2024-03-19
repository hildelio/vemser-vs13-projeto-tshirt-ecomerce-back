package br.com.dbc.vemser.iShirts.service;

import br.com.dbc.vemser.iShirts.dto.pessoa.PessoaCreateDTO;
import br.com.dbc.vemser.iShirts.dto.pessoa.PessoaUpdateDTO;
import br.com.dbc.vemser.iShirts.exceptions.RegraDeNegocioException;
import br.com.dbc.vemser.iShirts.model.Pessoa;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.dbc.vemser.iShirts.model.enums.Ativo;
import br.com.dbc.vemser.iShirts.repository.PessoaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PessoaServiceTest {

    @InjectMocks
    private PessoaService pessoaService;

    @Mock
    private ObjectMapper objectMapper;


    @Mock
    private PessoaRepository pessoaRepository;

    @BeforeEach
    void setUp() {
        pessoaService = new PessoaService(pessoaRepository, objectMapper);
    }

    @Tag("Teste para cadastrar uma Pessoa")
    @Test
    public void testarCadastrarPessoa() throws RegraDeNegocioException {
        PessoaCreateDTO pessoaCreateDTO = new PessoaCreateDTO();
        pessoaCreateDTO.setNome("Nome");
        pessoaCreateDTO.setSobrenome("Sobrenome");
        pessoaCreateDTO.setCpf("12345678901");
        pessoaCreateDTO.setCelular("11999999999");
        pessoaCreateDTO.setDataNascimento(new Date());
        pessoaCreateDTO.setPreferencia("Preferencia");
        pessoaCreateDTO.setAtivo("Ativo");
        pessoaCreateDTO.setIdUsuario(1);

        Pessoa pessoa = new Pessoa();
        pessoa.setNome(pessoaCreateDTO.getNome());
        pessoa.setSobrenome(pessoaCreateDTO.getSobrenome());
        pessoa.setCpf(pessoaCreateDTO.getCpf());
        pessoa.setCelular(pessoaCreateDTO.getCelular());
        pessoa.setDataNascimento(pessoaCreateDTO.getDataNascimento());
        pessoa.setPreferencia(pessoaCreateDTO.getPreferencia());
        pessoa.setAtivo(pessoaCreateDTO.getAtivo());
        pessoa.setIdUsuario(pessoaCreateDTO.getIdUsuario());

        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        Pessoa pessoaCadastrada = pessoaService.cadastrarPessoa(pessoaCreateDTO);

        assertEquals(pessoa, pessoaCadastrada);
    }

    @Tag("Teste_para_atualizar_uma_Pessoa")
    @Test
    public void testarAtualizarUmaPessoa() throws RegraDeNegocioException {
        PessoaUpdateDTO pessoaUpdateDTO = new PessoaUpdateDTO();
        pessoaUpdateDTO.setNome("Nome");
        pessoaUpdateDTO.setSobrenome("Sobrenome");
        pessoaUpdateDTO.setCpf("12345678901");
        pessoaUpdateDTO.setCelular("11999999999");
        pessoaUpdateDTO.setDataNascimento(new Date());
        pessoaUpdateDTO.setPreferencia("Preferencia");
        pessoaUpdateDTO.setAtivo("Ativo");

        Pessoa pessoa = new Pessoa();
        pessoa.setNome(pessoaUpdateDTO.getNome());
        pessoa.setSobrenome(pessoaUpdateDTO.getSobrenome());
        pessoa.setCpf(pessoaUpdateDTO.getCpf());
        pessoa.setCelular(pessoaUpdateDTO.getCelular());
        pessoa.setDataNascimento(pessoaUpdateDTO.getDataNascimento());
        pessoa.setPreferencia(pessoaUpdateDTO.getPreferencia());
        pessoa.setAtivo(pessoaUpdateDTO.getAtivo());

        Pessoa pessoaExistente = new Pessoa();
        pessoaExistente.setIdPessoa(1);
        when(pessoaRepository.findById(1)).thenReturn(Optional.of(pessoaExistente));

        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        Pessoa pessoaAtualizada = pessoaService.atualizarPessoa(1, pessoaUpdateDTO);

        assertEquals(pessoa, pessoaAtualizada);
    }

    @Tag("Teste_para_atualizar_uma_Pessoa_com_excecao")
    @Test
    public void testarAtualizarUmaPessoaComExcecao() {
        PessoaUpdateDTO pessoaUpdateDTO = new PessoaUpdateDTO();
        pessoaUpdateDTO.setNome("Nome");
        pessoaUpdateDTO.setSobrenome("Sobrenome");
        pessoaUpdateDTO.setCpf("12345678901");
        pessoaUpdateDTO.setCelular("11999999999");
        pessoaUpdateDTO.setDataNascimento(new Date());
        pessoaUpdateDTO.setPreferencia("Preferencia");
        pessoaUpdateDTO.setAtivo("Ativo");

        Pessoa pessoaExistente = new Pessoa();
        pessoaExistente.setIdPessoa(1);
        when(pessoaRepository.findById(1)).thenReturn(Optional.of(pessoaExistente));

        when(pessoaRepository.save(any(Pessoa.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(RuntimeException.class, () -> {
            pessoaService.atualizarPessoa(1, pessoaUpdateDTO);
        });
    }

    @Tag("Teste_para_buscar_todas_as_Pessoas")
    @Test
    public void testarBuscarTodasPessoas() throws RegraDeNegocioException {
        Pageable pageable = PageRequest.of(0,10);

        List<Pessoa> pessoas = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            Pessoa p = new Pessoa();
            p.setIdPessoa(i);
            pessoas.add(p);
        }

        Page<Pessoa> pessoaPage = new PageImpl<>(pessoas);

        when(pessoaRepository.findAll(any(Pageable.class))).thenReturn(pessoaPage);

        Page<Pessoa> result = pessoaService.buscarTodasPessoas(pageable);

        assertEquals(pessoaPage, result);
    }

    @Tag("Teste_para_buscar_uma_Pessoa_por_id")
    @Test
    public void testarBuscarPessoaPorId() throws RegraDeNegocioException {
        Pessoa pessoa = new Pessoa();
        pessoa.setIdPessoa(1);

        when(pessoaRepository.findById(1)).thenReturn(Optional.of(pessoa));

        Pessoa result = pessoaService.buscarPessoaPorId(1);

        assertEquals(pessoa, result);
    }


    @Tag("Teste_para_inativar_uma_Pessoa")
    @Test
    public void testarInativarPessoa() {
        Integer idPessoa = 1;
        Pessoa pessoa = new Pessoa();
        pessoa.setIdPessoa(idPessoa);
        pessoa.setAtivo("1");

        when(pessoaRepository.findById(idPessoa)).thenReturn(Optional.of(pessoa));

        pessoaService.inativarPessoa(idPessoa);

        assertEquals(String.valueOf(Ativo.INATIVO.getIndex()), pessoa.getAtivo());
        verify(pessoaRepository).save(any(Pessoa.class));
    }

    @Tag("Teste_para_ativar_uma_Pessoa")
    @Test
    public void testarAtivarPessoa() {
        Integer idPessoa = 1;
        Pessoa pessoa = new Pessoa();
        pessoa.setIdPessoa(idPessoa);
        pessoa.setAtivo("0");

        when(pessoaRepository.findById(idPessoa)).thenReturn(Optional.of(pessoa));

        pessoaService.ativarPessoa(idPessoa);

        assertEquals(String.valueOf(Ativo.ATIVO.getIndex()), pessoa.getAtivo());
        verify(pessoaRepository).save(any(Pessoa.class));

    }
}