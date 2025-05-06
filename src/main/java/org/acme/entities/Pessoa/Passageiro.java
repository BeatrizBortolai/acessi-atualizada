package org.acme.entities.Pessoa;

import java.time.LocalDateTime;

public class Passageiro extends Usuario {
    public Passageiro() {
    }

    public Passageiro(int id, boolean deleted, LocalDateTime dataCriacao, String nome, String email, String senha) {
        super(id, deleted, dataCriacao, nome, email, senha);
    }

    @Override
    public String toString() {
        return "Passageiro{} " + super.toString();
    }
}
