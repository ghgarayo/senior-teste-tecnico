package br.com.senior.seniortestetecnico.services;

import br.com.senior.seniortestetecnico.dto.PedidoDTOCompletoOutput;
import br.com.senior.seniortestetecnico.dto.PedidoDTOInput;
import br.com.senior.seniortestetecnico.entidade.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PedidoService {

    Pedido create(PedidoDTOInput dto);

    Page<Pedido> getAllPedidos(Pageable pageable);

    Pedido readById(UUID id);

    PedidoDTOCompletoOutput readByIdCompleto(UUID id);

    PedidoDTOCompletoOutput addItem(UUID id, PedidoDTOInput dto);

    void inactivate(UUID id);
    void fecharPedido(UUID id);


}
