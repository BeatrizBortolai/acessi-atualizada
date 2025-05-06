package org.acme.entities.Metro;

import org.acme.entities.Pessoa._BaseEntity;

import java.time.LocalDateTime;


public class Trem extends _BaseEntity {
    private String origem;
    private String destino;
    private Linha linha;
    private int capacidadeMaxima;
    private int capacidadeAtual;
    private boolean operando;

    public Trem() {
    }

    public Trem(int id, boolean deleted, LocalDateTime dataCriacao, String origem, String destino, Linha linha, int capacidadeMaxima, int capacidadeAtual, boolean operando) {
        super(id, deleted, dataCriacao);
        this.origem = origem;
        this.destino = destino;
        this.linha = linha;
        this.capacidadeMaxima = capacidadeMaxima;
        this.capacidadeAtual = capacidadeAtual;
        this.operando = operando;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Linha getLinha() {
        return linha;
    }

    public void setLinha(Linha linha) {
        this.linha = linha;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public void setCapacidadeMaxima(int capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public int getCapacidadeAtual() {
        return capacidadeAtual;
    }

    public void setCapacidadeAtual(int capacidadeAtual) {
        this.capacidadeAtual = capacidadeAtual;
    }

    public boolean isOperando() {
        return operando;
    }

    public void setOperando(boolean operando) {
        this.operando = operando;
    }

    @Override
    public String toString() {
        return "Trem{" +
                "origem='" + origem + '\'' +
                ", destino='" + destino + '\'' +
                ", linha=" + linha +
                ", capacidadeMaxima=" + capacidadeMaxima +
                ", capacidadeAtual=" + capacidadeAtual +
                ", operando=" + operando +
                "} " + super.toString();
    }
}
