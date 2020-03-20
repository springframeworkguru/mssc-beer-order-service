package guru.sfg.beer.order.service.web.mappers;

import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.beer.order.service.services.beer.BeerService;
import guru.sfg.beer.order.service.services.beer.model.BeerDto;
import guru.sfg.beer.order.service.web.model.BeerOrderLineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by jeffreymzd on 3/19/20
 */
@Component
public abstract class BeerOrderLineMapperDecorator implements BeerOrderLineMapper{

    @Autowired
    BeerService beerService;

    @Autowired
    BeerOrderLineMapper beerOrderLineMapper;

    @Override
    public BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line) {
        BeerOrderLineDto beerOrderLineDto = beerOrderLineMapper.beerOrderLineToDto(line);

        Optional<BeerDto> beerDtoOptional = beerService.getBeerInfoByUpc(line.getUpc());

        beerDtoOptional.ifPresent(beerDto -> {
            beerOrderLineDto.setBeerId(beerDto.getId());
            beerOrderLineDto.setBeerStyle(beerDto.getBeerStyle());
            beerOrderLineDto.setBeerName(beerDto.getBeerName());
            beerOrderLineDto.setPrice(beerDto.getPrice());
        });

        return beerOrderLineDto;
    }
}
