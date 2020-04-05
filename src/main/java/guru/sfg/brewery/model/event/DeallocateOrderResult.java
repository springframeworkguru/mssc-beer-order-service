package guru.sfg.brewery.model.event;

import guru.sfg.brewery.model.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jeffreymzd on 4/4/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeallocateOrderResult {
    private BeerOrderDto beerOrderDto;
}
