package org.acme.services.PessoaService;

import org.acme.entities.Pessoa.Passageiro;

public class PassageiroService {
    public boolean validatePassageiro(Passageiro passageiro) {
        if(passageiro == null)
            return false;
        if(passageiro.getNome() == null || passageiro.getNome().isEmpty())
            return false;

        return true;
    }
}
