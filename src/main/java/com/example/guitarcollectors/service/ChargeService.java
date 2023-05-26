package com.example.guitarcollectors.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.guitarcollectors.model.Charge;
import com.example.guitarcollectors.repository.ChargeRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChargeService {
    private final ChargeRepository repository;

    public List<Charge> getAllCharges() {
        return (List<Charge>) repository.findAll();
    }

    public Charge getChargeItemById(Long chargeId) {
        Optional<Charge> response = repository.findById(chargeId);
        if (response.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return response.get();
    }

    public Charge addNewCharge(Charge newCharge) {
        return repository.save(newCharge);
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
