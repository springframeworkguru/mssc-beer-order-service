package guru.sfg.beer.order.service.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by jeffreymzd on 4/5/20
 */
@Profile("local-discovery")
@EnableDiscoveryClient
@Configuration
public class LocalDiscovery {
}
