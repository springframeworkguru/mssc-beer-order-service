package guru.sfg.beer.order.service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import guru.sfg.beer.order.service.config.JmsConfiguration;
import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.domain.Customer;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.repositories.CustomerRepository;
import guru.sfg.beer.order.service.services.beer.BeerServiceImpl;
import guru.sfg.brewery.model.BeerDto;
import guru.sfg.brewery.model.BeerPagedList;
import guru.sfg.brewery.model.event.AllocationFailureEvent;
import guru.sfg.brewery.model.event.DeallocateOrderEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by jeffreymzd on 3/31/20
 */
@ExtendWith(WireMockExtension.class)
@SpringBootTest
class BeerOrderManagerImplIT {

    @Autowired
    WireMockServer wireMockServer;

    @TestConfiguration
    static class RestTemplateBuilderProvider {

        @Bean(destroyMethod = "stop")
        public WireMockServer wireMockServer() {
            WireMockServer wireMockServer = with(wireMockConfig().port(8084));
            wireMockServer.start();
            return wireMockServer;
        }
    }

    @Autowired
    BeerOrderManager beerOrderManager;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JmsTemplate jmsTemplate;

    Customer testCustomer;

    UUID beerId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.save(Customer.builder()
                .customerName("Test Customer")
                .build());
    }

    @Test
    void testNewToAllocated() throws JsonProcessingException {

        BeerDto beerDto = BeerDto.builder().id(beerId).upc("12345").build();
        BeerPagedList list = new BeerPagedList(Collections.singletonList(beerDto));

        wireMockServer.stubFor(get(BeerServiceImpl.BEER_BY_UPC_PATH + "12345?showInventoryOnHand=true")
                .willReturn(okJson(objectMapper.writeValueAsString(list))));

        BeerOrder beerOrder = createBeerOrder();

        BeerOrder savedBeerOrder = beerOrderManager.newBeerOrder(beerOrder);

        await().untilAsserted(() -> {
            BeerOrder foundBeerOrder = beerOrderRepository.findById(beerOrder.getId()).get();
            assertEquals(BeerOrderStatusEnum.ALLOCATED, foundBeerOrder.getOrderStatus());
        });

        BeerOrder savedBeerOrder2 = beerOrderRepository.findById(savedBeerOrder.getId()).get();

        assertNotNull(savedBeerOrder2);
        assertEquals(BeerOrderStatusEnum.ALLOCATED, savedBeerOrder2.getOrderStatus());
        savedBeerOrder2.getBeerOrderLines().forEach(beerOrderLine -> {
            assertEquals(beerOrderLine.getOrderQuantity(), beerOrderLine.getQuantityAllocated());
        });
    }

    @Test
    void testNewToFailValidation() throws JsonProcessingException {

        BeerDto beerDto = BeerDto.builder().id(beerId).upc("12345").build();
        BeerPagedList list = new BeerPagedList(Collections.singletonList(beerDto));

        wireMockServer.stubFor(get(BeerServiceImpl.BEER_BY_UPC_PATH + "12345?showInventoryOnHand=true")
                .willReturn(okJson(objectMapper.writeValueAsString(list))));

        BeerOrder beerOrder = createBeerOrder();
        beerOrder.setCustomerRef("fail-validation");

        BeerOrder savedBeerOrder = beerOrderManager.newBeerOrder(beerOrder);

        await().untilAsserted(() -> {
            BeerOrder foundBeerOrder = beerOrderRepository.findById(beerOrder.getId()).get();
            assertEquals(BeerOrderStatusEnum.VALIDATION_EXCEPTION, foundBeerOrder.getOrderStatus());
        });
    }

    @Test
    void testNewToPickedUp() throws JsonProcessingException {

        BeerDto beerDto = BeerDto.builder().id(beerId).upc("12345").build();
        BeerPagedList list = new BeerPagedList(Collections.singletonList(beerDto));

        wireMockServer.stubFor(get(BeerServiceImpl.BEER_BY_UPC_PATH + "12345?showInventoryOnHand=true")
                .willReturn(okJson(objectMapper.writeValueAsString(list))));

        BeerOrder beerOrder = createBeerOrder();

        BeerOrder savedBeerOrder = beerOrderManager.newBeerOrder(beerOrder);

        await().untilAsserted(() -> {
            BeerOrder foundBeerOrder = beerOrderRepository.findById(beerOrder.getId()).get();
            assertEquals(BeerOrderStatusEnum.ALLOCATED, foundBeerOrder.getOrderStatus());
        });

        BeerOrder savedBeerOrder2 = beerOrderRepository.findById(savedBeerOrder.getId()).get();

        assertNotNull(savedBeerOrder2);
        assertEquals(BeerOrderStatusEnum.ALLOCATED, savedBeerOrder2.getOrderStatus());
        savedBeerOrder2.getBeerOrderLines().forEach(beerOrderLine -> {
            assertEquals(beerOrderLine.getOrderQuantity(), beerOrderLine.getQuantityAllocated());
        });

        beerOrderManager.beerOrderPickedUp(savedBeerOrder2.getId());

        await().untilAsserted(() -> {
            BeerOrder foundBeerOrder = beerOrderRepository.findById(beerOrder.getId()).get();
            assertEquals(BeerOrderStatusEnum.PICKED_UP, foundBeerOrder.getOrderStatus());
        });

        BeerOrder savedBeerOrder3 = beerOrderRepository.findById(savedBeerOrder.getId()).get();

        assertNotNull(savedBeerOrder3);
        assertEquals(BeerOrderStatusEnum.PICKED_UP, savedBeerOrder3.getOrderStatus());

    }

    @Test
    void testNewToFailAllocation() throws JsonProcessingException {

        BeerDto beerDto = BeerDto.builder().id(beerId).upc("12345").build();
        BeerPagedList list = new BeerPagedList(Collections.singletonList(beerDto));

        wireMockServer.stubFor(get(BeerServiceImpl.BEER_BY_UPC_PATH + "12345?showInventoryOnHand=true")
                .willReturn(okJson(objectMapper.writeValueAsString(list))));

        BeerOrder beerOrder = createBeerOrder();
        beerOrder.setCustomerRef("fail-allocation");

        BeerOrder savedBeerOrder = beerOrderManager.newBeerOrder(beerOrder);

        await().untilAsserted(() -> {
            BeerOrder foundBeerOrder = beerOrderRepository.findById(beerOrder.getId()).get();
            assertEquals(BeerOrderStatusEnum.ALLOCATION_EXCEPTION, foundBeerOrder.getOrderStatus());
        });

        BeerOrder savedBeerOrder2 = beerOrderRepository.findById(savedBeerOrder.getId()).get();

        AllocationFailureEvent allocationFailureEvent = (AllocationFailureEvent) jmsTemplate.receiveAndConvert(JmsConfiguration.ALLOCATE_ORDER_FAILURE_QUEUE);
        assertNotNull(allocationFailureEvent);
        assertNotNull(allocationFailureEvent.getOrderId());
        assertThat(allocationFailureEvent.getOrderId()).isEqualTo(beerOrder.getId());

        assertNotNull(savedBeerOrder2);
        assertEquals(BeerOrderStatusEnum.ALLOCATION_EXCEPTION, savedBeerOrder2.getOrderStatus());

    }

    @Test
    void testNewToPartialAllocation() throws JsonProcessingException {

        BeerDto beerDto = BeerDto.builder().id(beerId).upc("12345").build();
        BeerPagedList list = new BeerPagedList(Collections.singletonList(beerDto));

        wireMockServer.stubFor(get(BeerServiceImpl.BEER_BY_UPC_PATH + "12345?showInventoryOnHand=true")
                .willReturn(okJson(objectMapper.writeValueAsString(list))));

        BeerOrder beerOrder = createBeerOrder();
        beerOrder.setCustomerRef("partial-allocation");

        BeerOrder savedBeerOrder = beerOrderManager.newBeerOrder(beerOrder);

        await().untilAsserted(() -> {
            BeerOrder foundBeerOrder = beerOrderRepository.findById(beerOrder.getId()).get();
            assertEquals(BeerOrderStatusEnum.PENDING_INVENTORY, foundBeerOrder.getOrderStatus());
        });
    }

    @Test
    void testCancelOnAllocationPending() throws JsonProcessingException {

        BeerDto beerDto = BeerDto.builder().id(beerId).upc("12345").build();
        BeerPagedList list = new BeerPagedList(Collections.singletonList(beerDto));

        wireMockServer.stubFor(get(BeerServiceImpl.BEER_BY_UPC_PATH + "12345?showInventoryOnHand=true")
                .willReturn(okJson(objectMapper.writeValueAsString(list))));

        BeerOrder beerOrder = createBeerOrder();

        beerOrder.setCustomerRef("cancel-allocation-pending");

        BeerOrder savedBeerOrder = beerOrderManager.newBeerOrder(beerOrder);

        await().untilAsserted(() -> {
            BeerOrder foundBeerOrder = beerOrderRepository.findById(beerOrder.getId()).get();
            assertEquals(BeerOrderStatusEnum.ALLOCATION_PENDING, foundBeerOrder.getOrderStatus());
        });

        beerOrderManager.beerOrderCancel(beerOrder.getId());

        await().untilAsserted(() -> {
            BeerOrder foundBeerOrder = beerOrderRepository.findById(beerOrder.getId()).get();
            assertEquals(BeerOrderStatusEnum.CANCELLED, foundBeerOrder.getOrderStatus());
        });

        BeerOrder finalBeerOrder = beerOrderRepository.findById(beerOrder.getId()).get();
        assertNotNull(finalBeerOrder);
        assertThat(finalBeerOrder.getOrderStatus()).isEqualTo(BeerOrderStatusEnum.CANCELLED);
    }

    @Test
    void testCancelOnValidationPending() throws JsonProcessingException {

        BeerDto beerDto = BeerDto.builder().id(beerId).upc("12345").build();
        BeerPagedList list = new BeerPagedList(Collections.singletonList(beerDto));

        wireMockServer.stubFor(get(BeerServiceImpl.BEER_BY_UPC_PATH + "12345?showInventoryOnHand=true")
                .willReturn(okJson(objectMapper.writeValueAsString(list))));

        BeerOrder beerOrder = createBeerOrder();

        beerOrder.setCustomerRef("cancel-validation-pending");

        BeerOrder savedBeerOrder = beerOrderManager.newBeerOrder(beerOrder);

        await().untilAsserted(() -> {
            BeerOrder foundBeerOrder = beerOrderRepository.findById(beerOrder.getId()).get();
            assertEquals(BeerOrderStatusEnum.VALIDATION_PENDING, foundBeerOrder.getOrderStatus());
        });

        beerOrderManager.beerOrderCancel(beerOrder.getId());

        await().untilAsserted(() -> {
            BeerOrder foundBeerOrder = beerOrderRepository.findById(beerOrder.getId()).get();
            assertEquals(BeerOrderStatusEnum.CANCELLED, foundBeerOrder.getOrderStatus());
        });

        BeerOrder finalBeerOrder = beerOrderRepository.findById(beerOrder.getId()).get();
        assertNotNull(finalBeerOrder);
        assertThat(finalBeerOrder.getOrderStatus()).isEqualTo(BeerOrderStatusEnum.CANCELLED);

    }

    @Test
    void testCancelOnAllocated() throws JsonProcessingException {

        BeerDto beerDto = BeerDto.builder().id(beerId).upc("12345").build();
        BeerPagedList list = new BeerPagedList(Collections.singletonList(beerDto));

        wireMockServer.stubFor(get(BeerServiceImpl.BEER_BY_UPC_PATH + "12345?showInventoryOnHand=true")
                .willReturn(okJson(objectMapper.writeValueAsString(list))));

        BeerOrder beerOrder = createBeerOrder();

        BeerOrder savedBeerOrder = beerOrderManager.newBeerOrder(beerOrder);

        await().untilAsserted(() -> {
            BeerOrder foundBeerOrder = beerOrderRepository.findById(beerOrder.getId()).get();
            assertEquals(BeerOrderStatusEnum.ALLOCATED, foundBeerOrder.getOrderStatus());
        });

        beerOrderManager.beerOrderCancel(beerOrder.getId());

        await().untilAsserted(() -> {
            BeerOrder foundBeerOrder = beerOrderRepository.findById(beerOrder.getId()).get();
            assertEquals(BeerOrderStatusEnum.CANCELLED, foundBeerOrder.getOrderStatus());
        });

        BeerOrder finalBeerOrder = beerOrderRepository.findById(beerOrder.getId()).get();
        assertNotNull(finalBeerOrder);
        assertThat(finalBeerOrder.getOrderStatus()).isEqualTo(BeerOrderStatusEnum.CANCELLED);

        DeallocateOrderEvent deallocateOrderRequest = (DeallocateOrderEvent) jmsTemplate
                .receiveAndConvert(JmsConfiguration.DEALLOCATE_ORDER_QUEUE);
        assertNotNull(deallocateOrderRequest);
        assertThat(deallocateOrderRequest.getBeerOrderDto().getId())
                .isEqualTo(beerOrder.getId());
    }

    public BeerOrder createBeerOrder() {
        BeerOrder beerOrder = BeerOrder.builder()
                .customer(testCustomer)
                .build();
        Set<BeerOrderLine> lines = new HashSet<>();
        lines.add(BeerOrderLine.builder()
                .beerId(beerId)
                .upc("12345")
                .orderQuantity(1)
                .beerOrder(beerOrder)
                .build());

        beerOrder.setBeerOrderLines(lines);

        return beerOrder;
    }
}