package org.acme.services.MetroService;

import org.acme.entities.Metro.Viagem;

public class ViagemService {
    public boolean validateViagem(Viagem viagem) {
        if (viagem == null)
            return false;
        if (viagem.getPassageiro() == null)
            return false;

        return true;
    }
}
