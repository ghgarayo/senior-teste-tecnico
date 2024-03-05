package br.com.senior.seniortestetecnico.services.Impl;

import br.com.senior.seniortestetecnico.entidade.Item;
import br.com.senior.seniortestetecnico.enums.EnumTipoItem;
import br.com.senior.seniortestetecnico.repositories.ItemPedidoRepository;
import br.com.senior.seniortestetecnico.repositories.ItemRepository;
import br.com.senior.seniortestetecnico.services.ItemPedidoService;
import br.com.senior.seniortestetecnico.services.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
public class ItemServiceImpl implements ItemService {

    @Inject
    private ItemRepository repository;

    @Inject
    private ItemPedidoService itemPedidoService;

    @Override
    public Item create(String nome, String descricao, EnumTipoItem tipo, Double precoUnitario) {
        return repository.save(new Item(nome, descricao, tipo, precoUnitario));
    }

    @Override
    public Item update(UUID id, String nome, String descricao, Double precoUnitario) {
        var item = getById(id);
        item.update(nome, descricao, precoUnitario);

        return repository.save(item);
    }


    @Override
    public Page<Item> getAllItems(Pageable pageable) {
        return repository.findAllByAtivoTrue(pageable);
    }

    @Override
    public Item getById(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Item não encontrado"));
    }
    @Override
    public void inactivate(UUID id) {
        var item = getById(id);
        item.inativar();
        repository.save(item);
    }

    @Override
    public void delete(UUID id) {
        var item = getById(id);
        var isUsed = itemPedidoService.isItemUsed(id);

        if(isUsed) {
            throw new IllegalArgumentException("Produto faz parte de um pedido e não pode ser excluído.");
        }

        repository.deleteById(id);
    }
}
