package com.sgorokh.TimeSaver.domain.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SessionDetailsDTO {

    private Long id;
    private String name;
    private Long imageId;

}
