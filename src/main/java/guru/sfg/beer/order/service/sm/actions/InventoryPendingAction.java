package guru.sfg.beer.order.service.sm.actions;

import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import static guru.sfg.beer.order.service.services.BeerOrderManagerImpl.BEER_ORDER_HEADER;

/**
 * Created by jeffreymzd on 4/4/20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class InventoryPendingAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {
    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> stateContext) {
        log.error("Compensating transaction .......... OnInventoryPendingAction: Id: {}",
                stateContext.getMessageHeader(BEER_ORDER_HEADER));
    }
}
