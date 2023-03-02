package com.gpoalelungi.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsResponse {
  String userEmail;
  String cnp;
  String idCardNumber;
  String votingPublicKey;
  Boolean hasVoted;
}
