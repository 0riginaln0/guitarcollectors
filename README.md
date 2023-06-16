# Курсовая Spring Application

![Untitled](%D0%9A%D1%83%D1%80%D1%81%D0%BE%D0%B2%D0%B0%D1%8F%20Spring%20Application%20e5458ebf206545d290353d0ea792c9e6/Untitled.png)

![Untitled](%D0%9A%D1%83%D1%80%D1%81%D0%BE%D0%B2%D0%B0%D1%8F%20Spring%20Application%20e5458ebf206545d290353d0ea792c9e6/Untitled%201.png)

Ссылки на гитхаб: сервер, клиент

[GitHub - 0riginaln0/guitarcollectors: Java Spring course work](https://github.com/0riginaln0/guitarcollectors)

[GitHub - 0riginaln0/GuitarcollectorsClient: GUI for Java backend](https://github.com/0riginaln0/GuitarcollectorsClient)

# Обзор

## Server

- **Properties**

`application.properties`

```java
spring.config.import=optional:secrets.properties

spring.datasource.url=jdbc:postgresql://localhost:5432/guitarcollectors
spring.datasource.driver-class-name= org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.check_nullability=true

spring.output.ansi.enabled=always
spring.jpa.open-in-view=true
spring.jpa.properties.jakarta.persistence.sharedCache.mode=ALL
```

Про *secrets.properties* в секции ****Hiding passwords**

- **Dependencies**

`build.gradle`

```groovy
plugins {
	id 'java'
	id 'org.springframework.boot' version '3.0.6'
	id 'io.spring.dependency-management' version '1.1.0'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '17'

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-actuator'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-security'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	runtimeOnly 'org.postgresql:postgresql'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testImplementation 'org.springframework.security:spring-security-test'
	compileOnly 'org.projectlombok:lombok:1.18.20'
	annotationProcessor 'org.projectlombok:lombok:1.18.20'
	annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
	implementation 'io.jsonwebtoken:jjwt-api:0.11.5'
	runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.11.5'
	runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.11.5'
}

tasks.named('test') {
	useJUnitPlatform()
}
```

- **Entities & Database**

`Charge.java`

```java
// Таблица расходов
@Entity
@Data
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

`ExpenseItem.java`

```java
// Таблица статей расходов
@Entity
@Data
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

`Sale.java`

```java
// Таблица продаж
@Entity
@Data
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

`Warehouse.java`

```java
// Таблица склада товаров
@Entity
@Data
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

***ERM:***

![Untitled](%D0%9A%D1%83%D1%80%D1%81%D0%BE%D0%B2%D0%B0%D1%8F%20Spring%20Application%20e5458ebf206545d290353d0ea792c9e6/Untitled%202.png)

- **Controllers, Services, Repositories**

Все репозитории расширяют `CrudRepository`

Методы в контроллерах возвращяют `ResponseEntity`

Суммарно у всех контроллеров получилось 34 запроса (эндпоинта) 7 из которых взаимодействуют с несколькими таблицами базы данных.

- Запросы, взаимодействующие с несколькими таблицами
    
    `addRepurchasedProduct` Warehouse.java *(ExpenseItems, Charges, Warehouse)*
    
    `getSalesForProductId` Warehouse.java *(Sales, Warehouse)*
    
    `getChargesForExpenseItem` ExpenseItem.java *(Charges, ExpenseItems)*
    
    `updateSale` Sales.java *(Sales, Warehouse)*
    
    `addNewSale` Sales.java *(Sales, Warehouse)*
    
    `addNewSaleDiscountPercentage` Sales.java *(Sales, Warehouse)*
    
    `addNewSaleDiscountAmount` Sales.java *(Sales, Warehouse)*
    

- Exception handling

Осуществляется с помощью обработчиков исключений.

![Untitled](%D0%9A%D1%83%D1%80%D1%81%D0%BE%D0%B2%D0%B0%D1%8F%20Spring%20Application%20e5458ebf206545d290353d0ea792c9e6/Untitled%203.png)

`ControllerLayerAdvice.java`

```java
@Slf4j
@ControllerAdvice
public class ControllerLayerAdvice {
    @ResponseBody
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String badRequestHandler(BadRequestException ex) {
        log.error("\n" + ex.getMessage()
                + "\n    file name: " + ex.getStackTrace()[0].getFileName()
                + "\n   class name: " + ex.getStackTrace()[0].getClassName()
                + "\n  method name: " + ex.getStackTrace()[0].getMethodName()
                + "\n  line number: " + ex.getStackTrace()[0].getLineNumber());
        return ex.getMessage();
    }
}
```

`ServiceLayerAdvice.java`

```java
@Slf4j
@ControllerAdvice
public class ServiceLayerAdvice {
    @ResponseBody
    @ExceptionHandler(ForbiddenRequestException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String forbiddenRequestHandler(ForbiddenRequestException ex) {
        log.error("\n" + ex.getMessage()
                + "\n    file name: " + ex.getStackTrace()[0].getFileName()
                + "\n   class name: " + ex.getStackTrace()[0].getClassName()
                + "\n  method name: " + ex.getStackTrace()[0].getMethodName()
                + "\n  line number: " + ex.getStackTrace()[0].getLineNumber());
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String myEntityNotFoundHandler(EntityNotFoundException ex) {
        log.error("\n" + ex.getMessage()
                + "\n    file name: " + ex.getStackTrace()[0].getFileName()
                + "\n   class name: " + ex.getStackTrace()[0].getClassName()
                + "\n  method name: " + ex.getStackTrace()[0].getMethodName()
                + "\n  line number: " + ex.getStackTrace()[0].getLineNumber());
        return ex.getMessage();
    }
}
```

- **Security, Configuration**

Авторизация, ограничение выполнения запросов неавторизированными пользователями только запросами на выборку данных

`SecurityConfig.java`

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider AuthenticationProvider;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "api/warehouse/**", "api/expense-items/**", "api/sales/**",
                        "api/charges/**")
                .permitAll()
                .requestMatchers("api/warehouse/**", "api/expense-items/**", "api/sales/**",
                        "api/charges/**")
                .hasAuthority("MANAGER")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(AuthenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
```

- **JWT & Authentication**

![Untitled](%D0%9A%D1%83%D1%80%D1%81%D0%BE%D0%B2%D0%B0%D1%8F%20Spring%20Application%20e5458ebf206545d290353d0ea792c9e6/Untitled%204.png)

Всё начинается когда клиент отправляет HTTP request на сервер

Первая вещь которая запускается при начале работе spring application - это Filter.

В нашем случае запустится JwtAuthFilter. Он отвечает за валидацию токена и все операции с ними. 

Изначально мы проверяем,  существует ли токен, который отправили в запросе.

Фильтр сделает вызов к базе данных используя UserDetailsService чтобы достать информацию о пользователя из базы данных и сопоставить с тем, что представлено в запросе. Берётся email из запроса, и ищется в базе, есть ли такой пользователь.

Если пользователь есть, начинается процесс валидации токена. Validate JWT обращается к JwtService с данными email, password, token. 

Если токен просрочен или пренадлежит другому пользователю, то отправляем Invalid Jwt Token

Если с токеном всё в порядке, мы обновляем SecurityContextHolder. Пользователь аутентифицировался. После этого происходит то, что было запрошено. 

Схема классов, реализующих использование JWT:

![JwtScheme.drawio.png](%D0%9A%D1%83%D1%80%D1%81%D0%BE%D0%B2%D0%B0%D1%8F%20Spring%20Application%20e5458ebf206545d290353d0ea792c9e6/JwtScheme.drawio.png)

- **Hiding passwords**

Включаю возможность конфигурации свойств через spring-boot-configuration-processor

`GuitarcollectorsApplication.java`

```java
@SpringBootApplication
@EnableConfigurationProperties(JwtConfigProperties.class)
public class GuitarcollectorsApplication {
	public static void main(String[] args) {
		SpringApplication.run(GuitarcollectorsApplication.class, args);
	}
}
```

Класс свойства конфигурации `JwtConfigProperties.java`

```java
@ConfigurationProperties("jwt")
public record JwtConfigProperties(String secretKey) {

}
```

Используем его в `JwtService.java`

```java
@Service
public class JwtService {

    private final JwtConfigProperties JWT_CONFIG_PROPERTIES;

    public JwtService(JwtConfigProperties jwtConfigProperties) {
        JWT_CONFIG_PROPERTIES = jwtConfigProperties;
    }
...

private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_CONFIG_PROPERTIES.secretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
```

Создаю файл *`secrets.properties`*

```java
jwt.secret-key=extraClassSecretKey

spring.datasource.username=verygoodusername
spring.datasource.password=verystrongpassword
```

В `application.properties` прописываю импорт:

```java
spring.config.import=optional:secrets.properties
```

В *gitignore* пишем путь до *secrets.properties* и не светим секретами в гите

## Client

Выбрал для создания приложения движок Godot. В нём уже есть заготовленные ноды для UI и осуществления HTTP запросов.

Каждый “новое окно/меню” - отдельная сцена. Каждая сцена может содержать ноды. Вся логика: переключение между сценами, отправка запросов по нажатию кнопки, запись JWT токена - осуществляется скриптами на языке GDScript.

***Струкрура приложения:***

![asdasd.drawio.png](%D0%9A%D1%83%D1%80%D1%81%D0%BE%D0%B2%D0%B0%D1%8F%20Spring%20Application%20e5458ebf206545d290353d0ea792c9e6/asdasd.drawio.png)

***Скриншоты:***

`StartWindow`

![Untitled](%D0%9A%D1%83%D1%80%D1%81%D0%BE%D0%B2%D0%B0%D1%8F%20Spring%20Application%20e5458ebf206545d290353d0ea792c9e6/Untitled%205.png)

`AuthenticationWindow`                                                 `ChooseCategory`

![Untitled](%D0%9A%D1%83%D1%80%D1%81%D0%BE%D0%B2%D0%B0%D1%8F%20Spring%20Application%20e5458ebf206545d290353d0ea792c9e6/Untitled%206.png)

![Untitled](%D0%9A%D1%83%D1%80%D1%81%D0%BE%D0%B2%D0%B0%D1%8F%20Spring%20Application%20e5458ebf206545d290353d0ea792c9e6/Untitled%207.png)

`Warehouse` GET/All products pressed                          `ExpenseItems` POST/Expense item

![Untitled](%D0%9A%D1%83%D1%80%D1%81%D0%BE%D0%B2%D0%B0%D1%8F%20Spring%20Application%20e5458ebf206545d290353d0ea792c9e6/Untitled%208.png)

![Untitled](%D0%9A%D1%83%D1%80%D1%81%D0%BE%D0%B2%D0%B0%D1%8F%20Spring%20Application%20e5458ebf206545d290353d0ea792c9e6/Untitled%209.png)

`Sales` PUT/Update sale: sale id: 1, product id: 1        `Charges` DELETE/Delete charge: charge id: 11

![Untitled](%D0%9A%D1%83%D1%80%D1%81%D0%BE%D0%B2%D0%B0%D1%8F%20Spring%20Application%20e5458ebf206545d290353d0ea792c9e6/Untitled%2010.png)

![Untitled](%D0%9A%D1%83%D1%80%D1%81%D0%BE%D0%B2%D0%B0%D1%8F%20Spring%20Application%20e5458ebf206545d290353d0ea792c9e6/Untitled%2011.png)