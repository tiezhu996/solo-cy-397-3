package com.contractapi.dto;

import java.time.LocalDateTime;
import com.contractapi.entity.ContractTemplate;
import com.contractapi.entity.TemplateFavorite;

public record TemplateFavoriteView(Long templateId, String type, String title, String content, String variables, LocalDateTime favoritedAt) {
  public static TemplateFavoriteView of(TemplateFavorite favorite, ContractTemplate template) {
    return new TemplateFavoriteView(
        favorite.getTemplateId(),
        template == null ? null : template.getType(),
        template == null ? null : template.getTitle(),
        template == null ? null : template.getContent(),
        template == null ? null : template.getVariables(),
        favorite.getCreatedAt());
  }
}
