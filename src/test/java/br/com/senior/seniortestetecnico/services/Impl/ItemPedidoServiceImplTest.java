package br.com.senior.seniortestetecnico.services.Impl;

import br.com.senior.seniortestetecnico.dto.ItemPedidoDTOInput;
import br.com.senior.seniortestetecnico.entidade.Item;
import br.com.senior.seniortestetecnico.entidade.ItemPedido;
import br.com.senior.seniortestetecnico.entidade.Pedido;
import br.com.senior.seniortestetecnico.enums.EnumTipoItem;
import br.com.senior.seniortestetecnico.repositories.ItemPedidoRepository;
import br.com.senior.seniortestetecnico.services.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemPedidoServiceImplTest {

    @InjectMocks
    private ItemPedidoServiceImpl itemPedidoServiceImpl;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;
    @Mock
    private ItemService itemService;
    private Pedido pedido;
    private Item produto, servico;
    private ItemPedido itemPedidoProduto, itemPedidoServico;
    private ItemPedidoDTOInput inputProduto, inputServico;
    private Page<ItemPedido> itemPedidoPage;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        inputProduto = new ItemPedidoDTOInput("50322e28-eb89-4535-9fa4-16bbe04f899c", 1);
        inputServico = new ItemPedidoDTOInput("22740266-9f42-470f-894c-9a183c2db0f7", 1);

        produto = new Item(UUID.fromString("50322e28-eb89-4535-9fa4-16bbe04f899c"), "Item 1", "Descrição 1", EnumTipoItem.PRODUTO, 10.00, true);
        servico = new Item(UUID.fromString("22740266-9f42-470f-894c-9a183c2db0f7"), "Item 1", "Descrição 1", EnumTipoItem.SERVICO, 10.00, true);

        itemPedidoProduto = new ItemPedido();
        itemPedidoProduto.setPedido(pedido);
        itemPedidoProduto.setQuantidade(1);
        itemPedidoProduto.setItem(produto);
        itemPedidoProduto.setValorDesconto(0.00);
        itemPedidoProduto.setValorBruto(10.00);
        itemPedidoProduto.setValorLiquido(10.00);
        itemPedidoProduto.setAtivo(true);

        itemPedidoServico = new ItemPedido();
        itemPedidoServico.setPedido(pedido);
        itemPedidoServico.setQuantidade(1);
        itemPedidoServico.setItem(servico);
        itemPedidoServico.setValorDesconto(0.00);
        itemPedidoServico.setValorBruto(10.00);
        itemPedidoServico.setValorLiquido(10.00);
        itemPedidoServico.setAtivo(true);

        itemPedidoPage = new PageImpl<>(List.of(itemPedidoProduto, itemPedidoServico));

    }

    @Test
    @DisplayName("Deve criar um item de pedido do tipo produto")
    void testCreateProduto() {
        when(itemService.getById(any())).thenReturn(produto);
        when(itemPedidoRepository.save(any())).thenReturn(itemPedidoProduto);

        ItemPedido itemPedido = itemPedidoServiceImpl.create(pedido, inputProduto);

        assertEquals(itemPedidoProduto.getId(), itemPedido.getId());
        assertEquals(itemPedidoProduto.getPedido(), itemPedido.getPedido());
        assertEquals(itemPedidoProduto.getQuantidade(), itemPedido.getQuantidade());
        assertEquals(itemPedidoProduto.getItem(), itemPedido.getItem());
        assertEquals(itemPedidoProduto.getValorDesconto(), itemPedido.getValorDesconto());
        assertEquals(itemPedidoProduto.getValorBruto(), itemPedido.getValorBruto());
        assertEquals(itemPedidoProduto.getValorLiquido(), itemPedido.getValorLiquido());
        assertEquals(itemPedidoProduto.getAtivo(), itemPedido.getAtivo());
        assertEquals(itemPedidoProduto.getTipoItem(), itemPedido.getTipoItem());
    }

    @Test
    @DisplayName("Deve criar um item de pedido do tipo serviço")
    void testCreateServico() {
        when(itemService.getById(any())).thenReturn(servico);
        when(itemPedidoRepository.save(any())).thenReturn(itemPedidoServico);

        ItemPedido itemPedido = itemPedidoServiceImpl.create(pedido, inputServico);

        assertEquals(itemPedidoServico.getId(), itemPedido.getId());
        assertEquals(itemPedidoServico.getPedido(), itemPedido.getPedido());
        assertEquals(itemPedidoServico.getQuantidade(), itemPedido.getQuantidade());
        assertEquals(itemPedidoServico.getItem(), itemPedido.getItem());
        assertEquals(itemPedidoServico.getValorDesconto(), itemPedido.getValorDesconto());
        assertEquals(itemPedidoServico.getValorBruto(), itemPedido.getValorBruto());
        assertEquals(itemPedidoServico.getValorLiquido(), itemPedido.getValorLiquido());
        assertEquals(itemPedidoServico.getAtivo(), itemPedido.getAtivo());
        assertEquals(itemPedidoServico.getTipoItem(), itemPedido.getTipoItem());


    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar criar um item de pedido com um item inativo")
    void testCreateItemInativo() {
        produto.setAtivo(false);
        when(itemService.getById(any())).thenReturn(produto);

        assertThrows(IllegalArgumentException.class, () -> itemPedidoServiceImpl.create(pedido, inputProduto));
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar criar um item de pedido com um item inexistente")
    void testCreateItemInexistente() {
        when(itemService.getById(any())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> itemPedidoServiceImpl.create(pedido, inputProduto));
    }

    @Test
    @DisplayName("Deve lançar uma exceção quando a quantidade for menor ou igual a 0")
    void testCreateQuantidadeInvalida() {
        inputProduto = new ItemPedidoDTOInput("50322e28-eb89-4535-9fa4-16bbe04f899c", 0);

        assertThrows(IllegalArgumentException.class, () -> itemPedidoServiceImpl.create(pedido, inputProduto));
    }

    @Test
    @DisplayName("Deve aplicar desconto ao item do pedido")
    void testApplyDiscount() {
        Integer percentualDesconto = 10;
        Double valorDesconto = 1.00;
        itemPedidoProduto.setValorDesconto(valorDesconto);
        itemPedidoProduto.setValorLiquido(9.00);
        when(itemPedidoRepository.findById(any())).thenReturn(Optional.of(itemPedidoProduto));

        itemPedidoServiceImpl.applyDiscount(itemPedidoProduto, percentualDesconto);

        assertEquals(valorDesconto, itemPedidoProduto.getValorDesconto());
        assertEquals(9.00, itemPedidoProduto.getValorLiquido());
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar aplicar um desconto inválido")
    void testApplyDiscountInvalido() {
        Integer percentualDesconto = 101;
        when(itemPedidoRepository.findById(any())).thenReturn(Optional.of(itemPedidoProduto));
        assertThrows(IllegalArgumentException.class, () -> itemPedidoServiceImpl.applyDiscount(itemPedidoProduto, percentualDesconto));
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar aplicar um desconto em um item de pedido inexistente")
    void testApplyDiscountItemInexistente() {
        Integer percentualDesconto = 10;

        when(itemPedidoRepository.findById(any())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> itemPedidoServiceImpl.applyDiscount(itemPedidoProduto, percentualDesconto));
    }

    @Test
    @DisplayName("Deve retornar uma página de itens de pedido")
    void testGetAllItemPedidos() {
        when(itemPedidoRepository.findAllByPedidoId(any(), any())).thenReturn(new PageImpl<>(List.of(itemPedidoProduto, itemPedidoServico)));
        var itemPedidoPageTest = itemPedidoServiceImpl.getAllItemPedidos(UUID.randomUUID(), null);

        assertEquals(2, itemPedidoPageTest.getTotalElements());
        assertEquals(itemPedidoProduto, itemPedidoPageTest.getContent().get(0));
        assertEquals(itemPedidoServico, itemPedidoPageTest.getContent().get(1));
    }

    @Test
    @DisplayName("Deve retornar um item de pedido pelo id")
    void testReadById() {
        when(itemPedidoRepository.findById(any())).thenReturn(Optional.of(itemPedidoProduto));

        ItemPedido itemPedido = itemPedidoServiceImpl.readById(UUID.randomUUID());

        assertEquals(itemPedidoProduto, itemPedido);
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar retornar um item de pedido inexistente")
    void testReadByIdInexistente() {
        when(itemPedidoRepository.findById(any())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> itemPedidoServiceImpl.readById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Deve atualizar a quantidade de um item de pedido")
    void testUpdate() {
        Integer quantidade = 2;
        when(itemPedidoRepository.findById(any())).thenReturn(Optional.of(itemPedidoProduto));

        itemPedidoServiceImpl.update(UUID.randomUUID(), quantidade);

        assertEquals(quantidade, itemPedidoProduto.getQuantidade());
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar atualizar a quantidade de um item de pedido inexistente")
    void testUpdateInexistente() {
        when(itemPedidoRepository.findById(any())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> itemPedidoServiceImpl.update(UUID.randomUUID(), 2));
    }

    @Test
    @DisplayName("Deve deletar um item de pedido")
    void testDelete() {
        when(itemPedidoRepository.findById(any())).thenReturn(Optional.of(itemPedidoProduto));

        itemPedidoServiceImpl.delete(UUID.randomUUID());
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar deletar um item de pedido inexistente")
    void testDeleteInexistente() {
        when(itemPedidoRepository.findById(any())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> itemPedidoServiceImpl.delete(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Deve retornar verdadeiro se o item de pedido está sendo usado")
    void testIsItemUsed() {
        when(itemPedidoRepository.existsByItemId(any())).thenReturn(true);

        assertTrue(itemPedidoServiceImpl.isItemUsed(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Deve retornar falso se o item de pedido não está sendo usado")
    void testIsItemNotUsed() {
        when(itemPedidoRepository.existsByItemId(any())).thenReturn(false);

        assertFalse(itemPedidoServiceImpl.isItemUsed(UUID.randomUUID()));
    }

}