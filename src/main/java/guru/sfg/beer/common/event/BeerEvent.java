package guru.sfg.beer.common.event;

import guru.sfg.beer.common.model.BeerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by jeffreymzd on 3/23/20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeerEvent implements Serializable {

    private static final long serialVersionUID = 1935424428211105886L;
    private BeerDto beerDto;
}
