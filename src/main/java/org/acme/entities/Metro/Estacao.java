package org.acme.entities.Metro;
import org.acme.entities.Pessoa._BaseEntity;

import java.time.LocalDateTime;

public class Estacao extends _BaseEntity {
    private String nome;
    private String horarioAbertura;
    private String horarioFechamento;
    private String localizacao;

    public Estacao() {
    }

    public Estacao(int id, boolean deleted, LocalDateTime dataCriacao, String nome, String horarioAbertura, String horarioFechamento, String localizacao) {
        super(id, deleted, dataCriacao);
        this.nome = nome;
        this.horarioAbertura = horarioAbertura;
        this.horarioFechamento = horarioFechamento;
        this.localizacao = localizacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHorarioAbertura() {
        return horarioAbertura;
    }

    public void setHorarioAbertura(String horarioAbertura) {
        this.horarioAbertura = horarioAbertura;
    }

    public String getHorarioFechamento() {
        return horarioFechamento;
    }

    public void setHorarioFechamento(String horarioFechamento) {
        this.horarioFechamento = horarioFechamento;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    @Override
    public String toString() {
        return "Estacao{" +
                "nome='" + nome + '\'' +
                ", horarioAbertura='" + horarioAbertura + '\'' +
                ", horarioFechamento='" + horarioFechamento + '\'' +
                ", localizacao='" + localizacao + '\'' +
                "} " + super.toString();
    }
}
