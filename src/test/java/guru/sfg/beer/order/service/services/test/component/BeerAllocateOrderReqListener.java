package guru.sfg.beer.order.service.services.test.component;

import guru.sfg.beer.order.service.config.JmsConfiguration;
import guru.sfg.brewery.model.event.AllocateOrderRequest;
import guru.sfg.brewery.model.event.AllocateOrderResult;
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
public class BeerAllocateOrderReqListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfiguration.ALLOCATE_ORDER_QUEUE)
    public void listen(AllocateOrderRequest allocateOrderRequest) {

        System.out.println("#################### I RAN TOO ########################");

        jmsTemplate.convertAndSend(JmsConfiguration.ALLOCATE_ORDER_RESPONSE_QUEUE, AllocateOrderResult.builder()
                .allocationError(false)
                .pendingInventory(false)
                .beerOrderDto(allocateOrderRequest.getBeerOrderDto())
                .build());
    }
}
