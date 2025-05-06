package org.acme.dtos.Metro;

import org.acme.entities.Metro.Viagem;

import java.util.List;

public record SearchViagemDto(int page, String direction, int pageSize, int totalItems, List<Viagem> data) {
}
