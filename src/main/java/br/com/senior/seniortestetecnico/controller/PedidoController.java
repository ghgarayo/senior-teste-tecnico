package br.com.senior.seniortestetecnico.controller;

import br.com.senior.seniortestetecnico.dto.PedidoDTOCompletoOutput;
import br.com.senior.seniortestetecnico.dto.PedidoDTOInput;
import br.com.senior.seniortestetecnico.dto.PedidoDTOOutput;
import br.com.senior.seniortestetecnico.services.Impl.PedidoServiceImpl;
import br.com.senior.seniortestetecnico.services.PedidoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.util.UUID;


@RestController
@RequestMapping("/api/pedido")
public class PedidoController {

    @Inject
    private PedidoServiceImpl service;

    @PostMapping
    @Transactional
    public ResponseEntity<PedidoDTOOutput> create(@RequestBody @Valid PedidoDTOInput dto, UriComponentsBuilder uriBuilder){
        var pedido = service.create(dto);
        var uri = uriBuilder.path("/api/pedido/{id}").buildAndExpand(pedido.getId()).toUri();

        return ResponseEntity.created(uri).body(new PedidoDTOOutput(pedido));
    }

    @GetMapping
    public ResponseEntity<Page<PedidoDTOOutput>> readAll(Pageable pageable){
        var pedidos = service.getAllPedidos(pageable);

        return ResponseEntity.ok().body(pedidos.map(PedidoDTOOutput::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTOOutput> readById(@PathVariable String id){
        var pedido = service.readById(UUID.fromString(id));

        return ResponseEntity.ok(new PedidoDTOOutput(pedido));
    }

    @GetMapping("/{id}/completo")
    public ResponseEntity<PedidoDTOCompletoOutput> readByIdCompleto(@PathVariable String id){
        PedidoDTOCompletoOutput pedido = service.readByIdCompleto(UUID.fromString(id));

        return ResponseEntity.ok().body(pedido);
    }

    @PutMapping("/{id}/add-item")
    @Transactional
    public ResponseEntity<PedidoDTOCompletoOutput> addItem(@PathVariable String id, @RequestBody PedidoDTOInput dto){
        var pedidoCompleto = service.addItem(UUID.fromString(id), dto);

        return ResponseEntity.ok().body(pedidoCompleto);
    }

    @PutMapping("/{id}/{discount}")
    @Transactional
    public ResponseEntity<PedidoDTOCompletoOutput> applyDiscount(@PathVariable String id, @PathVariable Integer discount){
        var pedidoCompleto = service.applyDiscount(UUID.fromString(id), discount);

        return ResponseEntity.ok().body(pedidoCompleto);
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<?> fecharPedido(@PathVariable String id){
        service.fecharPedido(UUID.fromString(id));

        return ResponseEntity.ok().build();
    }
}
