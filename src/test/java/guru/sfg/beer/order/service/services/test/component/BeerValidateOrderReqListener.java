package guru.sfg.beer.order.service.services.test.component;

import guru.sfg.beer.order.service.config.JmsConfiguration;
import guru.sfg.brewery.model.event.ValidateOrderRequest;
import guru.sfg.brewery.model.event.ValidateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by jeffreymzd on 4/3/20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BeerValidateOrderReqListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfiguration.VALIDATE_ORDER_QUEUE)
    public void listen(ValidateOrderRequest validateOrderRequest) {

        System.out.println("#################### I RAN ########################");

        jmsTemplate.convertAndSend(JmsConfiguration.VALIDATE_ORDER_RESPONSE_QUEUE, ValidateOrderResult.builder()
                .isValid(true)
                .beerOrderId(validateOrderRequest.getBeerOrderDto().getId())
                .build());

    }
}
