package com.sgorokh.TimeSaver.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SessionDTO {

    private String name;
    private List<ImageDTO> images;

}
