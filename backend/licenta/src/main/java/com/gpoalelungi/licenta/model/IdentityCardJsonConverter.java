package com.gpoalelungi.licenta.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Converter
@Component
@Slf4j
public class IdentityCardJsonConverter implements AttributeConverter<IdentityCard, String> {

  private static ObjectMapper mapper;

  @Autowired
  @SuppressWarnings("squid:S2696")
  public void setJacksonObjectMapper(ObjectMapper jacksonObjectMapper) {
    IdentityCardJsonConverter.mapper = jacksonObjectMapper;
  }

  @Override
  public String convertToDatabaseColumn(IdentityCard attribute) {
    try {
      return mapper.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      // TODO exception
      throw new RuntimeException("Cannot convert to JSON", e);
    }
  }

  @Override
  public IdentityCard convertToEntityAttribute(String identityCardData) {
    if (StringUtils.isBlank(identityCardData)) {
      return null;
    } else {
      try {
        return mapper.readValue(identityCardData, IdentityCard.class);
      } catch (IOException e) {
        log.warn("Cannot deserialize {} into a IdentityCard instance", identityCardData);
        return null;
      }
    }
  }
}