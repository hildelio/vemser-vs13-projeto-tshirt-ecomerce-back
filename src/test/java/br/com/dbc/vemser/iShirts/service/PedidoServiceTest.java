package br.com.dbc.vemser.iShirts.service;

import br.com.dbc.vemser.iShirts.dto.pedido.PedidoCreateDTO;
import br.com.dbc.vemser.iShirts.dto.pedido.PedidoDTO;
import br.com.dbc.vemser.iShirts.dto.pedido.PedidoUpdateDTO;
import br.com.dbc.vemser.iShirts.exceptions.RegraDeNegocioException;
import br.com.dbc.vemser.iShirts.model.*;
import br.com.dbc.vemser.iShirts.model.enums.StatusPedido;
import br.com.dbc.vemser.iShirts.repository.PedidoRepository;
import br.com.dbc.vemser.iShirts.service.mocks.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("PedidoService - Test")
class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private PessoaService pessoaService;
    @Mock
    private CarrinhoService carrinhoService;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private CupomService cupomService;
    @Mock
    private ObjectMapper objectMapper;

    @Test
    void criarPedido() throws IOException, RegraDeNegocioException {
        Usuario usuario = MockUsuario.retornarEntity();
        Carrinho carrinho = MockCarrinho.retornarEntity();
        Pedido pedido = MockPedido.retornarEntity();
        PedidoDTO pedidoDTO = MockPedido.retornarPedidoDTO();


        when(usuarioService.buscarUsuarioLogadoEntity()).thenReturn(usuario);
        when(carrinhoService.buscarCarrinhoUsuarioLogado()).thenReturn(carrinho);
        when(pedidoRepository.save(any())).thenReturn(pedido);
        doNothing().when(carrinhoService).limparCarrinhoPedidoFeito();
        when(objectMapper.convertValue(any(), eq(PedidoDTO.class))).thenReturn(pedidoDTO);

        PedidoDTO pedidoCriado = pedidoService.criarPedido(MockPedido.retornarPedidoCreateDTO());


        verify(usuarioService, times(1)).buscarUsuarioLogadoEntity();
        verify(carrinhoService, times(1)).buscarCarrinhoUsuarioLogado();
        verify(carrinhoService, times(1)).limparCarrinhoPedidoFeito();
        assertEquals(pedidoCriado, pedidoDTO);

    }

    @DisplayName("Deveria retorna um valor subtraido do valor minimo do cupom")
    @Test
    void deveriaRetornaValorSubtraidoMinimoCupom() throws RegraDeNegocioException {
        BigDecimal valorCarrinho = BigDecimal.valueOf(1000);
        Integer idCupom = new Random().nextInt();
        Cupom cupom = MockCupom.retornarCupom();

        when(cupomService.getCupom(anyInt())).thenReturn(cupom);

        BigDecimal bigDecimalCurrent = pedidoService.validarCupom(valorCarrinho, idCupom);
        assertEquals(0, bigDecimalCurrent.intValue());
    }

    @DisplayName("Deveria montar pedido com cupom nulo")
    @Test
    void deveriaMontarPedidoCupomNulo() throws IOException, RegraDeNegocioException {
        Usuario usuario = MockUsuario.retornarEntity();
        PedidoCreateDTO pedidoCreateDTO = MockPedido.retornarPedidoCreateDTO();
        Carrinho carrinho = MockCarrinho.retornarEntity();
        Cupom cupomMock = MockCupom.retornarCupom();
        cupomMock.setValorMinimo(900.0);


        when(pessoaService.buscarPessoaPorUsuario(usuario)).thenReturn(new Pessoa());
        when(cupomService.getCupom(anyInt())).thenReturn(cupomMock);

        Pedido pedidoCurrent = pedidoService.montarPedido(usuario, pedidoCreateDTO, carrinho);
        assertNotNull(pedidoCurrent);
        assertEquals(pedidoCurrent.getStatus(), StatusPedido.EM_ANDAMENTO);
    }

    @DisplayName("Deveria retornar o valor do carrinho ao tentar recuperar cupom")
    @Test
    void deveriaRetornaValorCarrinhoNuloRecuperarCupom() throws RegraDeNegocioException {
        BigDecimal valorCarrinho = BigDecimal.valueOf(25);
        BigDecimal bigDecimalCurrent = pedidoService.validarCupom(valorCarrinho, 0);

        assertNotNull(bigDecimalCurrent);
        assertEquals(bigDecimalCurrent.intValue(),valorCarrinho.intValue());
    }

    @Test
    void editarPedido() throws IOException, RegraDeNegocioException {
        Pedido pedido = MockPedido.retornarEntity();
        PedidoUpdateDTO pedidoUpdateDTO = MockPedido.retornarPedidoUpdateDTO();
        PedidoDTO pedidoDTO = MockPedido.retornarPedidoDTO();

        when(pedidoRepository.findById(anyInt())).thenReturn(Optional.of(pedido));
        when(objectMapper.convertValue(any(), eq(PedidoDTO.class))).thenReturn(pedidoDTO);

        PedidoDTO pedidoEditado = pedidoService.editarPedido(pedido.getIdPedido(), pedidoUpdateDTO);

        verify(pedidoRepository, times(1)).save(pedido);
        assertEquals(pedidoEditado, pedidoDTO);

    }

    @Test
    void listarPedidosPorId() throws IOException, RegraDeNegocioException {

        Pedido pedido = MockPedido.retornarEntity();
        PedidoDTO pedidoDTO = MockPedido.retornarPedidoDTO();

        when(pedidoRepository.findById(anyInt())).thenReturn(Optional.of(pedido));
        when(objectMapper.convertValue(any(Pedido.class), eq(PedidoDTO.class))).thenReturn(pedidoDTO);

        PedidoDTO pedidoListado = pedidoService.listarPedidoPorId(pedido.getIdPedido());

        verify(pedidoRepository, times(1)).findById(pedido.getIdPedido());
        assertEquals(pedidoListado, pedidoDTO);

    }

    @Test
    void listarPedidos() throws IOException {
        Pageable pageable = PageRequest.of(0, 10);
        List<Pedido> pedidos = MockPedido.retornarListaPedidoEntity();
        List<PedidoDTO> pedidosDTO = MockPedido.retornarListaPedidoDTO();
        Page<Pedido> pedidosPageEntity = new PageImpl<>(pedidos, pageable, pedidos.size());

        when(pedidoRepository.findAllBy(pageable)).thenReturn(pedidosPageEntity);
        when(objectMapper.convertValue(any(Pedido.class), eq(PedidoDTO.class))).thenReturn(pedidosDTO.get(0));

        Page<PedidoDTO> pedidosListados = pedidoService.listarPedidos(pageable);

        assertEquals(pedidosPageEntity.getSize(), pedidosListados.getSize());
        assertEquals(pedidosDTO.get(0), pedidosListados.getContent().get(0));
    }

    @Test
    void listarPedidosPorIdPessoa() throws IOException, RegraDeNegocioException {
        Pessoa pessoa = MockPessoa.retornarEntity();
        Pageable pageable = PageRequest.of(0, 10);
        List<Pedido> pedidos = MockPedido.retornarListaPedidoEntity();
        List<PedidoDTO> pedidosDTO = MockPedido.retornarListaPedidoDTO();
        Page<Pedido> pedidosPageEntity = new PageImpl<>(pedidos, pageable, pedidos.size());

        when(pessoaService.buscarPessoaPorId(anyInt())).thenReturn(pessoa);
        when(pedidoRepository.listPedidosPorPessoa(any(),eq(pageable))).thenReturn(pedidosPageEntity);
        when(objectMapper.convertValue(any(Pedido.class), eq(PedidoDTO.class))).thenReturn(pedidosDTO.get(0));

        Page<PedidoDTO> pedidosListados = pedidoService.listarPedidosPorIdPessoa(pessoa.getIdPessoa(), pageable);

        verify(pedidoRepository, times(1)).listPedidosPorPessoa(pessoa, pageable);
        assertEquals(pedidosPageEntity.getSize(), pedidosListados.getSize());
        assertEquals(pedidosDTO.get(0), pedidosListados.getContent().get(0));
    }

    @Test
    void excluirPedido() throws IOException, RegraDeNegocioException {

        Pedido pedido = MockPedido.retornarEntity();
        Pedido pedidoExcluido = MockPedido.retornarEntity();
        pedidoExcluido.setIdPedido(2);

        when(pedidoRepository.findById(anyInt())).thenReturn(Optional.of(pedido));

        pedidoService.deletarPedido(pedidoExcluido.getIdPedido());

        assertNotEquals(pedidoExcluido.getStatus(), pedido.getStatus());

    }

    @Test
    void deveriaValidarCupomValorCarrinhoMenorQueValorMinimoLancaExcecao() throws RegraDeNegocioException {
        BigDecimal valorCarrinho = BigDecimal.valueOf(50);
        Integer idCupom = 1;
        Cupom cupom = MockCupom.retornarCupom();

        when(cupomService.getCupom(idCupom)).thenReturn(cupom);

        assertThrows(RegraDeNegocioException.class, () -> pedidoService.validarCupom(valorCarrinho, idCupom));
    }

    @Test
    @DisplayName("Deveria lançar exceção quando o carrinho está vazio")
    void deveriaLancarExcecaoQUandoOcarrinhoEstaVazio() {
        Usuario usuario = MockUsuario.retornarEntity();
        PedidoCreateDTO pedidoCreateDTO = new PedidoCreateDTO();
        Carrinho carrinho = new Carrinho();

        when(pessoaService.buscarPessoaPorUsuario(usuario)).thenReturn(new Pessoa());

        assertThrows(RegraDeNegocioException.class, () -> pedidoService.montarPedido(usuario, pedidoCreateDTO, carrinho));
    }

}