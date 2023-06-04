package com.example.guitarcollectors.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Таблица склада товаров
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Наименование
    @Column(nullable = false)
    private String name;

    // Количество товара
    @Column(nullable = false)
    private Integer quantity;

    // Стоимость одной единицы товара
    @Column(nullable = false)
    private BigDecimal amount;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    @Column(nullable = false)
    private List<Sale> Sales;

    public Warehouse(String name, Integer quantity, BigDecimal amount) {
        this.name = name;
        this.quantity = quantity;
        this.amount = amount;
    }
}
