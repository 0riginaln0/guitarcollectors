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
import com.example.guitarcollectors.model.Sale;
import com.example.guitarcollectors.service.SaleService;

import lombok.AllArgsConstructor;

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

    // Добавить продажу
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Sale> addNewSale(@RequestBody Sale newSale) {
        validate(newSale);
        Sale sale = saleService.addNewSale(newSale);
        return new ResponseEntity<>(sale, HttpStatus.CREATED);
    }

    // Продать со скидкой процентом
    @PostMapping(path = "/percentage-discount/{percentage}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Sale> addNewSaleDiscountPercentage(@RequestBody Sale newSale,
            @PathVariable Integer percentage) {
        validate(newSale);
        validateDiscount(percentage);
        Sale sale = saleService.addNewSaleDiscountPercentage(newSale, percentage);
        return new ResponseEntity<>(sale, HttpStatus.CREATED);
    }

    // Продать со скидкой абсолютным значением
    @PostMapping(path = "/absolute-discount/{amount}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Sale> addNewSaleDiscountAmount(@RequestBody Sale newSale, @PathVariable Integer amount) {
        validate(newSale);
        validateDiscount(amount);
        Sale sale = saleService.addNewSaleDiscountAmount(newSale, amount);
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
    public ResponseEntity<String> deleteSale(@PathVariable Long saleId) {
        saleService.deleteSale(saleId);
        return new ResponseEntity<>("Resource deleted successfully", HttpStatus.OK);
    }

    // Put Дать скидку процентом
    @PutMapping(path = "/{saleId}/percentage-discount/{percentage}")
    public ResponseEntity<Sale> giveDiscountByPercentage(@PathVariable Long saleId, @PathVariable Integer percentage) {
        validateDiscount(percentage);
        Sale sale = saleService.giveDiscountByPercentage(saleId, percentage);
        return new ResponseEntity<>(sale, HttpStatus.CREATED);
    }

    // Put Дать скидку абсолютным значением
    @PutMapping(path = "/{saleId}/absolute-discount/{amount}")
    public ResponseEntity<Sale> giveDiscountOnAmount(@PathVariable Long saleId, @PathVariable Integer amount) {
        validateDiscount(amount);
        Sale sale = saleService.giveDiscountOnAmount(saleId, amount);
        return new ResponseEntity<>(sale, HttpStatus.CREATED);
    }

    private void validateDiscount(Integer discount) {
        if (discount < 0) {
            throw new BadRequestException("Discount can't be less than or equal 0");
        }
    }

    public void validate(Sale newSale) {
        if (newSale.getQuantity() == null) {
            throw new BadRequestException("Sale's quantity cannot be null");
        }
        if (newSale.getWarehouse().getId() == null) {
            throw new BadRequestException("Sale's product id cannot be null");
        }
        if (newSale.getQuantity() < 1) {
            throw new BadRequestException("Sale's quantity cannot less than one");
        }
    }
}
