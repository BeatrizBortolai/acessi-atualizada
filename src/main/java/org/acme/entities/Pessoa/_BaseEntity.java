package org.acme.entities.Pessoa;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public abstract class _BaseEntity {
    private int id;
    private boolean deleted;
    private LocalDateTime dataCriacao = LocalDateTime.now();

    public _BaseEntity() {
    }

    public _BaseEntity(int id, boolean deleted, LocalDateTime dataCriacao) {
        this.id = id;
        this.deleted = deleted;
        this.dataCriacao = dataCriacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataHoraFormatada = dataCriacao.format(formatter);
        return "_BaseEntity" +
                " ID: " + id +
                ", Deleted: " + deleted +
                ", Data Criação: " + dataHoraFormatada +
                ' ';
    }
}
