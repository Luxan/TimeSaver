package com.sgorokh.TimeSaver.domain.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PortfolioImageDTO {
    private Long id;
    private String name;
}
