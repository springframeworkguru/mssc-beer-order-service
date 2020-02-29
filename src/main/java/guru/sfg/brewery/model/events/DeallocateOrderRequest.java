package guru.sfg.brewery.model.events;

import guru.sfg.brewery.model.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jt on 2/29/20.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeallocateOrderRequest {
    private BeerOrderDto beerOrderDto;
}
