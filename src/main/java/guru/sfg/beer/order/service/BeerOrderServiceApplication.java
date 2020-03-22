package guru.sfg.beer.order.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;

@SpringBootApplication(exclude = ArtemisAutoConfiguration.class)
public class BeerOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeerOrderServiceApplication.class, args);
    }

}
