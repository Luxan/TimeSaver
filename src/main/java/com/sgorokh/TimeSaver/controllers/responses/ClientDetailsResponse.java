package com.sgorokh.TimeSaver.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class ClientDetailsResponse {

    private Long id;

    private String name;

    private String phone;

    private String email;

    private List<SessionDetails> sessions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SessionDetails {

        private Long id;

        private String name;

        private Long imageId;
    }
}
