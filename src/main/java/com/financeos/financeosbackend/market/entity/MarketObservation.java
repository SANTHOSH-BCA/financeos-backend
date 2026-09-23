package com.financeos.financeosbackend.market.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "market_observations")
public class MarketObservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instrument_id", nullable = false)
    private MarketInstrument instrument;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal currentValue;

    @Column(precision = 19, scale = 6)
    private BigDecimal dailyChange;

    @Column(precision = 10, scale = 4)
    private BigDecimal dailyChangePercentage;

    @Column(precision = 19, scale = 6)
    private BigDecimal previousClose;

    @Column(nullable = false)
    private LocalDateTime observedAt;

    @Column(nullable = false, length = 100)
    private String source;

    @Column(name = "is_delayed", nullable = false)
    private boolean delayed;

    public Long getId() {
        return id;
    }

    public MarketInstrument getInstrument() {
        return instrument;
    }

    public void setInstrument(MarketInstrument instrument) {
        this.instrument = instrument;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getDailyChange() {
        return dailyChange;
    }

    public void setDailyChange(BigDecimal dailyChange) {
        this.dailyChange = dailyChange;
    }

    public BigDecimal getDailyChangePercentage() {
        return dailyChangePercentage;
    }

    public void setDailyChangePercentage(BigDecimal dailyChangePercentage) {
        this.dailyChangePercentage = dailyChangePercentage;
    }

    public BigDecimal getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(BigDecimal previousClose) {
        this.previousClose = previousClose;
    }

    public LocalDateTime getObservedAt() {
        return observedAt;
    }

    public void setObservedAt(LocalDateTime observedAt) {
        this.observedAt = observedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isDelayed() {
        return delayed;
    }

    public void setDelayed(boolean delayed) {
        this.delayed = delayed;
    }
}