package com.example.guitarcollectors.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.guitarcollectors.exception.BadRequestException;
import com.example.guitarcollectors.model.Charge;
import com.example.guitarcollectors.model.ExpenseItem;
import com.example.guitarcollectors.service.ExpenseItemService;

import lombok.AllArgsConstructor;

@RestController()
@RequestMapping("api/expense-items")
@AllArgsConstructor
public class ExpenseItemController {
    private final ExpenseItemService expenseItemService;

    // Показать все статьи расходов
    @GetMapping("/")
    public ResponseEntity<List<ExpenseItem>> getAllExpenseItems() {
        return new ResponseEntity<>(expenseItemService.getAllExpenseItems(), HttpStatus.OK);
    }

    // Показать статью расходов по id
    @GetMapping("/{expenseItemId}")
    public ResponseEntity<ExpenseItem> getExpenseItemById(@PathVariable Long expenseItemId) {
        return new ResponseEntity<>(expenseItemService.getExpenseItemById(expenseItemId), HttpStatus.OK);
    }

    // Добавить статью
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExpenseItem> addNewExpenseItem(@RequestBody ExpenseItem newExpenseItem) {
        validateExpenseItem(newExpenseItem);
        ExpenseItem expenseItem = expenseItemService.addNewExpenseItem(newExpenseItem);
        return new ResponseEntity<>(expenseItem, HttpStatus.CREATED);
    }

    // Обновить статью
    @PutMapping(path = "/{expenseItemId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExpenseItem> updateExpenseItem(@PathVariable Long expenseItemId,
            @RequestBody ExpenseItem updatedExpenseItem) {
        validateExpenseItem(updatedExpenseItem);
        ExpenseItem expenseItem = expenseItemService.updateExpenseItem(expenseItemId, updatedExpenseItem);
        return new ResponseEntity<>(expenseItem, HttpStatus.CREATED);
    }

    // Удалить статью
    @DeleteMapping(path = "/{expenseItemId}")
    public ResponseEntity<String> deleteExpenseItem(@PathVariable Long expenseItemId) {
        expenseItemService.deleteExpenseItem(expenseItemId);
        return new ResponseEntity<String>("Expense item deleted successfully", HttpStatus.OK);
    }

    // Показать расходы по определённой статье
    @GetMapping("/charges/{expenseItemId}")
    public ResponseEntity<List<Charge>> getChargesForExpenseItem(@PathVariable Long expenseItemId) {
        return new ResponseEntity<>(expenseItemService.getChargesForExpenseItem(expenseItemId), HttpStatus.OK);
    }

    public void validateExpenseItem(ExpenseItem expenseItem) {
        if (expenseItem.getName() == null) {
            throw new BadRequestException("Expense item's name cannot be null");
        }
        if (expenseItem.getName().isEmpty()) {
            throw new BadRequestException("Expense item's name cannot be empty");
        }
    }
}