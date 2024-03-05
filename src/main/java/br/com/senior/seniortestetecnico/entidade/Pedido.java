package br.com.senior.seniortestetecnico.entidade;

import br.com.senior.seniortestetecnico.enums.EnumSituacaoPedido;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity(name="Pedido")
@Table(name="pedido")
@AllArgsConstructor
public class Pedido {

    @Id
    private UUID id;
    @Enumerated(EnumType.STRING)
    private EnumSituacaoPedido situacao;
    @Column(name = "data_emissao")
    private LocalDateTime dataEmissao;
    @Column(name="data_fechamento")
    private LocalDateTime dataFechamento;
    @Column(name = "valor_bruto")
    private Double valorBruto;
    @Column(name = "valor_desconto")
    private Double valorDesconto;
    @Column(name = "valor_liquido")
    private Double valorTotal;
    private boolean ativo;

    public Pedido() {
        this.id = UUID.randomUUID();
        this.dataEmissao = LocalDateTime.now();
        this.situacao = EnumSituacaoPedido.ABERTO;
        this.ativo = true;
    }



}
