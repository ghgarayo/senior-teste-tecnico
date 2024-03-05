package br.com.senior.seniortestetecnico.dto;

import br.com.senior.seniortestetecnico.entidade.Pedido;
import br.com.senior.seniortestetecnico.enums.EnumSituacaoPedido;

public record PedidoDTOOutput(
        String id,
        EnumSituacaoPedido situacao,
        Double valorBruto,
        Double valorDesconto,
        Double valorTotal
) {
    public PedidoDTOOutput(Pedido pedido){
        this(pedido.getId().toString(),
                pedido.getSituacao(),
                pedido.getValorBruto(),
                pedido.getValorDesconto(),
                pedido.getValorTotal());
    }
}