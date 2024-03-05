package br.com.senior.seniortestetecnico.services;

import br.com.senior.seniortestetecnico.dto.ItemPedidoDTOInput;
import br.com.senior.seniortestetecnico.entidade.ItemPedido;
import br.com.senior.seniortestetecnico.entidade.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ItemPedidoService {

    ItemPedido create(Pedido pedido, ItemPedidoDTOInput dto);

    Page<ItemPedido> getAllItemPedidos(UUID pedidoId, Pageable pageable);

    Double applyDiscount(ItemPedido itemPedido, Integer percentualDesconto);

    ItemPedido readById(UUID id);
    void update(UUID id, Integer quantidade);
    void delete(UUID id);
    Boolean isItemUsed(UUID id);
}
