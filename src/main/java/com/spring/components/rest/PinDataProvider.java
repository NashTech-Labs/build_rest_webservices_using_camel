package com.spring.components.rest;

import com.spring.components.dto.PinCodeDto;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class PinDataProvider {
    private static Map<String, PinCodeDto> pinCodeData = new HashMap<>();

    public PinDataProvider() {
        PinCodeDto dto = PinCodeDto.builder().pinCode("244717").state("Uttarakhand").city("Ramnagar").receivedTime(new Date().toString()).id(1).build();
        pinCodeData.put("244717", dto);
    }

    public PinCodeDto getUserPin(String city) {
        return pinCodeData.get(city);
    }

    public void setUserPin(PinCodeDto dto) {
        dto.setReceivedTime(new Date().toString());
        pinCodeData.put(dto.getCity(), dto);
    }
}
