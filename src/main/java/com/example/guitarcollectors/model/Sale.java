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

// Таблица продаж
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Стоимость одной единицы товара
    @Column(nullable = false)
    private BigDecimal amount;

    // Количество товара
    @Column(nullable = false)
    private Integer quantity;

    // Дата продажи
    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;

    // Товар
    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    @JsonIgnoreProperties("sales")
    private Warehouse warehouse;

    public Sale(BigDecimal amount, Integer quantity, LocalDateTime saleDate, Warehouse warehouse) {
        this.amount = amount;
        this.quantity = quantity;
        this.saleDate = saleDate;
        this.warehouse = warehouse;
    }

}
