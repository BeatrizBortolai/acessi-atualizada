package org.acme.dtos.Metro;

import org.acme.entities.Metro.Trem;

import java.util.List;

public record SearchTremDto(int page, String direction, int pageSize, int totalItems, List<Trem> data) {
}
