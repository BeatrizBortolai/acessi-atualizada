package org.acme.services.PessoaService;

import org.acme.entities.Pessoa.Usuario;

public class UsuarioService {
    public boolean validateUsuario(Usuario usuario) {
        if (usuario == null)
            return false;
        if (usuario.getNome() == null || usuario.getNome().isEmpty())
            return false;

        return true;
    }
}
