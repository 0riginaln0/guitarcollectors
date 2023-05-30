package com.example.guitarcollectors.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.guitarcollectors.exception.ForbiddenRequestException;
import com.example.guitarcollectors.exception.MyEntityNotFoundException;
import com.example.guitarcollectors.model.Charge;
import com.example.guitarcollectors.model.ExpenseItem;
import com.example.guitarcollectors.repository.ExpenseItemsRepository;

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
    public ExpenseItem getExpenseItemById(Long expenseItemId) {
        ExpenseItem response = repository.findById(expenseItemId)
                .orElseThrow(
                        () -> new MyEntityNotFoundException("expense item with id "
                                + expenseItemId + " is not found"));
        return response;
    }

    // Добавить статью
    public ExpenseItem addNewExpenseItem(ExpenseItem newExpenseItem) {
        return repository.save(newExpenseItem);
    }

    // Обновить статью
    public ExpenseItem updateExpenseItem(Long expenseItemId, ExpenseItem updatedExpenseItem) {
        repository.findById(expenseItemId)
                .orElseThrow(() -> new MyEntityNotFoundException("expense item with id " + expenseItemId));
        updatedExpenseItem.setId(expenseItemId);
        return repository.save(updatedExpenseItem);
    }

    // Удалить статью
    public void deleteExpenseItem(Long expenseItemId) {
        List<Charge> charges = getChargesForExpenseItem(expenseItemId);
        if (charges.isEmpty()) {
            repository.deleteById(expenseItemId);
        } else {
            throw new ForbiddenRequestException(
                    "Cannot delete expense item with id " + expenseItemId + " because it's being used.");
        }
    }

    // Показать расходы по определённой статье
    public List<Charge> getChargesForExpenseItem(Long expenseItemId) {
        ExpenseItem expenseItem = repository.findById(expenseItemId)
                .orElseThrow(() -> new MyEntityNotFoundException("Can't find expense item with id " + expenseItemId));
        return expenseItem.getCharges();
    }

}
