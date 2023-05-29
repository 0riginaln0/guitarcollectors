package com.example.guitarcollectors.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.guitarcollectors.controller.ExpenseItemController;
import com.example.guitarcollectors.exception.ChargeNotFoundException;
import com.example.guitarcollectors.model.Charge;
import com.example.guitarcollectors.repository.ChargeRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChargeService {
    private final ChargeRepository repository;
    private final ExpenseItemService expenseItemService;

    public List<Charge> getAllCharges() {
        return (List<Charge>) repository.findAll();
    }

    public Charge getChargeItemById(Long chargeId) {
        Charge response = repository.findById(chargeId)
                .orElseThrow(() -> new ChargeNotFoundException(chargeId));
        return response;
    }

    public Charge addNewCharge(Charge newCharge) {
        expenseItemService.getExpenseItemById(newCharge.getExpenseItem().getId());
        Charge addedCharge = repository.save(newCharge);
        return addedCharge;
    }

    public Charge updateCharge(Long chargeId, Charge updatedCharge) {
        Optional<Charge> response = repository.findById(chargeId);
        if (response.isEmpty()) {
            throw new IllegalArgumentException();
        }
        updatedCharge.setId(chargeId);
        return repository.save(updatedCharge);
    }

    public void deleteCharge(Long chargeId) {
        repository.deleteById(chargeId);
    }

}
