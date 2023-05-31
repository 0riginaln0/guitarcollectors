package com.example.guitarcollectors.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.guitarcollectors.exception.ForbiddenRequestException;
import com.example.guitarcollectors.exception.MyEntityNotFoundException;
import com.example.guitarcollectors.model.Sale;
import com.example.guitarcollectors.model.Warehouse;
import com.example.guitarcollectors.repository.SaleRepository;

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

    private Sale findById(Long saleId) {
        return repository.findById(saleId)
                .orElseThrow(() -> new MyEntityNotFoundException("Sale with id " + saleId + " is not found"));
    }

    // Показать продажу по id
    public Sale getSaleItemById(Long saleId) {
        Sale response = findById(saleId);
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

    // Обновить продажу
    public Sale updateSale(Long saleId, Sale updatedSale) {
        findById(saleId);
        updatedSale.setId(saleId);
        return repository.save(updatedSale);
    }

    // Удалить продажу
    public void deleteSale(Long saleId) {
        findById(saleId);
        repository.deleteById(saleId);
    }

    // Put Дать скидку процентом
    public Sale giveDiscountByPercentage(Long saleId, Integer percentage) {
        Sale response = findById(saleId);
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
        Sale response = findById(saleId);
        Sale discountSale = response;

        BigDecimal currentPrice = discountSale.getAmount();
        BigDecimal discountAmount = BigDecimal.valueOf(amount);
        BigDecimal newPrice = currentPrice.subtract(discountAmount);

        discountSale.setAmount(newPrice);
        return repository.save(discountSale);
    }

}
