package guru.sfg.beer.order.service.services.beer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by jeffreymzd on 3/19/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDto {

    private UUID id;
    private String beerName;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    private String beerStyle;
}
