package com.example.guitarcollectors.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.guitarcollectors.exception.MyEntityNotFoundException;
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
                .orElseThrow(() -> new MyEntityNotFoundException("Charge with id " + chargeId + "is not found"));
        return response;
    }

    public Charge addNewCharge(Charge newCharge) {
        expenseItemService.getExpenseItemById(newCharge.getExpenseItem().getId());
        newCharge.setChargeDate(LocalDateTime.now());
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
