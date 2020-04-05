package guru.sfg.beer.order.service.sm.actions;

import guru.sfg.beer.order.service.config.JmsConfiguration;
import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.web.mappers.BeerOrderMapper;
import guru.sfg.brewery.model.event.DeallocateOrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;

import static guru.sfg.beer.order.service.services.BeerOrderManagerImpl.BEER_ORDER_HEADER;

/**
 * Created by jeffreymzd on 4/4/20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DeallocateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final JmsTemplate jmsTemplate;

    @Transactional
    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> stateContext) {

        BeerOrder beerOrder = beerOrderRepository.getOne((UUID) stateContext.getMessageHeader(BEER_ORDER_HEADER));

        log.debug("OnDeallocateOrderAction, Order Id: {}", stateContext.getMessageHeader(BEER_ORDER_HEADER));

        jmsTemplate.convertAndSend(JmsConfiguration.DEALLOCATE_ORDER_QUEUE,
                DeallocateOrderEvent.builder()
                .beerOrderDto(beerOrderMapper.beerOrderToDto(beerOrder))
                .build());

        log.info("Sent de-allocation request for order: " + beerOrder.getId());
    }
}
