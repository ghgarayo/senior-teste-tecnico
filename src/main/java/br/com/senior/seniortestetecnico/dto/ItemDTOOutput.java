package br.com.senior.seniortestetecnico.dto;

import br.com.senior.seniortestetecnico.entidade.Item;
import br.com.senior.seniortestetecnico.enums.EnumTipoItem;

public record ItemDTOOutput(String id, String nome, String descricao, EnumTipoItem tipo,Double precoUnitario, Boolean ativo) {

    public ItemDTOOutput(Item item) {
        this(item.getId().toString(),
                item.getNome(),
                item.getDescricao(),
                item.getTipoItem(),
                item.getPrecoUnitario(),
                item.getAtivo());
    }

}
