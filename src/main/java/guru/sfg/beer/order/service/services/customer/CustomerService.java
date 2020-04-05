package guru.sfg.beer.order.service.services.customer;

import guru.sfg.brewery.model.CustomerPagedList;
import org.springframework.data.domain.Pageable;

/**
 * Created by jeffreymzd on 4/5/20
 */
public interface CustomerService {
    CustomerPagedList listCustomers(Pageable pageable);
}
