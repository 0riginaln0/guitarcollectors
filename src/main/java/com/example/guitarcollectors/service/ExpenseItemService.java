package com.example.guitarcollectors.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.guitarcollectors.exception.ExpenseItemHasChargesException;
import com.example.guitarcollectors.exception.ExpenseItemNotFoundException;
import com.example.guitarcollectors.model.Charge;
import com.example.guitarcollectors.model.ExpenseItem;
import com.example.guitarcollectors.repository.ExpenseItemsRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ExpenseItemService {
    private final ExpenseItemsRepository repository;

    // Показать все статьи расходов
    public List<ExpenseItem> getAllExpenseItems() {
        return (List<ExpenseItem>) repository.findAll();
    }

    // Показать статью расходов по id
    public List<Charge> getChargesForExpenseItem(Long expenseItemId) {
        ExpenseItem expenseItem = repository.findById(expenseItemId)
                .orElseThrow(() -> new ExpenseItemNotFoundException(expenseItemId));
        return expenseItem.getCharges();
    }

    // Показать статью расходов по id
    public ExpenseItem getExpenseItemById(Long expenseItemId) {
        ExpenseItem response = repository.findById(expenseItemId)
                .orElseThrow(() -> new ExpenseItemNotFoundException(expenseItemId));
        return response;
    }

    // Добавить статью
    public ExpenseItem addNewExpenseItem(ExpenseItem newExpenseItem) {
        ExpenseItem savedExpenseItem = repository.save(newExpenseItem);
        return savedExpenseItem;
    }

    public ExpenseItem updateExpenseItem(Long expenseItemId, ExpenseItem updatedExpenseItem) {
        repository.findById(expenseItemId)
                .orElseThrow(() -> new ExpenseItemNotFoundException(expenseItemId));
        updatedExpenseItem.setId(expenseItemId);
        return repository.save(updatedExpenseItem);
    }

    public void deleteExpenseItem(Long expenseItemId) {
        List<Charge> charges = getChargesForExpenseItem(expenseItemId);
        if (charges.isEmpty()) {
            repository.deleteById(expenseItemId);
        } else {
            throw new ExpenseItemHasChargesException(expenseItemId);
        }
    }

}
