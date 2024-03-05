package br.com.senior.seniortestetecnico.services.Impl;


import br.com.senior.seniortestetecnico.entidade.Item;
import br.com.senior.seniortestetecnico.enums.EnumTipoItem;
import br.com.senior.seniortestetecnico.repositories.ItemRepository;
import br.com.senior.seniortestetecnico.services.ItemPedidoService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl service;
    @Mock
    private ItemRepository repository;
    @Mock
    private ItemPedidoService itemPedidoService;
    @Captor
    ArgumentCaptor<Item> captor;
    private Item item;
    Pageable pageable;
    Page<Item> page;

    @BeforeEach()
    void setUp(){
        captor = ArgumentCaptor.forClass(Item.class);

        item = new Item();
        item.setId(UUID.fromString("8d175726-cf6b-4d78-962b-2860cfb6af43"));
        item.setNome("nome");
        item.setDescricao("descricao");
        item.setPrecoUnitario(10.0);
        item.setTipoItem(EnumTipoItem.PRODUTO);
        item.setAtivo(true);

        pageable = PageRequest.of(0, 10);
        page = new PageImpl<>(List.of(item));
    }

    @Test
    @DisplayName("Deve criar um item do tipo Produto")
    void testCreateProduct() {
        when(repository.save(captor.capture())).thenReturn(item);

        service.create("nome", "descricao", EnumTipoItem.PRODUTO ,10.0);
        verify(repository).save(captor.capture());

        var itemCapturado = captor.getValue();

        assertEquals("nome", itemCapturado.getNome());
        assertEquals("descricao", itemCapturado.getDescricao());
        assertEquals(10.0, itemCapturado.getPrecoUnitario());
        assertEquals(EnumTipoItem.PRODUTO, itemCapturado.getTipoItem());
        assertTrue(itemCapturado.getAtivo());
    }

    @Test
    @DisplayName("Deve criar um item do tipo Serviço")
    void testCreateService() {
        when(repository.save(captor.capture())).thenReturn(item);

        service.create("nome", "descricao", EnumTipoItem.SERVICO ,10.0);
        verify(repository).save(captor.capture());

        var itemCapturado = captor.getValue();

        assertEquals("nome", itemCapturado.getNome());
        assertEquals("descricao", itemCapturado.getDescricao());
        assertEquals(10.0, itemCapturado.getPrecoUnitario());
        assertEquals(EnumTipoItem.SERVICO, itemCapturado.getTipoItem());
        assertTrue(itemCapturado.getAtivo());
    }


    @Test
    @DisplayName("Deve atualizar um item")
    void testUpdateItem() {
        when(repository.findById(any())).thenReturn(Optional.of(item));

        service.update(UUID.fromString("8d175726-cf6b-4d78-962b-2860cfb6af43"), "nome2", "descricao2", 123.0);
        verify(repository).save(captor.capture());

        assertEquals("nome2", captor.getValue().getNome());
        assertEquals("descricao2", captor.getValue().getDescricao());
        assertEquals(123.0, captor.getValue().getPrecoUnitario());
        assertTrue(captor.getValue().getAtivo());
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar atualizar um item inexistente")
    void testUpdateItemException() {
        when(repository.findById(UUID.fromString("8d175726-cf6b-4d78-962b-2860cfb6af43"))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(UUID.fromString("8d175726-cf6b-4d78-962b-2860cfb6af43"), "nome2", "descricao2", 10.0));
    }

    @Test
    @DisplayName("Deve inativar um item")
    void testInactivateItem() {
        when(repository.findById(UUID.fromString("8d175726-cf6b-4d78-962b-2860cfb6af43"))).thenReturn(java.util.Optional.of(item));

        service.inactivate(UUID.fromString("8d175726-cf6b-4d78-962b-2860cfb6af43"));
        verify(repository).save(captor.capture());

        assertFalse(captor.getValue().getAtivo());
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar inativar um item inexistente")
    void testInactivateItemException() {
        when(repository.findById(UUID.fromString("8d175726-cf6b-4d78-962b-2860cfb6af43"))).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.inactivate(UUID.fromString("8d175726-cf6b-4d78-962b-2860cfb6af43")));
    }

    @Test
    @DisplayName("Deve retornar uma página de itens")
    void testGetAllItems() {
        when(repository.findAllByAtivoTrue(pageable)).thenReturn(page);
        var itens = service.getAllItems(pageable);

        assertEquals(1, itens.getTotalElements());
        assertEquals("nome", itens.getContent().get(0).getNome());
        assertEquals("descricao", itens.getContent().get(0).getDescricao());
        assertEquals(10.0, itens.getContent().get(0).getPrecoUnitario());
        assertEquals(EnumTipoItem.PRODUTO, itens.getContent().get(0).getTipoItem());
        assertTrue(itens.getContent().get(0).getAtivo());

    }

    @Test
    @DisplayName("Deve retornar um item")
    void testGetItem() {
        when(repository.findById(any())).thenReturn(Optional.of(item));

        var produtoCapturado = service.getById(UUID.fromString("8d175726-cf6b-4d78-962b-2860cfb6af43"));

        assertEquals("nome", produtoCapturado.getNome());
        assertEquals("descricao", produtoCapturado.getDescricao());
        assertEquals(10.0, produtoCapturado.getPrecoUnitario());
        assertEquals(EnumTipoItem.PRODUTO, produtoCapturado.getTipoItem());
        assertTrue(produtoCapturado.getAtivo());
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar retornar um item inexistente")
    void testGetItemException() {
        when(repository.findById(any())).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getById(UUID.fromString("8d175726-cf6b-4d78-962b-2860cfb6af43")));
    }

    @Test
    @DisplayName("Deve excluir um item")
    void testDeleteItem(){
        when(repository.findById(any())).thenReturn(Optional.of(item));
        when(itemPedidoService.isItemUsed(any())).thenReturn(false);

        service.delete(UUID.fromString("8d175726-cf6b-4d78-962b-2860cfb6af43"));
        verify(repository).deleteById(UUID.fromString("8d175726-cf6b-4d78-962b-2860cfb6af43"));
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar excluir um item que está em um pedido")
    void testDeleteItemException(){
        when(repository.findById(any())).thenReturn(Optional.of(item));
        when(itemPedidoService.isItemUsed(any())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.delete(UUID.fromString("8d175726-cf6b-4d78-962b-2860cfb6af43")));
    }
}