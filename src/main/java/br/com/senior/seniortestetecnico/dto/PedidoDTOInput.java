package br.com.senior.seniortestetecnico.dto;

import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

public record PedidoDTOInput(
        @Valid
        List<ItemPedidoDTOInput> itensPedido
) {
}
