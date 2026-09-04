package com.financeos.financeosbackend.transaction.service;

import com.financeos.financeosbackend.transaction.dto.TransactionClassificationResponse;
import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.entity.TransactionClassification;
import com.financeos.financeosbackend.transaction.entity.TransactionClassificationCorrection;
import com.financeos.financeosbackend.transaction.enums.TransactionType;
import com.financeos.financeosbackend.transaction.repository.TransactionClassificationRepository;
import com.financeos.financeosbackend.transaction.repository.TransactionClassificationCorrectionRepository;
import org.springframework.stereotype.Service;
import com.financeos.financeosbackend.transaction.dto.TransactionClassificationCorrectionResponse;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionClassificationService {

    private final TransactionClassificationRepository classificationRepository;
    private final TransactionClassificationCorrectionRepository correctionRepository;

    public TransactionClassificationService(
            TransactionClassificationRepository classificationRepository,
            TransactionClassificationCorrectionRepository correctionRepository) {

        this.classificationRepository = classificationRepository;
        this.correctionRepository = correctionRepository;
    }

    public TransactionClassificationResponse createSuggestion(
            FinancialTransaction transaction,
            TransactionType suggestedType,
            String suggestedCategory,
            BigDecimal confidence) {

        TransactionClassification classification =
                classificationRepository
                        .findByTransaction(transaction)
                        .orElseGet(TransactionClassification::new);

        classification.setTransaction(transaction);
        classification.setSuggestedType(suggestedType);
        classification.setSuggestedCategory(suggestedCategory);
        classification.setConfidence(confidence);
        classification.setCreatedAt(LocalDateTime.now());

        TransactionClassification savedClassification =
                classificationRepository.save(classification);

        return mapToResponse(savedClassification);
    }

    public TransactionClassificationResponse getSuggestion(
            FinancialTransaction transaction) {

        return classificationRepository
                .findByTransaction(transaction)
                .map(this::mapToResponse)
                .orElse(null);
    }

    public void recordCorrection(
            FinancialTransaction transaction,
            TransactionType correctedType,
            String correctedCategory) {

        TransactionClassification classification =
                classificationRepository
                        .findByTransaction(transaction)
                        .orElse(null);

        if (classification == null) {
            return;
        }

        boolean typeChanged =
                classification.getSuggestedType() != correctedType;

        boolean categoryChanged =
                classification.getSuggestedCategory() == null
                        ? correctedCategory != null
                        : !classification.getSuggestedCategory()
                        .equalsIgnoreCase(correctedCategory);

        if (!typeChanged && !categoryChanged) {
            return;
        }

        TransactionClassificationCorrection correction =
                new TransactionClassificationCorrection();

        correction.setTransaction(transaction);
        correction.setSuggestedType(
                classification.getSuggestedType()
        );
        correction.setSuggestedCategory(
                classification.getSuggestedCategory()
        );
        correction.setCorrectedType(correctedType);
        correction.setCorrectedCategory(correctedCategory);
        correction.setCreatedAt(LocalDateTime.now());

        correctionRepository.save(correction);
    }

    public List<TransactionClassificationCorrectionResponse>
    getCorrectionHistory(FinancialTransaction transaction) {

        return correctionRepository.findByTransaction(transaction)
                .stream()
                .map(correction -> {

                    TransactionClassificationCorrectionResponse response =
                            new TransactionClassificationCorrectionResponse();

                    response.setId(correction.getId());
                    response.setTransactionId(
                            correction.getTransaction().getId()
                    );
                    response.setSuggestedType(
                            correction.getSuggestedType()
                    );
                    response.setSuggestedCategory(
                            correction.getSuggestedCategory()
                    );
                    response.setCorrectedType(
                            correction.getCorrectedType()
                    );
                    response.setCorrectedCategory(
                            correction.getCorrectedCategory()
                    );
                    response.setCreatedAt(
                            correction.getCreatedAt()
                    );

                    return response;
                })
                .toList();
    }

    private TransactionClassificationResponse mapToResponse(
            TransactionClassification classification) {

        TransactionClassificationResponse response =
                new TransactionClassificationResponse();

        response.setId(classification.getId());

        response.setTransactionId(
                classification.getTransaction().getId()
        );

        response.setSuggestedType(
                classification.getSuggestedType()
        );

        response.setSuggestedCategory(
                classification.getSuggestedCategory()
        );

        response.setConfidence(
                classification.getConfidence()
        );

        response.setCreatedAt(
                classification.getCreatedAt()
        );

        return response;
    }
}