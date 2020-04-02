package guru.sfg.beer.order.service.services.listener;

import guru.sfg.beer.order.service.config.JmsConfiguration;
import guru.sfg.beer.order.service.services.BeerOrderManager;
import guru.sfg.brewery.model.event.ValidateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by jeffreymzd on 4/1/20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderValidationResultListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JmsConfiguration.VALIDATE_ORDER_RESPONSE_QUEUE)
    public void listen(ValidateOrderResult validateOrderResult) {
        final UUID orderId = validateOrderResult.getBeerOrderId();
        final boolean isValid = validateOrderResult.getIsValid();

        log.info("OnValidationResponse: BeerOrder={}, isValid={}", orderId, isValid);

        beerOrderManager.processValidationResult(orderId, isValid);
    }

}
