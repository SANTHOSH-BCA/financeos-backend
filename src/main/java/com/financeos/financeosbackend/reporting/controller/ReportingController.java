package com.financeos.financeosbackend.reporting.controller;

import com.financeos.financeosbackend.reporting.dto.FinancialReportResponse;
import com.financeos.financeosbackend.reporting.service.ReportingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.financeos.financeosbackend.reporting.pdf.PdfReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.financeos.financeosbackend.reporting.excel.ExcelReportService;
import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;


@RestController
@RequestMapping("/api/reports")
public class ReportingController {

    private final ReportingService reportingService;
    private final PdfReportService pdfReportService;
    private final ExcelReportService excelReportService;

    public ReportingController(
            ReportingService reportingService,
            PdfReportService pdfReportService,
            ExcelReportService excelReportService) {

        this.reportingService = reportingService;
        this.pdfReportService = pdfReportService;
        this.excelReportService = excelReportService;
    }
    @GetMapping("/financial-summary")
    public ResponseEntity<ApiResponse<FinancialReportResponse>> generateFinancialReport() {

        FinancialReportResponse response =
                reportingService.generateFinancialReport();

        return ResponseBuilder.success(
                "Financial report retrieved successfully",
                response
        );
    }
    @GetMapping("/download/pdf")
    public ResponseEntity<byte[]> downloadPdfReport() {

        FinancialReportResponse report =
                reportingService.generateFinancialReport();

        byte[] pdf =
                pdfReportService.generatePdf(report);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=FinanceOS-Report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/download/excel")
    public ResponseEntity<byte[]> downloadExcelReport() {

        FinancialReportResponse report =
                reportingService.generateFinancialReport();

        byte[] excel =
                excelReportService.generateExcel(report);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=FinanceOS-Report.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excel);
    }
}