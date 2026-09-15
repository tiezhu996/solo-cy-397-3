package com.contractapi.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import com.contractapi.dto.TemplateFavoriteView;
import com.contractapi.dto.TemplateView;
import com.contractapi.entity.ContractTemplate;
import com.contractapi.service.TemplateFavoriteService;
import com.contractapi.service.TemplateService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {
  private final TemplateService service;
  private final TemplateFavoriteService favoriteService;
  public TemplateController(TemplateService service, TemplateFavoriteService favoriteService) {
    this.service = service;
    this.favoriteService = favoriteService;
  }
  @GetMapping public List<TemplateView> list(@RequestParam(required = false) Long userId) {
    Set<Long> favorited = userId == null ? Set.of() : favoriteService.favoritedTemplateIds(userId);
    return service.list().stream()
        .map(template -> TemplateView.of(template, userId == null ? null : favorited.contains(template.getId())))
        .toList();
  }
  @PostMapping public ContractTemplate create(@RequestBody ContractTemplate template) { return service.create(template); }
  @PostMapping("/{id}/favorite") public TemplateView favorite(@PathVariable Long id, @RequestParam Long userId) {
    favoriteService.favorite(userId, id);
    return TemplateView.of(service.findById(id), true);
  }
  @DeleteMapping("/{id}/favorite") public Map<String, Object> unfavorite(@PathVariable Long id, @RequestParam Long userId) {
    favoriteService.unfavorite(userId, id);
    return Map.of("templateId", id, "favorited", false);
  }
  @GetMapping("/favorites") public List<TemplateFavoriteView> favorites(@RequestParam Long userId) {
    return favoriteService.list(userId).stream()
        .map(favorite -> TemplateFavoriteView.of(favorite, service.findById(favorite.getTemplateId())))
        .toList();
  }
}
