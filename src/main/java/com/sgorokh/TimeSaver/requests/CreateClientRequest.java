package com.sgorokh.TimeSaver.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateClientRequest {

    private String name;

    private String email;

    private String phone;

}
