package com.example.guitarcollectors.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.guitarcollectors.model.Sale;
import com.example.guitarcollectors.repository.SaleRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SaleService {
    private final SaleRepository repository;

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
