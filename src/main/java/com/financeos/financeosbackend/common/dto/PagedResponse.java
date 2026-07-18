package com.financeos.financeosbackend.common.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PagedResponse<T> {

    private boolean success;
    private int status;
    private String message;
    private LocalDateTime timestamp;

    private List<T> data;

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public PagedResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public PagedResponse(boolean success,
                         int status,
                         String message,
                         List<T> data,
                         int page,
                         int size,
                         long totalElements,
                         int totalPages,
                         boolean last) {

        this.success = success;
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();

        this.data = data;

        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public static <T> PagedResponse<T> success(
            int status,
            String message,
            List<T> data,
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean last) {

        return new PagedResponse<>(
                true,
                status,
                message,
                data,
                page,
                size,
                totalElements,
                totalPages,
                last
        );
    }
}