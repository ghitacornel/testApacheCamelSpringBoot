package camel.route.components;

import camel.route.model.PersonResponse;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutputModelProcessor implements Processor {


    @Override
    public void process(Exchange exchange) {
        PersonResponse personResponse = exchange.getIn().getBody(PersonResponse.class);
        exchange.getMessage().setBody(personResponse);
    }

}
