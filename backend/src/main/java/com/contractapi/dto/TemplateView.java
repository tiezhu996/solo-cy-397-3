package com.contractapi.dto;

import com.contractapi.entity.ContractTemplate;

public record TemplateView(Long id, String type, String title, String content, String variables, Boolean favorited) {
  public static TemplateView of(ContractTemplate template, Boolean favorited) {
    return new TemplateView(template.getId(), template.getType(), template.getTitle(), template.getContent(), template.getVariables(), favorited);
  }
}
