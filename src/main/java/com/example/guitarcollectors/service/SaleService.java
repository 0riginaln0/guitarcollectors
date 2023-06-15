package com.example.guitarcollectors.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.guitarcollectors.exception.BadRequestException;
import com.example.guitarcollectors.exception.ForbiddenRequestException;
import com.example.guitarcollectors.model.Sale;
import com.example.guitarcollectors.model.Warehouse;
import com.example.guitarcollectors.repository.SaleRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SaleService {
    private final SaleRepository repository;
    private final WarehouseService warehouse;

    // Показать все продажи
    public List<Sale> getAllSales() {
        return (List<Sale>) repository.findAll();
    }

    // Показать продажу по id
    public Sale getSaleItemById(Long saleId) {
        Sale response = repository.findById(saleId)
                .orElseThrow(() -> new EntityNotFoundException("Sale with id " + saleId + " is not found"));
        return response;
    }

    // Добавить продажу
    public Sale addNewSale(Sale newSale) {
        Warehouse product = warehouse.getProductById(newSale.getWarehouse().getId());
        Integer warehouseQuantity = product.getQuantity();
        if (warehouseQuantity.compareTo(newSale.getQuantity()) < 0) {
            throw new ForbiddenRequestException(
                    "Not enough product on warehouse. Required quantity: " + newSale.getQuantity() +
                            ". Quantity in warehouse: " + warehouseQuantity);
        }

        Warehouse updatedWarehouse = warehouse.getProductById(newSale.getWarehouse().getId());
        updatedWarehouse.setQuantity(warehouseQuantity - newSale.getQuantity());
        warehouse.updateProduct(newSale.getWarehouse().getId(), updatedWarehouse);

        newSale.setSaleDate(LocalDateTime.now());
        newSale.setAmount(product.getAmount());
        return repository.save(newSale);
    }

    // Продать со скидкой процентом
    public Sale addNewSaleDiscountPercentage(Sale newSale, Integer percentage) {
        Sale discountedSale = addNewSale(newSale);
        return giveDiscountByPercentage(discountedSale.getId(), percentage);
    }

    // Продать со скидкой абсолютным значением
    public Sale addNewSaleDiscountAmount(Sale newSale, Integer amount) {
        Sale discountedSale = addNewSale(newSale);
        return giveDiscountOnAmount(discountedSale.getId(), amount);
    }
    // TODO:
    // Сделать проверку что в скидке на абсолютное значение, сумма не станет
    // отрицательной

    // Обновить продажу
    public Sale updateSale(Long saleId, Sale updatedSale) {
        Sale oldSale = repository.findById(saleId)
                .orElseThrow(() -> new EntityNotFoundException("Sale with id " + saleId + " is not found"));
        // Проверка на amount
        if (updatedSale.getAmount() == null) {
            updatedSale.setAmount(oldSale.getAmount());
        } else if (updatedSale.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Product's amount cannot be less than zero");
        }
        if (updatedSale.getWarehouse() == null) {
            updatedSale.setWarehouse(oldSale.getWarehouse());
        }
        // проверка на saledate
        if (updatedSale.getSaleDate() == null) {
            updatedSale.setSaleDate(oldSale.getSaleDate());
        }

        // Проверка на quantity
        if (updatedSale.getQuantity() == null || updatedSale.getQuantity().equals(oldSale.getQuantity())) {
            updatedSale.setQuantity(oldSale.getQuantity());
        } else {
            if (updatedSale.getQuantity() < 1) {
                throw new ForbiddenRequestException("Quantity can't be less than one");
            }
            Integer difference = updatedSale.getQuantity() - oldSale.getQuantity();
            Warehouse updatedWarehouse = oldSale.getWarehouse();
            if (difference < 0) {
                updatedWarehouse.setQuantity(updatedWarehouse.getQuantity() - difference);
                warehouse.updateProduct(updatedWarehouse.getId(), updatedWarehouse);
            } else if (difference <= updatedWarehouse.getQuantity()) {
                updatedWarehouse.setQuantity(updatedWarehouse.getQuantity() - difference);
                warehouse.updateProduct(updatedWarehouse.getId(), updatedWarehouse);
            } else {
                throw new ForbiddenRequestException(
                        "Not enough product on warehouse. Required quantity: " + difference +
                                ". Quantity in warehouse: " + updatedWarehouse.getQuantity());
            }
        }

        updatedSale.setId(saleId);
        return repository.save(updatedSale);
    }

    // Удалить продажу
    public void deleteSale(Long saleId) {
        repository.findById(saleId)
                .orElseThrow(() -> new EntityNotFoundException("Sale with id " + saleId + " is not found"));
        repository.deleteById(saleId);
    }

    // Put Дать скидку процентом
    public Sale giveDiscountByPercentage(Long saleId, Integer percentage) {
        Sale response = repository.findById(saleId)
                .orElseThrow(() -> new EntityNotFoundException("Sale with id " + saleId + " is not found"));
        Sale discountSale = response;

        BigDecimal discount = BigDecimal.valueOf(percentage).divide(BigDecimal.valueOf(100));
        BigDecimal currentPrice = discountSale.getAmount();
        BigDecimal discountAmount = currentPrice.multiply(discount);
        BigDecimal newPrice = currentPrice.subtract(discountAmount);

        discountSale.setAmount(newPrice);
        return repository.save(discountSale);
    }

    // Put Дать скидку абсолютным значением
    public Sale giveDiscountOnAmount(Long saleId, Integer amount) {
        Sale response = repository.findById(saleId)
                .orElseThrow(() -> new EntityNotFoundException("Sale with id " + saleId + " is not found"));
        Sale discountSale = response;

        BigDecimal currentPrice = discountSale.getAmount();
        BigDecimal discountAmount = BigDecimal.valueOf(amount);
        BigDecimal newPrice = currentPrice.subtract(discountAmount);

        discountSale.setAmount(newPrice);
        return repository.save(discountSale);
    }

}
