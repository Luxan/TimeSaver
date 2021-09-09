package com.sgorokh.TimeSaver.domain.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class ClientDTO {

    private Long id;
    private String name;
    private String phone;
    private String email;

}
