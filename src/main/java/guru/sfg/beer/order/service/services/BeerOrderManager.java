package guru.sfg.beer.order.service.services;

import guru.sfg.beer.order.service.domain.BeerOrder;

/**
 * Created by jeffreymzd on 3/30/20
 */
public interface BeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);
}
