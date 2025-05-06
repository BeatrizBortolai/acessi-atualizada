package org.acme.entities.Metro;

import org.acme.entities.Pessoa._BaseEntity;

import java.time.LocalDateTime;
import java.util.List;

public class Linha extends _BaseEntity {
    private String nome;
    private List<Estacao> estacoes;

    public Linha() {
    }

    public Linha(int id, boolean deleted, LocalDateTime dataCriacao, String nome, List<Estacao> estacoes) {
        super(id, deleted, dataCriacao);
        this.nome = nome;
        this.estacoes = estacoes;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Estacao> getEstacoes() {
        return estacoes;
    }

    public void setEstacoes(List<Estacao> estacoes) {
        this.estacoes = estacoes;
    }

    @Override
    public String toString() {
        return "Linha{" +
                "nome='" + nome + '\'' +
                ", estacoes=" + estacoes +
                "} " + super.toString();
    }
}
