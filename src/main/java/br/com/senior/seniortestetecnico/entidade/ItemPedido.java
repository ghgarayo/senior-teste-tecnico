package br.com.senior.seniortestetecnico.entidade;

import br.com.senior.seniortestetecnico.enums.EnumTipoItem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity(name="ItemPedido")
@Table(name="item_pedido")
@AllArgsConstructor
public class ItemPedido {

    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @Enumerated(EnumType.STRING)
    private EnumTipoItem tipoItem;
    private Integer quantidade;
    @Column(name = "preco_unitario")
    private Double precoUnitario;
    @Column(name = "valor_bruto")
    private Double valorBruto;
    @Column(name = "valor_desconto")
    private Double valorDesconto;
    @Column(name = "valor_liquido")
    private Double valorLiquido;
    private Boolean ativo;

    public ItemPedido() {
        this.id = UUID.randomUUID();
    }


}
