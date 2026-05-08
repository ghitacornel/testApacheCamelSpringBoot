package org.example.beans;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.example.model.RequestDTO;
import org.example.model.ResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RequestResponseProcessor implements Processor {

    // added just to make sure the Spring specific type is used by the route
    @Value("${app.specificProcessorConfig}")
    private String specificProcessorConfig;

    @Override
    public void process(Exchange exchange) {
        RequestDTO request = exchange.getIn().getBody(RequestDTO.class);
        ResponseDTO response = ResponseDTO.builder()
                .id(1)
                .name(request.getName() + " " + specificProcessorConfig)
                .build();
        exchange.getMessage().setBody(response);
    }

}
