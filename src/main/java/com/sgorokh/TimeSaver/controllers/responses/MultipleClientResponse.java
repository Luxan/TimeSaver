package com.sgorokh.TimeSaver.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class MultipleClientResponse {

    private List<ClientDetails> clientDetails;

    @Builder
    @Data
    public static class ClientDetails {
        private Long id;
        private String name;
        private String phone;
        private String email;
    }

}
