package org.acme.entities.Problema;

import org.acme.entities.Pessoa._BaseEntity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Notificacao extends _BaseEntity {
    private String titulo;
    private String conteudo;
    private String linha;
    private String tipoOcorrencia;

    public Notificacao() {
    }

    public Notificacao(String conteudo, String linha, String tipoOcorrencia, String titulo) {
        this.conteudo = conteudo;
        this.linha = linha;
        this.tipoOcorrencia = tipoOcorrencia;
        this.titulo = titulo;
    }

    public Notificacao(int id, boolean deleted, LocalDateTime dataCriacao, String conteudo, String linha, String tipoOcorrencia, String titulo) {
        super(id, deleted, dataCriacao);
        this.conteudo = conteudo;
        this.linha = linha;
        this.tipoOcorrencia = tipoOcorrencia;
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }

    public String getTipoOcorrencia() {
        return tipoOcorrencia;
    }

    public void setTipoOcorrencia(String tipoOcorrencia) {
        this.tipoOcorrencia = tipoOcorrencia;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notificacao that = (Notificacao) o;
        return Objects.equals(getConteudo(), that.getConteudo()) && Objects.equals(getLinha(), that.getLinha()) && Objects.equals(getTipoOcorrencia(), that.getTipoOcorrencia()) && Objects.equals(getTitulo(), that.getTitulo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getConteudo(), getLinha(), getTipoOcorrencia(), getTitulo());
    }

    @Override
    public String toString() {
        return "Notificacao{" +
                "conteudo='" + conteudo + '\'' +
                ", linha='" + linha + '\'' +
                ", tipoOcorrencia='" + tipoOcorrencia + '\'' +
                ", titulo='" + titulo + '\'' +
                "} " + super.toString();
    }
}
