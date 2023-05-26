package com.example.guitarcollectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GuitarcollectorsApplication {
	public static void main(String[] args) {
		SpringApplication.run(GuitarcollectorsApplication.class, args);
	}

	// To-Do
	// Выкуп гитары + автоматически выставить её на продажу с наценкой 20%
	// (ExpenseItems -> Charges -> Warehouse)

	// При продаже автоматически уменьшать quantity товара на один.
	// (Sales -> Warehouse)

	// Post процент скидки на товар.
	// (Sales -> Warehouse)
}
