package com.spring.components.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PinCodeDto implements Serializable {
    static int counter = 1;
    private int id =counter++;
    private String city;
    private String state;
    private String pinCode;
    private String receivedTime;
}
