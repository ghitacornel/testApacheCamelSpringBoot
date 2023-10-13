package camel.route.steps;

import camel.database.Order;
import camel.database.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReadTryForVoucherOrdersFromDBProcessor implements Processor {

    private final OrderRepository orderRepository;

    @Override
    public void process(Exchange exchange) {
        List<Order> orders = orderRepository.findTryForVoucherOrders();
        exchange.getMessage().setBody(orders);
    }
}
