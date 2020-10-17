package guru.sfg.beer.order.service.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BeerDto
{
    private UUID id;
    private Integer version;

    @JsonFormat(shape = JsonFormat.Shape.STRING)//, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private OffsetDateTime createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING)//, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private OffsetDateTime lastModifiedDate;

    private String beerName;

    private String beerStyle;

    private String upc;

    private Integer quantityOnHand;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;
}
