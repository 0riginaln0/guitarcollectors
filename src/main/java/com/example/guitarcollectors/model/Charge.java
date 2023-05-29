package com.example.guitarcollectors.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Таблица расходов
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "charges")
public class Charge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Сумма
    @Column(nullable = false)
    private BigDecimal amount;

    // Дата
    @Column(name = "charge_date", nullable = false)
    private LocalDateTime chargeDate;

    // Статья расхода
    @ManyToOne
    @JoinColumn(name = "expense_item_id", nullable = false)
    @JsonIgnoreProperties("charges")
    private ExpenseItem expenseItem;

    public Charge(BigDecimal amount, LocalDateTime chargeDate, ExpenseItem expenseItem) {
        this.amount = amount;
        this.chargeDate = chargeDate;
        this.expenseItem = expenseItem;
    }

}
