package br.com.senior.seniortestetecnico.repositories;

import br.com.senior.seniortestetecnico.entidade.ItemPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, UUID> {
    Page<ItemPedido> findAllByPedidoId(UUID pedidoId, Pageable pageable);
    Boolean existsByItemId(UUID id);

}
