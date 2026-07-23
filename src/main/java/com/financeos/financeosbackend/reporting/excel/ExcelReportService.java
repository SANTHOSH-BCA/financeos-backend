package com.financeos.financeosbackend.reporting.excel;

import com.financeos.financeosbackend.reporting.dto.FinancialReportResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ExcelReportService {

    private static final Logger logger =
            LoggerFactory.getLogger(ExcelReportService.class);

    public byte[] generateExcel(FinancialReportResponse report) {

        logger.info("Generating Excel financial report");

        try {

            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFSheet sheet = workbook.createSheet("Finance Report");

            Row title = sheet.createRow(0);
            title.createCell(0).setCellValue("FinanceOS Financial Report");

            Row income = sheet.createRow(2);
            income.createCell(0).setCellValue("Total Income");
            income.createCell(1).setCellValue(report.getTotalIncome().doubleValue());

            Row expense = sheet.createRow(3);
            expense.createCell(0).setCellValue("Total Expense");
            expense.createCell(1).setCellValue(report.getTotalExpense().doubleValue());

            Row savings = sheet.createRow(4);
            savings.createCell(0).setCellValue("Total Savings");
            savings.createCell(1).setCellValue(report.getTotalSavings().doubleValue());

            Row netWorth = sheet.createRow(5);
            netWorth.createCell(0).setCellValue("Net Worth");
            netWorth.createCell(1).setCellValue(report.getNetWorth().doubleValue());

            Row health = sheet.createRow(6);
            health.createCell(0).setCellValue("Financial Health");
            health.createCell(1).setCellValue(report.getFinancialHealth());

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            workbook.write(outputStream);
            workbook.close();

            logger.info("Excel financial report generated successfully");

            return outputStream.toByteArray();

        } catch (Exception e) {

            logger.error("Failed to generate Excel financial report", e);

            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }
}