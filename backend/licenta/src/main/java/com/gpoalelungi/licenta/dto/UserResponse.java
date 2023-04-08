package com.gpoalelungi.licenta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
  private String email;
  private String phoneNumber;
}
