# guitarcollectors
 Java Spring course work
# Курсовая работа Рыженко Дмитрия Александровича.
**Вариант 8: Автоматизация работы магазина**

### Задание:
![[Pasted image 20230602133644.png]]
![[Pasted image 20230602133818.png]]
### Используемые база данных, сборщик проекта и Spring зависимости:
Gradle
Postgresql

```Java
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    //implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    runtimeOnly 'org.postgresql:postgresql'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    //testImplementation 'org.springframework.security:spring-security-test'
    compileOnly 'org.projectlombok:lombok:1.18.20'
    annotationProcessor 'org.projectlombok:lombok:1.18.20'
}
```

### Реализация базы данных:
Сгенерированная ERD:
![[Pasted image 20230602135711.png]]

Entities:
```Java
// Таблица расходов
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "charges")
public class Charge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Сумма
    @Column(nullable = false)
    private BigDecimal amount;

    // Дата
    @Column(name = "charge_date")
    private LocalDateTime chargeDate;

    // Статья расхода
    @ManyToOne
    @JoinColumn(name = "expense_item_id", nullable = false)
    @JsonIgnoreProperties("charges")
    private ExpenseItem expenseItem;

    public Charge(BigDecimal amount, LocalDateTime chargeDate, ExpenseItem expenseItem) {
        this.amount = amount;
        this.chargeDate = chargeDate;
        this.expenseItem = expenseItem;
    }
}
```

```Java
// Таблица статей расходов
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "expense_items")
public class ExpenseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Наименование статьи
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "expenseItem", cascade = CascadeType.ALL)
    @Column(nullable = false)
    private List<Charge> charges;

    public ExpenseItem(String name) {
        this.name = name;
    }
}
```

```Java
// Таблица продаж
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Стоимость одной единицы товара
    @Column(nullable = false)
    private BigDecimal amount;

    // Количество товара
    @Column(nullable = false)
    private Integer quantity;

    // Дата продажи
    @Column(name = "sale_date")
    private LocalDateTime saleDate;

    // Товар
    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    @JsonIgnoreProperties("sales")
    private Warehouse warehouse;

    public Sale(BigDecimal amount, Integer quantity, LocalDateTime saleDate, Warehouse warehouse) {
        this.amount = amount;
        this.quantity = quantity;
        this.saleDate = saleDate;
        this.warehouse = warehouse;
    }
}
```

```Java
// Таблица склада товаров
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Наименование
    @Column(nullable = false)
    private String name;

    // Количество товара
    @Column(nullable = false)
    private Integer quantity;

    // Стоимость одной единицы товара
    @Column(nullable = false)
    private BigDecimal amount;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    @Column(nullable = false)
    private List<Sale> Sales;

    public Warehouse(String name, Integer quantity, BigDecimal amount) {
        this.name = name;
        this.quantity = quantity;
        this.amount = amount;
    }
}
```


### Реализация запросов и эндпоинты:
WarehouseController
```Java
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
        validateProduct(updatedProduct);
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
```


















Expense Item
![[Pasted image 20230531150501.png]]
6 запросов, 1 использует несколько таблиц (`getChargesForExpenseItem`)

Charges
![[Pasted image 20230531152902.png]]
5 запросов

Warehouse
![[Pasted image 20230531153112.png]]
![[Pasted image 20230531153140.png]]
14 запросов, 2 использует несколько таблиц (`addRepurchasedProduct, getSalesForProductId`)

Sales
![[Pasted image 20230531153605.png]]
![[Pasted image 20230531153624.png]]
9 запросов, 3 используют несколько таблиц (`addNewSale, addNewSaleDiscount Amount/Percentage`)

Итого:

34 запросов, 6 взаимодействуют сразу с несколькими таблицами
