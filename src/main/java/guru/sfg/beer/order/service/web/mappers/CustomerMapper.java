package guru.sfg.beer.order.service.web.mappers;

import guru.sfg.beer.order.service.domain.Customer;
import guru.sfg.brewery.model.CustomerDto;
import org.mapstruct.Mapper;

/**
 * Created by jeffreymzd on 4/5/20
 */
@Mapper(uses = DateMapper.class)
public interface CustomerMapper {

    CustomerDto customerToDto(Customer customer);
    Customer dtoToCustomer(CustomerDto dto);
}
