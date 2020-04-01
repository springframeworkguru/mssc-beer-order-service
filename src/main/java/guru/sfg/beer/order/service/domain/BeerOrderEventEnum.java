package guru.sfg.beer.order.service.domain;

/**
 * Created by jeffreymzd on 3/30/20
 */
public enum BeerOrderEventEnum {
    VALIDATE_ORDER, VALIDATION_PASSED, VALIDATION_FAILED, ALLOCATE_ORDER,
    ALLOCATION_SUCCESS, ALLOCATION_NO_INVENTORY, ALLOCATION_FAILED,
    BEER_ORDER_PICKED_UP, CANCEL_ORDER
}
