package br.com.senior.seniortestetecnico.services.Impl;

import br.com.senior.seniortestetecnico.dto.ItemPedidoDTOOutput;
import br.com.senior.seniortestetecnico.dto.PedidoDTOCompletoOutput;
import br.com.senior.seniortestetecnico.dto.PedidoDTOInput;
import br.com.senior.seniortestetecnico.entidade.ItemPedido;
import br.com.senior.seniortestetecnico.entidade.Pedido;
import br.com.senior.seniortestetecnico.enums.EnumSituacaoPedido;
import br.com.senior.seniortestetecnico.enums.EnumTipoItem;
import br.com.senior.seniortestetecnico.repositories.PedidoRepository;
import br.com.senior.seniortestetecnico.services.ItemPedidoService;
import br.com.senior.seniortestetecnico.services.PedidoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Inject
    private PedidoRepository repository;

    @Inject
    private ItemPedidoService itemPedidoService;


    @Override
    public Pedido create(PedidoDTOInput dto) {
        var pedido = repository.save(new Pedido());

        List<ItemPedido> itensPedido = criarItemPedido(dto, pedido);
        pedido.setValorDesconto(0.00);

        setValoresPedido(itensPedido, pedido);

        return repository.save(pedido);
    }

    @Override
    public Page<Pedido> getAllPedidos(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Pedido readById(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Pedido não encontrado")
        );
    }

    @Override
    public PedidoDTOCompletoOutput readByIdCompleto(UUID id) {
        var pedido = readById(id);
        var itensPedido = recuperaItensPedido(id);
        var itensPedidoDTO = itensPedido.stream().map(ItemPedidoDTOOutput::new).toList();

        return new PedidoDTOCompletoOutput(pedido, itensPedidoDTO);
    }

    @Override
    public PedidoDTOCompletoOutput addItem(UUID id, PedidoDTOInput dto) {
        var pedido = validaPedidoAberto(id);
        var newItensPedido = dto.itensPedido().stream()
                .map(itemPedidoDTOInput -> itemPedidoService.create(pedido, itemPedidoDTOInput))
                .toList();
        setValoresPedido(newItensPedido, pedido);
        repository.save(pedido);

        var itensPedido = recuperaItensPedido(pedido.getId());

        return new PedidoDTOCompletoOutput(pedido, itensPedido.stream().map(ItemPedidoDTOOutput::new).toList());
    }

    public PedidoDTOCompletoOutput applyDiscount(UUID id, Integer percentualDesconto) {
        var pedido = validaPedidoAberto(id);
        var itensPedido = recuperaItensPedido(pedido.getId());

        generateDiscount(percentualDesconto, itensPedido);

        pedido.setValorBruto(itensPedido.stream().mapToDouble(ItemPedido::getValorBruto).sum());
        pedido.setValorTotal(itensPedido.stream().mapToDouble(ItemPedido::getValorLiquido).sum());
        pedido.setValorDesconto(itensPedido.stream().mapToDouble(ItemPedido::getValorDesconto).sum());

        repository.save(pedido);

        return new PedidoDTOCompletoOutput(pedido, itensPedido.stream().map(ItemPedidoDTOOutput::new).toList());
    }

    @Override
    public void inactivate(UUID id) {
        var pedido = validaPedidoAberto(id);

        if (!pedido.isAtivo()) {
            throw new IllegalArgumentException("Pedido já está inativo");
        }

        pedido.setAtivo(false);
        repository.save(pedido);
    }

    @Override
    public void fecharPedido(UUID id) {
        var pedido = validaPedidoAberto(id);
        pedido.setSituacao(EnumSituacaoPedido.FECHADO);
        pedido.setDataFechamento(LocalDateTime.now());
        repository.save(pedido);
    }

    private List<ItemPedido> criarItemPedido(PedidoDTOInput dto, Pedido pedido) {
        return dto.itensPedido().stream()
                .map(itemPedidoDTOInput -> itemPedidoService.create(pedido, itemPedidoDTOInput))
                .toList();
    }

    private List<ItemPedido> recuperaItensPedido(UUID id) {
        return itemPedidoService.getAllItemPedidos(id, null).toList();
    }

    private void generateDiscount(Integer percentualDesconto, List<ItemPedido> itemPedidos) {
        itemPedidos.forEach(itemPedido -> {
            if (itemPedido.getTipoItem() != EnumTipoItem.SERVICO) {
                itemPedido.setValorLiquido(itemPedidoService.applyDiscount(itemPedido, percentualDesconto));
            }
        });
    }

    private Pedido validaPedidoAberto(UUID id) {
        var pedido = readById(id);
        if (pedido.getSituacao() != EnumSituacaoPedido.ABERTO) {
            throw new IllegalArgumentException("Pedido não está aberto");
        }

        return pedido;
    }

    private void setValoresPedido(List<ItemPedido> itensPedido, Pedido pedido) {
        double valorBruto = itensPedido.stream().mapToDouble(ItemPedido::getValorBruto).sum() + (pedido.getValorBruto() != null ? pedido.getValorBruto() : 0.00);
        double valorTotal = itensPedido.stream().mapToDouble(ItemPedido::getValorLiquido).sum() + (pedido.getValorTotal() != null ? pedido.getValorTotal() : 0.00);
        double valorDesconto = itensPedido.stream().mapToDouble(
                itemPedido -> {
                    if (itemPedido.getTipoItem() != EnumTipoItem.SERVICO) {
                        return itemPedido.getValorDesconto() +
                                (pedido.getValorDesconto() != null ? pedido.getValorDesconto() : 0.00);
                    }
                    return 0.00;
                }
        ).sum();

        pedido.setValorDesconto(valorDesconto);
        pedido.setValorBruto(valorBruto);
        pedido.setValorTotal(valorTotal);


    }

}
