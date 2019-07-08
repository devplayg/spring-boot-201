package com.devplayg.coffee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.sql.Time;
import java.util.TimeZone;

/**
 * 현재는 Controller > Repository 구조
 * 일부는 Controller > Service > Repository 구조
 * 추후에는 Controller > Service > Predicate > Repository 구조
 *
 * QueryDSL 역할 확장 예정
 */

@SpringBootApplication
public class CoffeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffeeApplication.class, args);
    }

    @PostConstruct
    public void initApp() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
    }


//    @Bean
//    CommandLineRunner initialize(UserRepository userRepository) {
//        return args -> {
//            Stream.of("John", "Robert", "Nataly", "Helen", "Mary").forEach(name -> {
//                User user = new User(name);
//                userRepository.save(user);
//            });
//            userRepository.findAll().forEach(System.out::println);
//        };
//    }
}
