package org.acme.dtos.Metro;

import org.acme.entities.Metro.Estacao;

import java.util.List;

public record SearchEstacaoDto(int page, String direction, int pageSize, int totalItems, List<Estacao> data) {
}
