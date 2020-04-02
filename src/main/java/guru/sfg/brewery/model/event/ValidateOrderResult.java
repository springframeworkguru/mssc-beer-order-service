package guru.sfg.brewery.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Created by jeffreymzd on 4/1/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidateOrderResult {

    private UUID beerOrderId;
    private Boolean isValid = false;
}
