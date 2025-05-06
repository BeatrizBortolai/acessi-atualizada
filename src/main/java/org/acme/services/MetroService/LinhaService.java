package org.acme.services.MetroService;

import org.acme.entities.Metro.Linha;

public class LinhaService {
    public boolean validateLinha(Linha linha) {
        if (linha == null)
            return false;
        if (linha.getNome() == null || linha.getNome().isEmpty())
            return false;

        return true;
    }
}
