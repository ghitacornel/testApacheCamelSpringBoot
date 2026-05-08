package org.example.beans;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.example.model.RequestDTO;
import org.example.model.ResponseDTO;

public class RequestResponseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        RequestDTO request = exchange.getIn().getBody(RequestDTO.class);
        ResponseDTO response = ResponseDTO.builder()
                .id(1)
                .name(request.getName())
                .build();
        exchange.getMessage().setBody(response);
    }

}
