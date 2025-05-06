package org.acme.dtos.Problema;

public class NotificacaoCriadaDto {
    private String titulo;
    private String conteudo;
    private String linha;
    private String tipoOcorrencia;

    public NotificacaoCriadaDto() {
    }

    public NotificacaoCriadaDto(String titulo, String conteudo, String linha, String tipoOcorrencia) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.linha = linha;
        this.tipoOcorrencia = tipoOcorrencia;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
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
}
