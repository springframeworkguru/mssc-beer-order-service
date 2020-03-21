package guru.sfg.beer.order.service.services.beer;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

/**
 * Created by jeffreymzd on 3/19/20
 */
@Disabled
@SpringBootTest
class BeerServiceImplTest {

    @Autowired
    BeerService beerService;

    @Test
    void getBeerInfo() {
        System.out.println(beerService.getBeerInfoById(UUID.fromString("0a818933-087d-47f2-ad83-2f986ed087eb")));;
    }

    @Test
    void getBeerInfoByUpc() {
        System.out.println(beerService.getBeerInfoByUpc("0631234200036"));;
    }
}