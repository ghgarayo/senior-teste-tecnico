package br.com.senior.seniortestetecnico.dto;

import br.com.senior.seniortestetecnico.enums.EnumTipoItem;
import jakarta.validation.constraints.NotNull;


public record ItemPedidoDTOInput(
        String id,
        @NotNull
        Integer quantidade
) {

}