package br.com.senior.seniortestetecnico.services.Impl;

import br.com.senior.seniortestetecnico.dto.ItemPedidoDTOInput;
import br.com.senior.seniortestetecnico.dto.ItemPedidoDTOOutput;
import br.com.senior.seniortestetecnico.dto.PedidoDTOCompletoOutput;
import br.com.senior.seniortestetecnico.dto.PedidoDTOInput;
import br.com.senior.seniortestetecnico.entidade.Item;
import br.com.senior.seniortestetecnico.entidade.ItemPedido;
import br.com.senior.seniortestetecnico.entidade.Pedido;
import br.com.senior.seniortestetecnico.enums.EnumSituacaoPedido;
import br.com.senior.seniortestetecnico.enums.EnumTipoItem;
import br.com.senior.seniortestetecnico.repositories.PedidoRepository;
import br.com.senior.seniortestetecnico.services.ItemPedidoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @InjectMocks
    private PedidoServiceImpl service;
    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private ItemPedidoService itemPedidoService;
    @Mock
    private Pageable pageable;
    private Page<Pedido> pedidoPage;
    private Pedido pedido;
    private PedidoDTOInput dto;
    private ItemPedidoDTOInput inputProduto, inputServico;
    private List<ItemPedidoDTOInput> itensPedidoDTOInput;
    private ItemPedidoDTOOutput outputProduto, outputServico;
    private PedidoDTOCompletoOutput dtoOutput;
    private ItemPedido itemPedido, itemPedidoServico;
    private List<ItemPedido> itensPedido;
    @Captor
    private ArgumentCaptor<Pedido> pedidoCaptor;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        pedido.setValorBruto(10.00);
        pedido.setValorDesconto(0.00);
        pedido.setValorTotal(10.00);
        pedido.setSituacao(EnumSituacaoPedido.ABERTO);
        pedido.setDataEmissao(LocalDateTime.now());

        itemPedido = new ItemPedido();
        itemPedido.setPedido(pedido);
        itemPedido.setQuantidade(1);
        itemPedido.setItem(new Item(UUID.fromString("50322e28-eb89-4535-9fa4-16bbe04f899c"), "Item 1", "Descrição 1", EnumTipoItem.PRODUTO, 10.00, true));
        itemPedido.setValorDesconto(0.00);
        itemPedido.setValorBruto(10.00);
        itemPedido.setValorLiquido(10.00);
        itemPedido.setAtivo(true);

        itemPedidoServico = new ItemPedido();
        itemPedidoServico.setPedido(pedido);
        itemPedidoServico.setQuantidade(1);
        itemPedidoServico.setItem(new Item(UUID.fromString("22740266-9f42-470f-894c-9a183c2db0f7"), "Item 1", "Descrição 1", EnumTipoItem.PRODUTO, 10.00, true));
        itemPedidoServico.setValorDesconto(0.00);
        itemPedidoServico.setValorBruto(10.00);
        itemPedidoServico.setValorLiquido(10.00);
        itemPedidoServico.setAtivo(true);

        itensPedido = new ArrayList<>();
        itensPedido.add(itemPedido);
        itensPedido.add(itemPedidoServico);

        inputProduto = new ItemPedidoDTOInput("50322e28-eb89-4535-9fa4-16bbe04f899c", 1);
        inputServico = new ItemPedidoDTOInput("22740266-9f42-470f-894c-9a183c2db0f7", 1);

        itensPedidoDTOInput = new ArrayList<>();
        itensPedidoDTOInput.add(inputProduto);
        itensPedidoDTOInput.add(inputServico);

        dto = new PedidoDTOInput(itensPedidoDTOInput);

        pedidoPage = new PageImpl<>(List.of(pedido));

        pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
    }

    @Test
    @DisplayName("Deve criar um novo pedido de Produto")
    void testCriarNovoPedido() {
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(itemPedidoService.create(any(Pedido.class), any())).thenReturn(itemPedido);
        when(service.create(dto)).thenReturn(pedido);

        Pedido result = service.create(dto);;

        assertEquals(result.getValorDesconto(), pedido.getValorDesconto());
        assertEquals(result.getValorBruto(), pedido.getValorBruto());
        assertEquals(result.getValorTotal(), pedido.getValorTotal());
    }

    @Test
    @DisplayName("Deve criar um novo pedido de Serviço")
    void testCriarNovoPedidoServico() {
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(itemPedidoService.create(any(Pedido.class), any())).thenReturn(itemPedidoServico);
        when(service.create(dto)).thenReturn(pedido);

        Pedido result = service.create(dto);;

        assertEquals(result.getValorDesconto(), pedido.getValorDesconto());
        assertEquals(result.getValorBruto(), pedido.getValorBruto());
        assertEquals(result.getValorTotal(), pedido.getValorTotal());
    }

    @Test
    @DisplayName("Deve adicionar um item ao pedido")
    void testAdicionarItemAoPedido() {
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any())).thenReturn(pedido);
        when(itemPedidoService.create(any(), any())).thenReturn(itemPedido);
        when(itemPedidoService.getAllItemPedidos(any(), any())).thenReturn(new PageImpl<>(itensPedido));

        var result = service.addItem(pedido.getId(), dto);
        verify(pedidoRepository).save(any(Pedido.class));
        verify(itemPedidoService, times(dto.itensPedido().size())).create(any(Pedido.class), any());
        assertEquals(result.valorDesconto(), pedido.getValorDesconto());
        assertEquals(result.valorBruto(), pedido.getValorBruto());
        assertEquals(result.valorTotal(), pedido.getValorTotal());
        assertEquals(result.itensPedido().size(), itensPedido.size());
    }

    @Test
    @DisplayName("Não deve adicionar um item inativo ao pedido")
    void testAdicionarItemInativoAoPedido() {
        itemPedido.getItem().setAtivo(false);
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any())).thenReturn(pedido);
        when(itemPedidoService.create(any(), any())).thenReturn(itemPedido);
        when(itemPedidoService.getAllItemPedidos(any(), any())).thenReturn(new PageImpl<>(itensPedido));

        var result = service.addItem(pedido.getId(), dto);
        verify(pedidoRepository).save(any(Pedido.class));
        verify(itemPedidoService, times(dto.itensPedido().size())).create(any(Pedido.class), any());
        assertEquals(result.valorDesconto(), pedido.getValorDesconto());
        assertEquals(result.valorBruto(), pedido.getValorBruto());
        assertEquals(result.valorTotal(), pedido.getValorTotal());
        assertEquals(result.itensPedido().size(), itensPedido.size());
    }

    @Test
    @DisplayName("Não deve adicionar um item ao pedido fechado")
    void testAdicionarItemAoPedidoFechado() {
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
        pedido.setSituacao(EnumSituacaoPedido.FECHADO);

        assertThrows(IllegalArgumentException.class, () -> service.addItem(pedido.getId(), dto));
    }

    @Test
    @DisplayName("Deve aplicar desconto ao pedido")
    void testAplicarDescontoAoPedido() {
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any())).thenReturn(pedido);
        when(itemPedidoService.getAllItemPedidos(any(), any())).thenReturn(new PageImpl<>(itensPedido));
        when(itemPedidoService.applyDiscount(any(), anyInt())).thenReturn(9.00);

        var result = service.applyDiscount(pedido.getId(), 10);
        verify(pedidoRepository).save(any(Pedido.class));
        verify(itemPedidoService, times(itensPedido.size())).applyDiscount(any(), anyInt());
        assertEquals(result.valorDesconto(), pedido.getValorDesconto());
        assertEquals(result.valorBruto(), pedido.getValorBruto());
        assertEquals(result.valorTotal(), pedido.getValorTotal());
        assertEquals(result.itensPedido().size(), itensPedido.size());
    }

    @Test
    @DisplayName("Não deve aplicar desconto à um pedido fechado")
    void testAplicarDescontoPedidoFechado() {
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
        pedido.setSituacao(EnumSituacaoPedido.FECHADO);

        assertThrows(IllegalArgumentException.class, () -> service.applyDiscount(pedido.getId(), 10));
    }

    @Test
    @DisplayName("Deve fechar um pedido")
    void testFecharPedido() {
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any())).thenReturn(pedido);

        service.fecharPedido(pedido.getId());
        verify(pedidoRepository).save(pedidoCaptor.capture());

        assertEquals(pedidoCaptor.getValue().getSituacao(), EnumSituacaoPedido.FECHADO);
        assertNotNull(pedidoCaptor.getValue().getDataFechamento());
    }

    @Test
    @DisplayName("Não deve fechar um pedido fechado")
    void testFecharPedidoFechado() {
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
        pedido.setSituacao(EnumSituacaoPedido.FECHADO);

        assertThrows(IllegalArgumentException.class, () -> service.fecharPedido(pedido.getId()));
    }

    @Test
    @DisplayName("Deve inativar um pedido")
    void testInativarPedido() {
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any())).thenReturn(pedido);

        service.inactivate(pedido.getId());
        verify(pedidoRepository).save(pedidoCaptor.capture());

        assertFalse(pedidoCaptor.getValue().isAtivo());
    }

    @Test
    @DisplayName("Não deve inativar um pedido inativo")
    void testInativarPedidoInativo() {
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
        pedido.setAtivo(false);

        assertThrows(IllegalArgumentException.class, () -> service.inactivate(pedido.getId()));
    }

    @Test
    @DisplayName("Deve retornar um pedido por id")
    void testRetornarPedidoPorId() {
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));

        var result = service.readById(pedido.getId());
        assertEquals(result, pedido);
    }

    @Test
    @DisplayName("Não deve retornar um pedido inexistente")
    void testRetornarPedidoInexistente() {
        when(pedidoRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.readById(pedido.getId()));
    }

    @Test
    @DisplayName("Deve retornar um pedido completo por id")
    void testRetornarPedidoCompletoPorId() {
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
        when(itemPedidoService.getAllItemPedidos(any(), any())).thenReturn(new PageImpl<>(itensPedido));

        var result = service.readByIdCompleto(pedido.getId());
        assertEquals(result.valorDesconto(), pedido.getValorDesconto());
        assertEquals(result.valorBruto(), pedido.getValorBruto());
        assertEquals(result.valorTotal(), pedido.getValorTotal());
        assertEquals(result.itensPedido().size(), itensPedido.size());
    }

    @Test
    @DisplayName("Deve retornar todos os pedidos")
    void testRetornarTodosPedidos() {
        when(pedidoRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(pedido)));
        var result = service.getAllPedidos(pageable);

        assertEquals(result.getTotalElements(), pedidoPage.getTotalElements());
    }


}