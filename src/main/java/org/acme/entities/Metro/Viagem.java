package org.acme.entities.Metro;

import org.acme.entities.Pessoa.Passageiro;
import org.acme.entities.Pessoa._BaseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Viagem extends _BaseEntity {
    private int duracao;
    private Passageiro passageiro;
    private Estacao estacaoOrigem;
    private Estacao estacaoDestino;

    public Viagem() {
    }

    public Viagem(int id, boolean deleted, LocalDateTime dataCriacao, int duracao, Passageiro passageiro, Estacao estacaoOrigem, Estacao estacaoDestino) {
        super(id, deleted, dataCriacao);
        this.duracao = duracao;
        this.passageiro = passageiro;
        this.estacaoOrigem = estacaoOrigem;
        this.estacaoDestino = estacaoDestino;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public Passageiro getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(Passageiro passageiro) {
        this.passageiro = passageiro;
    }

    public Estacao getEstacaoOrigem() {
        return estacaoOrigem;
    }

    public void setEstacaoOrigem(Estacao estacaoOrigem) {
        this.estacaoOrigem = estacaoOrigem;
    }

    public Estacao getEstacaoDestino() {
        return estacaoDestino;
    }

    public void setEstacaoDestino(Estacao estacaoDestino) {
        this.estacaoDestino = estacaoDestino;
    }

    @Override
    public String toString() {
        return "Viagem{" +
                "duracao=" + duracao +
                ", passageiro=" + passageiro +
                ", estacaoOrigem=" + estacaoOrigem +
                ", estacaoDestino=" + estacaoDestino +
                "} " + super.toString();
    }
}
