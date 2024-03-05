package br.com.senior.seniortestetecnico.services.Impl;


import br.com.senior.seniortestetecnico.dto.ItemPedidoDTOInput;
import br.com.senior.seniortestetecnico.entidade.Item;
import br.com.senior.seniortestetecnico.entidade.ItemPedido;
import br.com.senior.seniortestetecnico.entidade.Pedido;

import br.com.senior.seniortestetecnico.repositories.ItemPedidoRepository;
import br.com.senior.seniortestetecnico.services.ItemPedidoService;
import br.com.senior.seniortestetecnico.services.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.UUID;

@Service
public class ItemPedidoServiceImpl implements ItemPedidoService {
    @Inject
    private ItemPedidoRepository itemPedidoRepository;
    @Inject
    private ItemService itemService;

    @Override
    public ItemPedido create(Pedido pedido, ItemPedidoDTOInput dto) {

        if (Objects.isNull(dto.quantidade()) || dto.quantidade() <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        Item item = itemService.getById(UUID.fromString(dto.id()));

        if(!item.getAtivo()) {
            throw new IllegalArgumentException("Produto inativo");
        }

        var itemPedido = setItemPedido(pedido, dto, item);

        return itemPedidoRepository.save(itemPedido);
    }

    private ItemPedido setItemPedido(Pedido pedido, ItemPedidoDTOInput dto, Item item) {
        var itemPedido = new ItemPedido();
        itemPedido.setPedido(pedido);
        itemPedido.setQuantidade(dto.quantidade());
        itemPedido.setAtivo(true);
        itemPedido.setItem(item);
        itemPedido.setTipoItem(item.getTipoItem());
        itemPedido.setPrecoUnitario(item.getPrecoUnitario());
        itemPedido.setValorDesconto(0.0);
        itemPedido.setValorBruto(calculaValorBruto(dto.quantidade(), itemPedido.getPrecoUnitario()));
        itemPedido.setValorLiquido(calculaValorLiquido(itemPedido.getValorBruto(), 0.0));

        return itemPedido;
    }

    @Override
    public Page<ItemPedido> getAllItemPedidos(UUID pedidoId, Pageable pageable) {
        return itemPedidoRepository.findAllByPedidoId(pedidoId, pageable);
    }

    @Override
    public ItemPedido readById(UUID id) {
        return itemPedidoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Item não encontrado"));
    }

    @Override
    public void update(UUID id, Integer quantidade) {
        var itemPedido = readById(id);
        itemPedido.setQuantidade(quantidade);
    }

    @Override
    public void delete(UUID id) {
        var itemPedido = readById(id);
        itemPedidoRepository.delete(itemPedido);
    }

    @Override
    public Boolean isItemUsed(UUID id) {
        return itemPedidoRepository.existsByItemId(id);
    }

    @Override
    public Double applyDiscount(ItemPedido itemPedido, Integer percentualDesconto) {
        readById(itemPedido.getId());
        validateDiscount(percentualDesconto);

        itemPedido.setValorDesconto(calculaValorDesconto(itemPedido.getValorBruto(), percentualDesconto));
        itemPedido.setValorLiquido(calculaValorLiquido(itemPedido.getValorBruto(), itemPedido.getValorDesconto()));

        return itemPedido.getValorLiquido();

    }

    private Double calculaValorBruto(Integer quantidade, Double precoUnitario) {
        return quantidade * precoUnitario;
    }

    private Double calculaValorDesconto(Double valorBruto, Integer percentualDesconto) {
        return valorBruto * percentualDesconto / 100;
    }

    private Double calculaValorLiquido(Double valorBruto, Double valorDesconto) {
        return valorBruto - valorDesconto;
    }

    private static void validateDiscount(Integer percentualDesconto) {
        if(percentualDesconto < 0 || percentualDesconto > 100) {
            throw new IllegalArgumentException("Percentual de desconto inválido");
        }
    }
}
