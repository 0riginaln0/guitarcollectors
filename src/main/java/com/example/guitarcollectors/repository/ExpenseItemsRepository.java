package com.example.guitarcollectors.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.guitarcollectors.model.ExpenseItem;

public interface ExpenseItemsRepository extends CrudRepository<ExpenseItem, Long> {

}
