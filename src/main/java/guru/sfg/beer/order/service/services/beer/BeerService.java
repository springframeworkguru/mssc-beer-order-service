package guru.sfg.beer.order.service.services.beer;

import guru.sfg.beer.order.service.services.beer.model.BeerDto;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by jeffreymzd on 3/19/20
 */
public interface BeerService {

    Optional<BeerDto> getBeerInfoById(UUID beerId);

    Optional<BeerDto> getBeerInfoByUpc(String upc);
}
