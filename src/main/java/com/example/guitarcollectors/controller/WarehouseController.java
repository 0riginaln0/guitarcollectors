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

import com.example.guitarcollectors.model.Warehouse;
import com.example.guitarcollectors.service.WarehouseService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController()
@RequestMapping("api/warehouse")
@AllArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;
    // TODO:
    // Разделить логику сортировки товаров и показа товаров по цене

    // Удалить можно только тот товар, который ещё ни разу не продали

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
    public void deleteProduct(@PathVariable Long productId) {
        warehouseService.deleteProduct(productId);
    }

    // Показать все товары, которые есть в наличии
    @GetMapping("/instock")
    public ResponseEntity<List<Warehouse>> getAllInStock() {
        return new ResponseEntity<>(warehouseService.getAllInStock(), HttpStatus.OK);
    }

    // Количество товаров на складе
    @GetMapping("/quantity")
    public ResponseEntity<Integer> getProductQuantity() {
        return new ResponseEntity<>(warehouseService.getAllProductsQuantity(), HttpStatus.OK);
    }

    // Средняя стоимость товара
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

}
