package com.sgorokh.TimeSaver.domain.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageDTO {

    String name;
    byte[] image;

}
