package org.acme.dtos.Problema;

import org.acme.entities.Problema.Notificacao;

import java.util.List;

public record SearchNotificacaoDto(int page, String direction, int pageSize, int totalItems, List<Notificacao> data) {
}
