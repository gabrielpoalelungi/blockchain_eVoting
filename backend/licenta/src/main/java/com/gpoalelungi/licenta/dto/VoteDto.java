package com.gpoalelungi.licenta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteDto {
  private String publicKey;
  private String encryptedVote;
  private String signature;
}
