package org.acme.dtos.Pessoa;

import org.acme.entities.Pessoa.Colaborador;

import java.util.List;

public record SearchColaboradorDto(int page, String direction, int pageSize, int totalItems, List<Colaborador> data) {
}
