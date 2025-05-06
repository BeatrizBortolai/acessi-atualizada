package org.acme.entities.Problema;

import org.acme.entities.Pessoa.Usuario;
import org.acme.entities.Pessoa._BaseEntity;

import java.time.LocalDateTime;


public class Conversa extends _BaseEntity {
    private Usuario usuario;
    private String assunto;
    private String modeloLLM;
    private String mensagem;
    private boolean encerrada;

    public Conversa() {
    }

    public Conversa(int id, boolean deleted, LocalDateTime dataCriacao, Usuario usuario, String assunto, String modeloLLM, String mensagem, boolean encerrada) {
        super(id, deleted, dataCriacao);
        this.usuario = usuario;
        this.assunto = assunto;
        this.modeloLLM = modeloLLM;
        this.mensagem = mensagem;
        this.encerrada = encerrada;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getModeloLLM() {
        return modeloLLM;
    }

    public void setModeloLLM(String modeloLLM) {
        this.modeloLLM = modeloLLM;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public boolean isEncerrada() {
        return encerrada;
    }

    public void setEncerrada(boolean encerrada) {
        this.encerrada = encerrada;
    }

    @Override
    public String toString() {
        return "Conversa{" +
                "usuario=" + usuario +
                ", assunto='" + assunto + '\'' +
                ", modeloLLM='" + modeloLLM + '\'' +
                ", mensagem='" + mensagem + '\'' +
                ", encerrada=" + encerrada +
                "} " + super.toString();
    }
}
