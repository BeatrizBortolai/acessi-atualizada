package org.acme.dtos.Metro;

import org.acme.entities.Metro.Linha;

import java.util.List;

public record SearchLinhaDto(int page, String direction, int pageSize, int totalItems, List<Linha> data) {
}
