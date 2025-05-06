package org.acme.services.PessoaService;

import org.acme.entities.Pessoa.Colaborador;

public class ColaboradorService {
    public boolean validateColaborador(Colaborador colaborador) {
        if(colaborador == null)
            return false;
        if(colaborador.getNome() == null || colaborador.getNome().isEmpty())
            return false;

        return true;
    }
}
