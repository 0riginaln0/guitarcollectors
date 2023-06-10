package com.example.guitarcollectors.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.guitarcollectors.model.Charge;
import com.example.guitarcollectors.model.ExpenseItem;
import com.example.guitarcollectors.model.Sale;
import com.example.guitarcollectors.model.Warehouse;
import com.example.guitarcollectors.repository.ChargeRepository;
import com.example.guitarcollectors.repository.ExpenseItemsRepository;
import com.example.guitarcollectors.repository.SaleRepository;
import com.example.guitarcollectors.repository.UserRepository;
import com.example.guitarcollectors.repository.WarehouseRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserRepository userRepository;

    @Bean
    public Comparator<Warehouse> amountAscendingComparator() {
        return new Comparator<Warehouse>() {
            public int compare(Warehouse w1, Warehouse w2) {
                return w1.getAmount().compareTo(w2.getAmount());
            }
        };
    }

    @Bean
    public Comparator<Warehouse> amountDescendingComparator() {
        return new Comparator<Warehouse>() {
            public int compare(Warehouse w1, Warehouse w2) {
                return w2.getAmount().compareTo(w1.getAmount());
            }
        };
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvidervider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // CommandLineRunner fillUpCharges(ExpenseItemsRepository
    // expenseItemsRepository, ChargeRepository chargeRepository,
    // SaleRepository saleRepository, WarehouseRepository warehouseRepository,
    // DataSource dataSource) {
    // return arge -> {
    // ResourceDatabasePopulator resourceDatabasePopulator = new
    // ResourceDatabasePopulator(false, false, "UTF-8",
    // new ClassPathResource(
    // "Guitarcollectors.session.sql"));
    // resourceDatabasePopulator.execute(dataSource);

    // List<Charge> chargeList = new ArrayList<Charge>();
    // List<ExpenseItem> expenseItemsList = (List<ExpenseItem>)
    // expenseItemsRepository.findAll();
    // for (ExpenseItem expenseItem : expenseItemsList) {
    // Charge charge = new Charge();
    // Integer amount = (int) ((Math.random() * (10000 - 1000)) + 1000);
    // charge.setAmount(new BigDecimal(amount));
    // charge.setChargeDate(LocalDateTime.now());
    // charge.setExpenseItem(expenseItem);
    // chargeList.add(charge);
    // }
    // chargeRepository.saveAll(chargeList);

    // List<Sale> saleList = new ArrayList<Sale>();
    // Sale newSale1 = new Sale();
    // Warehouse warehouse1 = warehouseRepository.findById((long) 1).get();
    // newSale1.setWarehouse(warehouse1);
    // newSale1.setAmount(warehouse1.getAmount());
    // newSale1.setQuantity(1);
    // newSale1.setSaleDate(LocalDateTime.now());

    // Sale newSale2 = new Sale();
    // Warehouse warehouse2 = warehouseRepository.findById((long) 2).get();
    // newSale2.setWarehouse(warehouse2);
    // newSale2.setAmount(warehouse2.getAmount());
    // newSale2.setQuantity(1);
    // newSale2.setSaleDate(LocalDateTime.now());

    // saleList.add(newSale1);
    // saleList.add(newSale2);

    // saleRepository.saveAll(saleList);
    // };
    // }

}