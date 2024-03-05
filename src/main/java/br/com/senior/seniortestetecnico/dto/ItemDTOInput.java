package br.com.senior.seniortestetecnico.dto;

import br.com.senior.seniortestetecnico.enums.EnumTipoItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemDTOInput(
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        @NotBlank(message = "Descrição é obrigatória")
        String descricao,
        @NotNull(message = "Tipo é obrigatório")
        EnumTipoItem tipo,
        @NotNull(message = "Preço Unitário é obrigatório")
        Double precoUnitario
) {
}

