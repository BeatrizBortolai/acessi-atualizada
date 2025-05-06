package org.acme.entities.Pessoa;

import java.time.LocalDateTime;

public class Usuario extends _BaseEntity {
    private String nome;
    private String email;
    private String senha;

    public Usuario() {
    }

    public Usuario(int id, boolean deleted, LocalDateTime dataCriacao, String nome, String email, String senha) {
        super(id, deleted, dataCriacao);
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


    @Override
    public String toString() {
        return "\nUsuario " +
                "Nome: " + nome + '\'' +
                ", E-mail: " + email + '\'' +
                ", Senha: " + senha + '\'' +
                " " + super.toString();
    }
}
