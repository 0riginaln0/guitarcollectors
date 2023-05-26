package com.example.guitarcollectors.controller;

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

import com.example.guitarcollectors.model.Sale;
import com.example.guitarcollectors.service.SaleService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController()
@RequestMapping("api/sales")
@AllArgsConstructor
public class SaleController {
    private final SaleService saleService;

    // Показать все продажи
    @GetMapping("/")
    public ResponseEntity<List<Sale>> getAllSales() {
        return new ResponseEntity<>(saleService.getAllSales(), HttpStatus.OK);
    }

    // Показать продажу по id
    @GetMapping("/{saleId}")
    public ResponseEntity<Sale> getSaleItemById(@PathVariable Long saleId) {
        return new ResponseEntity<>(saleService.getSaleItemById(saleId), HttpStatus.OK);
    }

    // Добавить продажу (по умолчанию quantity = 1)
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Sale> addNewSale(@RequestBody Sale newSale) {
        Sale sale = saleService.addNewSale(newSale);
        return new ResponseEntity<>(sale, HttpStatus.CREATED);
    }

    // Добавить продажу со своим quantity
    @PostMapping(path = "/{quantity}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Sale> addNewSale(@RequestBody Sale newSale, @PathVariable Integer quantity) {
        Sale sale = saleService.addNewSale(newSale, quantity);
        return new ResponseEntity<>(sale, HttpStatus.CREATED);
    }

    // Обновить продажу
    @PutMapping(path = "/{saleId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sale> updateSale(@PathVariable Long saleId,
            @RequestBody Sale updatedSale) {
        Sale sale = saleService.updateSale(saleId, updatedSale);
        return new ResponseEntity<>(sale, HttpStatus.CREATED);
    }

    // Удалить продажу
    @DeleteMapping(path = "/{saleId}")
    public void deleteSale(@PathVariable Long saleId) {
        saleService.deleteSale(saleId);
    }

    // Put Дать скидку процентом
    @PutMapping(path = "/{saleId}/percentage-discount/{percentage}")
    public ResponseEntity<Sale> giveDiscountByPercentage(@PathVariable Long saleId, @PathVariable Integer percentage) {
        Sale sale = saleService.giveDiscountByPercentage(saleId, percentage);
        return new ResponseEntity<>(sale, HttpStatus.CREATED);
    }

    // Put Дать скидку целым числом
    @PutMapping(path = "/{saleId}/amount-discount/{amount}")
    public ResponseEntity<Sale> giveDiscountOnAmount(@PathVariable Long saleId, @PathVariable Integer amount) {
        Sale sale = saleService.giveDiscountOnAmount(saleId, amount);
        return new ResponseEntity<>(sale, HttpStatus.CREATED);
    }
}
