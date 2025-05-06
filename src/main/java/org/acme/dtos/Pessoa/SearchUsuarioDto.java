package org.acme.dtos.Pessoa;

import org.acme.entities.Pessoa.Usuario;

import java.util.List;

public record SearchUsuarioDto(int page, String direction, int pageSize, int totalItems, List<Usuario> data) {
}
