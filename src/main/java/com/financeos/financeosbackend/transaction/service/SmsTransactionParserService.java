package com.financeos.financeosbackend.transaction.service;

import com.financeos.financeosbackend.transaction.dto.SmsTransactionParseResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SmsTransactionParserService {

    private static final Pattern AMOUNT_PATTERN =
            Pattern.compile(
                    "(?:INR|Rs\\.?|₹)\\s*([0-9,]+(?:\\.\\d{1,2})?)",
                    Pattern.CASE_INSENSITIVE
            );

    private static final Pattern REFERENCE_PATTERN =
            Pattern.compile(
                    "(?:UPI Ref(?:erence)?|Ref(?:erence)?|Txn(?:\\.?|\\s)?ID)\\s*[:#-]?\\s*([A-Za-z0-9]+)",
                    Pattern.CASE_INSENSITIVE
            );

    private static final Pattern MERCHANT_PATTERN =
            Pattern.compile(
                    "(?:at|to|from)\\s+([A-Za-z0-9&._' -]{2,80}?)(?=\\s+(?:on|via|using|for|Ref|Txn|UPI)|[.,]|$)",
                    Pattern.CASE_INSENSITIVE
            );

    public SmsTransactionParseResult parse(String message) {

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("SMS message is required");
        }

        SmsTransactionParseResult result =
                new SmsTransactionParseResult();

        Matcher amountMatcher = AMOUNT_PATTERN.matcher(message);

        if (!amountMatcher.find()) {
            throw new IllegalArgumentException(
                    "Unable to detect transaction amount from SMS"
            );
        }

        String amountValue =
                amountMatcher.group(1).replace(",", "");

        result.setAmount(new BigDecimal(amountValue));

        String lowerMessage = message.toLowerCase();

        boolean debit =
                lowerMessage.contains("debited")
                        || lowerMessage.contains("debit")
                        || lowerMessage.contains("spent")
                        || lowerMessage.contains("paid")
                        || lowerMessage.contains("sent");

        boolean credit =
                lowerMessage.contains("credited")
                        || lowerMessage.contains("credit")
                        || lowerMessage.contains("received");

        if (!debit && !credit) {
            throw new IllegalArgumentException(
                    "Unable to determine whether SMS represents a debit or credit"
            );
        }

        result.setDebit(debit);

        Matcher merchantMatcher =
                MERCHANT_PATTERN.matcher(message);

        if (merchantMatcher.find()) {
            result.setMerchantPayee(
                    merchantMatcher.group(1).trim()
            );
        }

        Matcher referenceMatcher =
                REFERENCE_PATTERN.matcher(message);

        if (referenceMatcher.find()) {
            result.setReference(
                    referenceMatcher.group(1).trim()
            );
        }

        result.setTransactionDateTime(LocalDateTime.now());

        return result;
    }
}