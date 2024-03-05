package br.com.senior.seniortestetecnico.repositories;

import br.com.senior.seniortestetecnico.entidade.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {
    Page<Item> findAllByAtivoTrue(Pageable pageable);
}
