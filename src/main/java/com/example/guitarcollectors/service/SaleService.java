package com.example.guitarcollectors.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.guitarcollectors.model.Sale;
import com.example.guitarcollectors.model.Warehouse;
import com.example.guitarcollectors.repository.SaleRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SaleService {
    private final SaleRepository repository;
    private final WarehouseService warehouse;

    public List<Sale> getAllSales() {
        return (List<Sale>) repository.findAll();
    }

    public Sale getSaleItemById(Long saleId) {
        Optional<Sale> response = repository.findById(saleId);
        if (response.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return response.get();
    }

    public Sale addNewSale(Sale newSale) {
        Warehouse product = warehouse.getProductById(newSale.getWarehouse().getId());
        Integer warehouseQuantity = product.getQuantity();
        if (warehouseQuantity.equals(0)) {
            throw new IllegalArgumentException();
        }
        Warehouse updatedWarehouse = product;
        updatedWarehouse.setQuantity(warehouseQuantity - 1);
        newSale.setAmount(product.getAmount());
        warehouse.updateProduct(newSale.getWarehouse().getId(), updatedWarehouse);
        return repository.save(newSale);
    }

    public Sale addNewSale(Sale newSale, Integer quantity) {
        Warehouse product = warehouse.getProductById(newSale.getWarehouse().getId());
        Integer warehouseQuantity = product.getQuantity();
        if (warehouseQuantity.compareTo(quantity) < 0) {
            throw new IllegalArgumentException();
        }
        newSale.setAmount(product.getAmount());
        Warehouse updatedWarehouse = warehouse.getProductById(newSale.getWarehouse().getId());
        updatedWarehouse.setQuantity(warehouseQuantity - quantity);
        warehouse.updateProduct(newSale.getWarehouse().getId(), updatedWarehouse);
        return repository.save(newSale);
    }

    public Sale updateSale(Long saleId, Sale updatedSale) {
        Optional<Sale> response = repository.findById(saleId);
        if (response.isEmpty()) {
            throw new IllegalArgumentException();
        }
        updatedSale.setId(saleId);
        return repository.save(updatedSale);
    }

    public void deleteSale(Long saleId) {
        repository.deleteById(saleId);
    }

}
