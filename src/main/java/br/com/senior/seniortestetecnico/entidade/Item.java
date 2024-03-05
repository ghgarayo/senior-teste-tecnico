package br.com.senior.seniortestetecnico.entidade;

import br.com.senior.seniortestetecnico.enums.EnumTipoItem;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Data
@Entity(name = "Item")
@Table(name = "item")
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    private UUID id;
    private String nome;
    private String descricao;
    @Enumerated(EnumType.STRING)
    private EnumTipoItem tipoItem;
    private Double precoUnitario;
    private Boolean ativo;

    public Item(String nome, String descricao, EnumTipoItem tipo, Double preco) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.descricao = descricao;
        this.tipoItem = tipo;
        this.precoUnitario = preco;
        this.ativo = true;
    }

    public void update(String nome, String descricao, Double preco){
        if(!Objects.equals(nome, this.nome)) {
            this.nome = nome;
        }
        if(!Objects.equals(descricao, this.descricao)) {
            this.descricao = descricao;
        }
        if(!Objects.equals(preco, this.precoUnitario)){
            this.precoUnitario = preco;
        }
    }

    public void inativar() {
        setAtivo(false);
    }

    public void reativar() {
        setAtivo(true);
    }
}
