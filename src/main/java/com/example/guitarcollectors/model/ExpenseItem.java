package com.example.guitarcollectors.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Таблица статей расходов
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "expense_items")
public class ExpenseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Наименование статьи
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "expenseItem", cascade = CascadeType.ALL)
    @Column(nullable = false)
    private List<Charge> charges;

    public ExpenseItem(String name) {
        this.name = name;
    }

}
