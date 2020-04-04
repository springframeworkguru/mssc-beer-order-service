package guru.sfg.beer.order.service.sm.actions;

import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.brewery.model.event.AllocationFailureEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static guru.sfg.beer.order.service.config.JmsConfiguration.ALLOCATE_ORDER_FAILURE_QUEUE;
import static guru.sfg.beer.order.service.services.BeerOrderManagerImpl.BEER_ORDER_HEADER;

/**
 * Created by jeffreymzd on 4/4/20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AllocationFailureAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final JmsTemplate jmsTemplate;
    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> stateContext) {

        UUID orderId = (UUID) stateContext.getMessageHeader(BEER_ORDER_HEADER);
        log.debug("Compensating transaction .......... OnAllocationFailureAction: Id: {}", orderId);

        jmsTemplate.convertAndSend(ALLOCATE_ORDER_FAILURE_QUEUE,
                AllocationFailureEvent.builder()
                        .orderId(orderId)
                        .build());
    }
}
