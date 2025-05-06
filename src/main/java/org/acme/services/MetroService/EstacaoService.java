package org.acme.services.MetroService;

import org.acme.entities.Metro.Estacao;

public class EstacaoService {
    public boolean validateEstacao(Estacao estacao) {
        if (estacao == null)
            return false;
        if (estacao.getNome() == null || estacao.getNome().isEmpty())
            return false;

        return true;
    }
}
