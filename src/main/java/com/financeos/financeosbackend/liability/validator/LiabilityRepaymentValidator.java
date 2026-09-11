package com.financeos.financeosbackend.liability.validator;

import com.financeos.financeosbackend.liability.dto.CreateLiabilityRepaymentRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class LiabilityRepaymentValidator {

    public void validate(
            CreateLiabilityRepaymentRequest request,
            BigDecimal outstandingAmount
    ) {
        BigDecimal paymentAmount = request.getPaymentAmount();
        BigDecimal principalAmount = request.getPrincipalAmount();
        BigDecimal interestAmount = request.getInterestAmount();

        if (principalAmount != null
                && interestAmount != null) {

            BigDecimal calculatedTotal =
                    principalAmount.add(interestAmount);

            if (calculatedTotal.compareTo(paymentAmount) != 0) {
                throw new IllegalArgumentException(
                        "Principal amount and interest amount must equal payment amount"
                );
            }
        }

        if (principalAmount != null
                && principalAmount.compareTo(outstandingAmount) > 0) {
            throw new IllegalArgumentException(
                    "Principal amount cannot exceed outstanding amount"
            );
        }
    }
}