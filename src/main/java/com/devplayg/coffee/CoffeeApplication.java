package com.devplayg.coffee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

//    @PostConstruct
//    public void initApp() {
//        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
//    }


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

//    @Autowired
//    private UserRepository personRepository;
//    @Autowired
//    private AddressRepository addressRepository;
//
//    @PostConstruct
//    private void initializeData() {
//        // Create John
//        final User john = new User("John");
//        personRepository.save(john);
//        final Address addressOne = new Address("Fake Street 1", "Spain", john);
//        addressRepository.save(addressOne);
//        // Create Lisa
//        final User lisa = new User("Lisa");
//        personRepository.save(lisa);
//        final Address addressTwo = new Address("Real Street 1", "Germany", lisa);
//        addressRepository.save(addressTwo);
//    }
}


