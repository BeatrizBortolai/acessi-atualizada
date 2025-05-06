package org.acme.dtos.Pessoa;

import org.acme.entities.Pessoa.Passageiro;

import java.util.List;

public record SearchPassageiroDto(int page, String direction, int pageSize, int totalItems, List<Passageiro> data) {
}
