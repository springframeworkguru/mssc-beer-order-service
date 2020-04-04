package guru.sfg.beer.order.service.services.listener;

import guru.sfg.beer.order.service.config.JmsConfiguration;
import guru.sfg.beer.order.service.services.BeerOrderManager;
import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.event.AllocateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by jeffreymzd on 4/2/20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderAllocationResultListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JmsConfiguration.ALLOCATE_ORDER_RESPONSE_QUEUE)
    public void listen(AllocateOrderResult allocateOrderResult) {
        log.info("Receive allocation result: {}", allocateOrderResult);
        BeerOrderDto beerOrderDto = allocateOrderResult.getBeerOrderDto();
        final UUID beerOrderId = beerOrderDto.getId();
        final boolean allocationError = allocateOrderResult.getAllocationError();
        final boolean pendingInventory = allocateOrderResult.getPendingInventory();

        log.info("OnAllocationResponse: BeerOrder={}, allocationError={}, pendingInventory={}",
                beerOrderId, allocationError, pendingInventory);

        if (allocationError)
            beerOrderManager.beerOrderAllocationError(beerOrderDto);
        else if (pendingInventory)
            beerOrderManager.beerOrderPendingInventory(beerOrderDto);
        else
            beerOrderManager.beerOrderAllocationSuccess(beerOrderDto);
    }
}
