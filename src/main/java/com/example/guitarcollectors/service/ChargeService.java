package com.example.guitarcollectors.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.guitarcollectors.model.Charge;
import com.example.guitarcollectors.repository.ChargeRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChargeService {
    private final ChargeRepository repository;
    private final ExpenseItemService expenseItemService;

    // Показать все расходы
    public List<Charge> getAllCharges() {
        return (List<Charge>) repository.findAll();
    }

    // Показать расход по id
    public Charge getChargeItemById(Long chargeId) {
        Charge response = repository.findById(chargeId)
                .orElseThrow(() -> new EntityNotFoundException("Charge with id " + chargeId + " is not found"));
        return response;
    }

    // Добавить расход
    public Charge addNewCharge(Charge newCharge) {
        expenseItemService.getExpenseItemById(newCharge.getExpenseItem().getId());
        newCharge.setChargeDate(LocalDateTime.now());
        Charge addedCharge = repository.save(newCharge);
        return addedCharge;
    }

    // Обновить расход
    public Charge updateCharge(Long chargeId, Charge updatedCharge) {
        repository.findById(chargeId)
                .orElseThrow(() -> new EntityNotFoundException("Charge with id " + chargeId + " is not found"));
        updatedCharge.setId(chargeId);

        return addNewCharge(updatedCharge);
    }

    // Удалить расход
    public void deleteCharge(Long chargeId) {
        repository.findById(chargeId)
                .orElseThrow(() -> new EntityNotFoundException("Charge with id " + chargeId + " is not found"));
        repository.deleteById(chargeId);
    }

}
