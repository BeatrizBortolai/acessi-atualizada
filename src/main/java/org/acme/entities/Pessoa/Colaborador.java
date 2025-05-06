package org.acme.entities.Pessoa;

import java.time.LocalDateTime;

public class Colaborador extends Usuario{
    private String cargo;

    public Colaborador() {
    }

    public Colaborador(int id, boolean deleted, LocalDateTime dataCriacao, String nome, String email, String senha, String cargo) {
        super(id, deleted, dataCriacao, nome, email, senha);
        this.cargo = cargo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public String toString() {
        return "Colaborador{" +
                "cargo='" + cargo + '\'' +
                "} " + super.toString();
    }
}
