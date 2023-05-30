package com.example.guitarcollectors.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.example.guitarcollectors.service.ChargeService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController()
@RequestMapping("api/charges")
@AllArgsConstructor
public class ChargeController {
    private final ChargeService chargeService;

    // Показать все расходы
    @GetMapping("/")
    public ResponseEntity<List<Charge>> getAllCharges() {
        return new ResponseEntity<>(chargeService.getAllCharges(), HttpStatus.OK);
    }

    // Показать расход по id
    @GetMapping("/{chargeId}")
    public ResponseEntity<Charge> getChargeItemById(@PathVariable Long chargeId) {
        return new ResponseEntity<>(chargeService.getChargeItemById(chargeId), HttpStatus.OK);
    }

    // Добавить расход
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Charge> addNewCharge(@RequestBody Charge newCharge) {
        validate(newCharge);
        Charge charge = chargeService.addNewCharge(newCharge);
        return new ResponseEntity<>(charge, HttpStatus.CREATED);
    }

    public void validate(Charge newCharge) {
        if (newCharge.getAmount() == null) {
            throw new BadRequestException("Charge's amount cannot be null");
        }
        if (newCharge.getExpenseItem().getId() == null) {
            throw new BadRequestException("Charge's expense item id cannot be null");
        }
        if (newCharge.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Charge amount cannot be less than or equal to zero");
        }
    }

    // Обновить расход
    @PutMapping(path = "/{chargeId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Charge> updateCharge(@PathVariable Long chargeId,
            @RequestBody Charge updatedCharge) {
        Charge charge = chargeService.updateCharge(chargeId, updatedCharge);
        return new ResponseEntity<>(charge, HttpStatus.CREATED);
    }

    // Удалить расход
    @DeleteMapping(path = "/{chargeId}")
    public ResponseEntity<String> deleteCharge(@PathVariable Long chargeId) {
        chargeService.deleteCharge(chargeId);
        return new ResponseEntity<>("Resource deleted successfully", HttpStatus.NO_CONTENT);
    }

}
