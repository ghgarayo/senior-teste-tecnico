package br.com.senior.seniortestetecnico.repositories;

import br.com.senior.seniortestetecnico.dto.PedidoDTOCompletoOutput;
import br.com.senior.seniortestetecnico.entidade.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

}
