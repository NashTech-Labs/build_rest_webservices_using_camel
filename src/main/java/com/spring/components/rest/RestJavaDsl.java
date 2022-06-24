package com.spring.components.rest;

import com.spring.components.dto.PinCodeDto;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.support.DefaultMessage;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
public class RestJavaDsl extends RouteBuilder {
    private final PinDataProvider pinDataProvider;

    public RestJavaDsl(PinDataProvider pinDataProvider) {
        this.pinDataProvider = pinDataProvider;
    }

    @Override
    public void configure() throws Exception {
        restConfiguration().component("servlet").bindingMode(RestBindingMode.auto);


        from("rest:get:javadsl/pin/{pinCode}?produces=application/json").outputType(PinCodeDto.class).process(this::getPinCodeData);
    }

    private void getPinCodeData(Exchange exchange) {
        String city = exchange.getMessage().getHeader("pinCode", String.class);
        PinCodeDto currentPinCode = pinDataProvider.getUserPin(city);

        if (Objects.nonNull(currentPinCode)) {
            Message message = new DefaultMessage(exchange.getContext());
            message.setBody(currentPinCode);
            exchange.setMessage(message);
        } else {
            exchange.getMessage().setHeader(HTTP_RESPONSE_CODE, NOT_FOUND.value());
        }
    }
}
