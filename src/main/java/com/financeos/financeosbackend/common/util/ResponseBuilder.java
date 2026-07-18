package com.financeos.financeosbackend.common.util;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.dto.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {

    private ResponseBuilder() {
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        message,
                        data
                )
        );
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                HttpStatus.CREATED.value(),
                                message,
                                data
                        )
                );
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status,
                                                           String message) {

        return ResponseEntity.status(status)
                .body(
                        ApiResponse.error(
                                status.value(),
                                message
                        )
                );
    }

    public static <T> ResponseEntity<PagedResponse<T>> paged(
            String message,
            Page<T> page) {

        PagedResponse<T> response =
                PagedResponse.success(
                        HttpStatus.OK.value(),
                        message,
                        page.getContent(),
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages(),
                        page.isLast()
                );

        return ResponseEntity.ok(response);
    }
}