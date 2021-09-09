package com.sgorokh.TimeSaver.domain.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClientDetailsDTO {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private List<SessionDetailsDTO> sessionDetails;

}
