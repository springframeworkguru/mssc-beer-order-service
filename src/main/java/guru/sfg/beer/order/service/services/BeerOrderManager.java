package guru.sfg.beer.order.service.services;

import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.brewery.model.BeerOrderDto;

import java.util.UUID;

/**
 * Created by jeffreymzd on 3/30/20
 */
public interface BeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);
    void processValidationResult(UUID orderId, boolean isValid);
    void beerOrderAllocationSuccess(BeerOrderDto beerOrderDto);
    void beerOrderAllocationError(BeerOrderDto beerOrderDto);
    void beerOrderPendingInventory(BeerOrderDto beerOrderDto);
    void beerOrderPickedUp(UUID orderId);
    void beerOrderCancel(UUID orderId);
}
