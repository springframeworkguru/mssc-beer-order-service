package guru.sfg.brewery.model.event;

import guru.sfg.brewery.model.BeerDto;
import lombok.NoArgsConstructor;

/**
 * Created by jeffreymzd on 3/23/20
 */
@NoArgsConstructor
public class NewInventoryEvent extends BeerEvent {

    private static final long serialVersionUID = 2754018446273430878L;

    public NewInventoryEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
