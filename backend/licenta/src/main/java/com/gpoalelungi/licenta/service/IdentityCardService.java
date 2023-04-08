package com.gpoalelungi.licenta.service;

import com.gpoalelungi.licenta.model.IdentityCard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class IdentityCardService {

  public boolean validateIdentityCard(IdentityCard identityCard) {
    return validateCNP(identityCard.getCNP()) && validateAgeFromCNP(identityCard.getCNP()) && validateExpirationDateFromIdentityCard(identityCard) && validateIdCardNumber(identityCard);
  }

  private boolean validateCNP(String CNP) {
        if (CNP.length() != 13) {
          return false;
        }
        int[] cnp = new int[13];
        for (int i = 0; i < 13; i++) {
          cnp[i] = CNP.charAt(i) - '0';
        }
        int[] control = {2, 7, 9, 1, 4, 6, 3, 5, 8, 2, 7, 9};
        int sum = 0;
        for (int i = 0; i < 12; i++) {
          sum += cnp[i] * control[i];
        }
        int rest = sum % 11;
        if (rest == 10) {
          rest = 1;
        }
        return rest == cnp[12];
  }

  public boolean validateAgeFromCNP(String CNP) {
        if (CNP.length() != 13) {
          return false;
        }
        int[] cnp = new int[13];
        for (int i = 0; i < 13; i++) {
          cnp[i] = CNP.charAt(i) - '0';
        }
        int year = cnp[1] * 10 + cnp[2];
        int month = cnp[3] * 10 + cnp[4];
        int day = cnp[5] * 10 + cnp[6];
        if (month > 12) {
          month -= 50;
          year += 2000;
        } else {
          year += 1900;
        }
        if (month == 2) {
          if (year % 4 == 0) {
                if (day > 29) {
                  return false;
                }
          } else {
                if (day > 28) {
                  return false;
                }
          }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
          if (day > 30) {
                return false;
          }
        } else {
          if (day > 31) {
                return false;
          }
        }
        return true;
  }

  private boolean validateExpirationDateFromIdentityCard(IdentityCard identityCard) {
      return identityCard.getExpirationDate().after(Date.from(Instant.now()));
  }

  private boolean validateIdCardNumber(IdentityCard identityCard) {
    return identityCard.getIdCardNumber().matches("[A-Z][A-Z][0-9][0-9][0-9][0-9][0-9][0-9]");
  }
}
