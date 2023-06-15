package com.example.guitarcollectors.controller;

import java.math.BigDecimal;
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
import com.example.guitarcollectors.model.Warehouse;
import com.example.guitarcollectors.service.WarehouseService;

import lombok.AllArgsConstructor;

@RestController()
@RequestMapping("api/warehouse")
@AllArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

    // Показать все товары
    @GetMapping("/")
    public ResponseEntity<List<Warehouse>> getAllProducts() {
        return new ResponseEntity<>(warehouseService.getAllProducts(), HttpStatus.OK);
    }

    // Показать товар по id
    @GetMapping("/{productId}")
    public ResponseEntity<Warehouse> getProductById(@PathVariable Long productId) {
        return new ResponseEntity<Warehouse>(warehouseService.getProductById(productId), HttpStatus.OK);
    }

    // Добавить товар
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Warehouse> addNewProduct(@RequestBody Warehouse newProduct) {
        validateProduct(newProduct);
        Warehouse product = warehouseService.addNewProduct(newProduct);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    // Обновить товар
    @PutMapping(path = "/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Warehouse> updateExpenseItem(@PathVariable Long productId,
            @RequestBody Warehouse updatedProduct) {
        Warehouse product = warehouseService.updateProduct(productId, updatedProduct);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    // Удалить товар
    @DeleteMapping(path = "/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        warehouseService.deleteProduct(productId);
        return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
    }

    // Показать товары в наличии
    @GetMapping("/instock")
    public ResponseEntity<List<Warehouse>> getAllInStock() {
        return new ResponseEntity<>(warehouseService.getAllInStock(), HttpStatus.OK);
    }

    // Количество товаров на складе
    @GetMapping("/quantity")
    public ResponseEntity<Integer> getAllProductsQuantity() {
        return new ResponseEntity<>(warehouseService.getAllProductsQuantity(), HttpStatus.OK);
    }

    // Количество товара на складе
    @GetMapping("/quantity/{productId}")
    public ResponseEntity<Integer> getProductQuantity(@PathVariable Long productId) {
        return new ResponseEntity<>(warehouseService.getProductQuantity(productId), HttpStatus.OK);
    }

    // Средняя цена всех товаров
    @GetMapping("/average")
    public ResponseEntity<BigDecimal> getAverageAmount() {
        return new ResponseEntity<BigDecimal>(warehouseService.getAverageAmount(), HttpStatus.OK);
    }

    // Показать все товары, по названию
    @GetMapping("/name/{name}")
    public ResponseEntity<List<Warehouse>> getAllByName(@PathVariable String name) {
        return new ResponseEntity<>(warehouseService.getAllByName(name), HttpStatus.OK);
    }

    // Показать товары по цене до {price} и сортированном по убыванию виде
    @GetMapping("/upto/{price}")
    public ResponseEntity<List<Warehouse>> getByPriceUpTo(@PathVariable BigDecimal price) {
        return new ResponseEntity<>(warehouseService.getByPriceUpTo(price), HttpStatus.OK);
    }

    // Показать товары по цене от {price} в сортированном по возростанию виде
    @GetMapping("/from/{price}")
    public ResponseEntity<List<Warehouse>> getByPricefrom(@PathVariable BigDecimal price) {
        return new ResponseEntity<>(warehouseService.getByPricefrom(price), HttpStatus.OK);
    }

    // Показать продажи определённого товара
    @GetMapping("/sales/{productId}")
    public ResponseEntity<List<Sale>> getSalesForProductId(@PathVariable Long productId) {
        return new ResponseEntity<>(warehouseService.getSalesForProductId(productId), HttpStatus.OK);
    }

    // Выкупить гитару + автоматически выставить её на продажу с выбранной наценкой
    // (ExpenseItems -> Charges -> Warehouse)
    @PostMapping(path = "/repurchase/{margin}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Warehouse> addRepurchasedProduct(@RequestBody Warehouse newProduct,
            @PathVariable BigDecimal margin) {
        validateProduct(newProduct);
        validateMargin(margin);
        Warehouse product = warehouseService.addRepurchasedProduct(newProduct, margin);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    public void validateMargin(BigDecimal margin) {
        if (margin.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Margine can't be less than 0");
        }
    }

    public void validateProduct(Warehouse product) {
        if (product.getName() == null) {
            throw new BadRequestException("Product's name cannot be null");
        }
        if (product.getName().isEmpty()) {
            throw new BadRequestException("Product's name cannot be empty");
        }
        if (product.getQuantity() == null) {
            throw new BadRequestException("Product's quantity cannot be null");
        }
        if (product.getQuantity() < 1) {
            throw new BadRequestException("Product's quantity cannot be less than 1");
        }
        if (product.getAmount() == null) {
            throw new BadRequestException("Product's amount cannot be null");
        }
        if (product.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Product's amount cannot be less than zero");
        }
    }
}
