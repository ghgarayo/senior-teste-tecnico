package br.com.senior.seniortestetecnico.controller;

import br.com.senior.seniortestetecnico.dto.ItemDTOInput;
import br.com.senior.seniortestetecnico.dto.ItemDTOOutput;
import br.com.senior.seniortestetecnico.entidade.Item;
import br.com.senior.seniortestetecnico.services.ItemService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Inject
    private ItemService service;

    @PostMapping
    @Transactional
    public ResponseEntity<ItemDTOOutput> create(@RequestBody @Valid ItemDTOInput dto, UriComponentsBuilder uriBuilder){
        var item = service.create(dto.nome(), dto.descricao(), dto.tipo() ,dto.precoUnitario());
        var uri = uriBuilder.path("/produto/{id}").buildAndExpand(item.getId()).toUri();

        return ResponseEntity.created(uri).body(new ItemDTOOutput(item));
    }

    @GetMapping
    public ResponseEntity<List<ItemDTOOutput>> readAll(@PageableDefault(size = 10, sort = "nome") Pageable pageable){
        Page<Item> itens = service.getAllItems(pageable);

        return ResponseEntity.ok().body(itens.stream().map(ItemDTOOutput::new).collect(Collectors.toList()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ItemDTOOutput> readById(@PathVariable String id){
        var item = service.getById(UUID.fromString(id));

        return ResponseEntity.ok().body(new ItemDTOOutput(item));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ItemDTOOutput> update(@PathVariable String id, @RequestBody @Valid ItemDTOInput dto){
        var item = service.update(UUID.fromString(id), dto.nome(), dto.descricao(), dto.precoUnitario());

        return ResponseEntity.ok().body(new ItemDTOOutput(item));
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<?> inactivate(@PathVariable String id ){
        service.inactivate(UUID.fromString(id));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable String id){
        service.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

}
