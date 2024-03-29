package com.gpoalelungi.licenta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateResponse {
  private Long id;
  private String officialName;
  private String description;
  private Long numberOfVotes;
}
