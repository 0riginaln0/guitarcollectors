package com.example.guitarcollectors.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.guitarcollectors.exception.ForbiddenRequestException;
import com.example.guitarcollectors.model.Charge;
import com.example.guitarcollectors.model.ExpenseItem;
import com.example.guitarcollectors.model.Sale;
import com.example.guitarcollectors.model.Warehouse;
import com.example.guitarcollectors.repository.WarehouseRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WarehouseService {
    private final WarehouseRepository repository;
    private final ChargeService chargeService;
    private final ExpenseItemService expenseItemService;
    private final Comparator<Warehouse> amountAscendingComparator;
    private final Comparator<Warehouse> amountDescendingComparator;

    // TODO:
    // Показать товары в наличии через query в repository

    // Показать все товары
    public List<Warehouse> getAllProducts() {
        return (List<Warehouse>) repository.findAll();
    }

    // Показать товар по id
    public Warehouse getProductById(Long productId) {
        Warehouse response = repository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("Product with id "
                        + productId + " is not found"));
        return response;
    }

    // Добавить товар
    public Warehouse addNewProduct(Warehouse newProduct) {
        return repository.save(newProduct);
    }

    // Обновить товар
    public Warehouse updateProduct(Long productId, Warehouse updatedProduct) {
        repository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("Product with id "
                        + productId + " is not found"));
        updatedProduct.setId(productId);
        return repository.save(updatedProduct);
    }

    // Удалить товар
    public void deleteProduct(Long productId) {
        List<Sale> sales = getSalesForProductId(productId);
        if (sales.isEmpty()) {
            repository.deleteById(productId);
        } else {
            throw new ForbiddenRequestException(
                    "Cannot delete product with id " + productId + " because it's being used.");
        }
    }

    // Показать товары в наличии
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

    // Количество товаров на складе
    public Integer getAllProductsQuantity() {
        List<Warehouse> list = getAllProducts();
        Integer quantity = 0;
        for (Warehouse product : list) {
            quantity += product.getQuantity();
        }
        return quantity;
    }

    // Количество товара на складе
    public Integer getProductQuantity(Long id) {
        Warehouse warehouse = getProductById(id);
        return warehouse.getQuantity();
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

    // Показать все товары, по названию
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

    // Показать товары по цене до {price} и сортированном по убыванию виде
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

    // Показать товары по цене от {price} в сортированном по возростанию виде
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

    // Показать продажи определённого товара
    public List<Sale> getSalesForProductId(Long productId) {
        Warehouse products = repository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("Product with id "
                        + productId + " is not found"));
        return products.getSales();
    }

    // Выкупить гитару + автоматически выставить её на продажу с выбранной наценкой
    // (ExpenseItems -> Charges -> Warehouse)
    public Warehouse addRepurchasedProduct(Warehouse newProduct, BigDecimal margin) {
        ExpenseItem repurchaseExpenseItem = expenseItemService.getExpenseItemById((long) 9);
        Charge repurchaseCharge = new Charge(newProduct.getAmount(), LocalDateTime.now(), repurchaseExpenseItem);
        chargeService.addNewCharge(repurchaseCharge);

        BigDecimal marginAmount = newProduct.getAmount().multiply(margin).divide(new BigDecimal(100));
        BigDecimal sellingPrice = newProduct.getAmount().add(marginAmount);
        sellingPrice = sellingPrice.divide(new BigDecimal(1000), 0, RoundingMode.UP).multiply(new BigDecimal(1000));

        newProduct.setAmount(sellingPrice);
        return repository.save(newProduct);
    }
}
