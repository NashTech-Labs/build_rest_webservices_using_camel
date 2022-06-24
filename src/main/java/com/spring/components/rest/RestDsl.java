package com.spring.components.rest;

import com.spring.components.dto.PinCodeDto;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.support.DefaultMessage;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
public class RestDsl extends RouteBuilder {
    private final PinDataProvider pinDataProvider;

    public RestDsl(PinDataProvider pinDataProvider) {
        this.pinDataProvider = pinDataProvider;
    }

    @Override
    public void configure() throws Exception {
        restConfiguration().component("servlet").bindingMode(RestBindingMode.auto);
        rest().consumes(MediaType.APPLICATION_JSON_VALUE).produces(MediaType.APPLICATION_JSON_VALUE).get("pin/{pinCode}").outType(PinCodeDto.class).to("direct:get-pin-data").post("/pin").type(PinCodeDto.class).to("direct:save-pin-data");

        from("direct:get-pin-data").process(this::getPinCodeData);
        from("direct:save-pin-data").process(this::savePinDataAndSetToExchange);

    }

    private void savePinDataAndSetToExchange(Exchange exchange) {
        PinCodeDto pinCodeDto = exchange.getMessage().getBody(PinCodeDto.class);
        pinDataProvider.setUserPin(pinCodeDto);

    }

    private void getPinCodeData(Exchange exchange) {
        String userPin = exchange.getMessage().getHeader("pinCode", String.class);
        PinCodeDto pinCode = pinDataProvider.getUserPin(userPin);

        if (Objects.nonNull(pinCode)) {
            Message message = new DefaultMessage(exchange.getContext());
            message.setBody(pinCode);
            exchange.setMessage(message);
        } else {
            exchange.getMessage().setHeader(HTTP_RESPONSE_CODE, NOT_FOUND.value());
        }
    }
}
