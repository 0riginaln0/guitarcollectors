package com.example.guitarcollectors.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.guitarcollectors.model.Charge;
import com.example.guitarcollectors.model.ExpenseItem;
import com.example.guitarcollectors.repository.ExpenseItemsRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ExpenseItemService {
    private final ExpenseItemsRepository repository;

    public List<ExpenseItem> getAllExpenseItems() {
        return (List<ExpenseItem>) repository.findAll();
    }

    public List<Charge> getChargesForExpenseItem(Long expenseItemId) {
        ExpenseItem expenseItem = repository.findById(expenseItemId)
                .orElseThrow(() -> new EntityNotFoundException("Expense item not found"));
        return expenseItem.getCharges();
    }

    public ExpenseItem getExpenseItemById(Long expenseItemId) {
        Optional<ExpenseItem> response = repository.findById(expenseItemId);
        if (response.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return response.get();
    }

    public ExpenseItem addNewExpenseItem(ExpenseItem newExpenseItem) {
        return repository.save(newExpenseItem);
    }

    public ExpenseItem updateExpenseItem(Long expenseItemId, ExpenseItem updatedExpenseItem) {
        Optional<ExpenseItem> response = repository.findById(expenseItemId);
        if (response.isEmpty()) {
            throw new IllegalArgumentException();
        }
        updatedExpenseItem.setId(expenseItemId);
        return repository.save(updatedExpenseItem);
    }

    public void deleteExpenseItem(Long expenseItemId) {
        // TODO:
        // Проверка, если в таблице расходов есть запись, ссылающаяся на статью
        // расходов, то нельзя удалять.
        repository.deleteById(expenseItemId);
    }

}
