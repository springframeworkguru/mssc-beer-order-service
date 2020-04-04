package guru.sfg.beer.order.service.services.beer;

import guru.sfg.beer.order.service.services.beer.model.BeerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by jeffreymzd on 3/19/20
 */
@Slf4j
@Component
public class BeerServiceImpl implements BeerService {

    private RestTemplate restTemplate;
    public static final String BEER_BY_ID_PATH = "/api/v1/beer/";
    public static final String BEER_BY_UPC_PATH = "/api/v1/beerUpc/";

    @Value("${sfg.brewery.beer.service.host}")
    private String beerServiceHost;

    public BeerServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    @Override
    public Optional<BeerDto> getBeerInfoById(UUID beerId) {

        log.info("Calling Beer Service By ID");

        String url = beerServiceHost + BEER_BY_ID_PATH + beerId.toString();
        String urlWithParams = UriComponentsBuilder.fromUriString(url).queryParam("showInventoryOnHand", true).build().toString();

        return Optional.of(restTemplate.getForObject(urlWithParams, BeerDto.class));
    }

    @Override
    public Optional<BeerDto> getBeerInfoByUpc(String upc) {

        log.info("Calling Beer Service By UPC");

        String url = beerServiceHost + BEER_BY_UPC_PATH + upc;
        String urlWithParams = UriComponentsBuilder.fromUriString(url).queryParam("showInventoryOnHand", true).build().toString();

        return Optional.of(restTemplate.getForObject(urlWithParams, BeerDto.class));
    }
}
