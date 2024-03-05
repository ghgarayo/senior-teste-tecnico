package br.com.senior.seniortestetecnico.services;

import br.com.senior.seniortestetecnico.entidade.Item;
import br.com.senior.seniortestetecnico.enums.EnumTipoItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ItemService {

    Item create(String nome, String descricao, EnumTipoItem tipo, Double precoUnitario);
    Item update(UUID id, String nome, String descricao, Double precoUnitario);
    void inactivate(UUID id);
    Page<Item> getAllItems(Pageable pageable);
    Item getById(UUID id);
    void delete(UUID id);
}
