package br.com.senior.seniortestetecnico.dto;

import br.com.senior.seniortestetecnico.entidade.ItemPedido;
import br.com.senior.seniortestetecnico.enums.EnumTipoItem;

public record ItemPedidoDTOOutput(
        String id,
        String pedidoId,
        EnumTipoItem tipoItem,
        Integer quantidade,
        Double precoUnitario,
        Double valorBruto,
        Double valorDesconto,
        Double valorLiquido
) {

    public ItemPedidoDTOOutput(ItemPedido itemPedido){
        this(itemPedido.getId().toString(),
                itemPedido.getPedido().getId().toString(),
                itemPedido.getTipoItem(),
                itemPedido.getQuantidade(),
                itemPedido.getPrecoUnitario(),
                itemPedido.getValorBruto(),
                itemPedido.getValorDesconto() != null ? itemPedido.getValorDesconto() : 0.0,
                itemPedido.getValorLiquido());
    }


}
