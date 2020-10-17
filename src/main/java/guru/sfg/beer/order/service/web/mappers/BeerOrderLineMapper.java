package guru.sfg.beer.order.service.web.mappers;

import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.beer.order.service.web.model.BeerOrderLineDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {DateMapper.class})
@DecoratedWith(BeerOrderLineMapperDecorator.class)
public interface BeerOrderLineMapper
{
    @Mapping(target = "beerName", ignore = true)
    @Mapping(target = "beerStyle", ignore = true)
    @Mapping(target = "price", ignore = true)
    BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line);

    @Mapping(target = "beerOrder", ignore = true)
    @Mapping(target = "quantityAllocated", ignore = true)
    BeerOrderLine dtoToBeerOrderLine(BeerOrderLineDto dto);
}
