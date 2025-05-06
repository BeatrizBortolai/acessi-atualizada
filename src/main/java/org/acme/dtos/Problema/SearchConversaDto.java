package org.acme.dtos.Problema;

import org.acme.entities.Problema.Conversa;

import java.util.List;

public record SearchConversaDto(int page, String direction, int pageSize, int totalItems, List<Conversa> data) {
}
