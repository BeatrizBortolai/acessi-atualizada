package org.acme.services.ProblemaService;

import org.acme.entities.Problema.Conversa;

public class ConversaService {
    public boolean validateConversa(Conversa conversa) {
        if(conversa == null)
            return false;
        if(conversa.getAssunto() == null || conversa.getAssunto().isEmpty())
            return false;

        return true;
    }
}
