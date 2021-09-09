package com.sgorokh.TimeSaver.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PortfolioImagesResponse {

    private List<PortfolioImage> images = new ArrayList<>();

    public void addImage(Long id, String name) {
        images.add(new PortfolioImage(id, name));
    }

    @Data
    @AllArgsConstructor
    public static class PortfolioImage {

        private Long id;
        private String name;

    }
}
