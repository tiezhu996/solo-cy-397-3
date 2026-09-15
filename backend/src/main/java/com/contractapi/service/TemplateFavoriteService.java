package com.contractapi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.contractapi.constants.ErrorCode;
import com.contractapi.entity.TemplateFavorite;
import com.contractapi.exception.ApiException;
import com.contractapi.mapper.TemplateFavoriteMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class TemplateFavoriteService {
  private final TemplateFavoriteMapper mapper;
  private final TemplateService templateService;

  public TemplateFavoriteService(TemplateFavoriteMapper mapper, TemplateService templateService) {
    this.mapper = mapper;
    this.templateService = templateService;
  }

  public TemplateFavorite favorite(Long userId, Long templateId) {
    if (!templateService.exists(templateId)) {
      throw new ApiException(ErrorCode.NOT_FOUND, "模板不存在: " + templateId);
    }
    TemplateFavorite existing = findOne(userId, templateId);
    if (existing != null) return existing;
    TemplateFavorite favorite = new TemplateFavorite();
    favorite.setUserId(userId);
    favorite.setTemplateId(templateId);
    favorite.setCreatedAt(LocalDateTime.now());
    try {
      mapper.insert(favorite);
      return favorite;
    } catch (DuplicateKeyException e) {
      return findOne(userId, templateId);
    }
  }

  public void unfavorite(Long userId, Long templateId) {
    mapper.delete(new LambdaQueryWrapper<TemplateFavorite>()
        .eq(TemplateFavorite::getUserId, userId)
        .eq(TemplateFavorite::getTemplateId, templateId));
  }

  public List<TemplateFavorite> list(Long userId) {
    return mapper.selectList(new LambdaQueryWrapper<TemplateFavorite>()
        .eq(TemplateFavorite::getUserId, userId)
        .orderByDesc(TemplateFavorite::getCreatedAt)
        .orderByDesc(TemplateFavorite::getId));
  }

  public Set<Long> favoritedTemplateIds(Long userId) {
    return list(userId).stream().map(TemplateFavorite::getTemplateId).collect(Collectors.toSet());
  }

  private TemplateFavorite findOne(Long userId, Long templateId) {
    return mapper.selectOne(new LambdaQueryWrapper<TemplateFavorite>()
        .eq(TemplateFavorite::getUserId, userId)
        .eq(TemplateFavorite::getTemplateId, templateId));
  }
}
