package org.acme.services.ProblemaService;

import org.acme.entities.Problema.Notificacao;

public class NotificacaoService {
    public boolean validateNotificacao(Notificacao notificacao) {
        if(notificacao == null)
            return false;
        if(notificacao.getTitulo() == null || notificacao.getTitulo().isEmpty())
            return false;

        return true;
    }
}
