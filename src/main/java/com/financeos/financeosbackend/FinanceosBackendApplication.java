package com.financeos.financeosbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FinanceosBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceosBackendApplication.class, args);
    }

}
