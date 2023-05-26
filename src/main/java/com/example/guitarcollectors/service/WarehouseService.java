package com.example.guitarcollectors.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.guitarcollectors.model.Warehouse;
import com.example.guitarcollectors.repository.WarehouseRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WarehouseService {
    private final WarehouseRepository repository;
    private final Comparator<Warehouse> amountAscendingComparator;
    private final Comparator<Warehouse> amountDescendingComparator;

    public List<Warehouse> getAllProducts() {
        return (List<Warehouse>) repository.findAll();
    }

    public Warehouse getProductById(Long productId) {
        Optional<Warehouse> response = repository.findById(productId);
        if (response.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return response.get();
    }

    // Средняя цена всех товаров
    public BigDecimal getAverageAmount() {
        List<Warehouse> list = (List<Warehouse>) repository.findAll();
        BigDecimal totalAmount = new BigDecimal(0);
        if (list.isEmpty()) {
            return totalAmount;
        }
        for (Warehouse guitar : list) {
            totalAmount = totalAmount.add(guitar.getAmount());
        }

        return totalAmount.divide(BigDecimal.valueOf(list.size()), 2, RoundingMode.HALF_UP);
    }

    public Warehouse addNewProduct(Warehouse newProduct) {
        return repository.save(newProduct);
    }

    public Warehouse updateProduct(Long productId, Warehouse updatedProduct) {
        Optional<Warehouse> response = repository.findById(productId);
        if (response.isEmpty()) {
            throw new IllegalArgumentException();
        }
        updatedProduct.setId(productId);
        return repository.save(updatedProduct);
    }

    public void deleteProduct(Long productId) {
        // To-Do
        // Сделать проверку, что если к товару привязана продажа, то удалять его нельзя
        repository.deleteById(productId);
    }

    public List<Warehouse> getAllInStock() {
        List<Warehouse> list = getAllProducts();
        List<Warehouse> outputList = new ArrayList<Warehouse>();
        for (Warehouse product : list) {
            if (product.getQuantity() > 0) {
                outputList.add(product);
            }
        }
        return outputList;
    }

    public List<Warehouse> getAllByName(String name) {
        List<Warehouse> list = getAllProducts();
        List<Warehouse> outputList = new ArrayList<Warehouse>();
        for (Warehouse product : list) {
            if (product.getName().contains(name)) {
                outputList.add(product);
            }
        }
        return outputList;
    }

    public List<Warehouse> getByPriceUpTo(BigDecimal price) {
        List<Warehouse> list = getAllProducts();
        List<Warehouse> outputList = new ArrayList<Warehouse>();
        for (Warehouse product : list) {
            if (product.getAmount().compareTo(price) <= 0) {
                outputList.add(product);
            }
        }
        Collections.sort(outputList, amountDescendingComparator);
        return outputList;
    }

    public List<Warehouse> getByPricefrom(BigDecimal price) {
        List<Warehouse> list = getAllProducts();
        List<Warehouse> outputList = new ArrayList<Warehouse>();
        for (Warehouse product : list) {
            if (product.getAmount().compareTo(price) >= 0) {
                outputList.add(product);
            }
        }
        Collections.sort(outputList, amountAscendingComparator);

        return outputList;
    }

    public Integer getProductQuantity() {
        List<Warehouse> list = getAllProducts();
        Integer quantity = 0;
        for (Warehouse product : list) {
            quantity += product.getQuantity();
        }
        return quantity;
    }
}
