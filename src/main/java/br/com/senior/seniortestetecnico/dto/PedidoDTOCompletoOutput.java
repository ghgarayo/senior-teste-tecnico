package br.com.senior.seniortestetecnico.dto;

import br.com.senior.seniortestetecnico.entidade.Pedido;
import br.com.senior.seniortestetecnico.enums.EnumSituacaoPedido;

import java.util.List;

public record PedidoDTOCompletoOutput(
        String id,
        EnumSituacaoPedido situacao,
        Double valorBruto,
        Double valorDesconto,
        Double valorTotal,
        List<ItemPedidoDTOOutput> itensPedido
) {

    public PedidoDTOCompletoOutput(Pedido pedido, List<ItemPedidoDTOOutput> itensPedidoDTOOutput){
        this(pedido.getId().toString(),
                pedido.getSituacao(),
                pedido.getValorBruto(),
                pedido.getValorDesconto(),
                pedido.getValorTotal(),
                itensPedidoDTOOutput);
    }
}

