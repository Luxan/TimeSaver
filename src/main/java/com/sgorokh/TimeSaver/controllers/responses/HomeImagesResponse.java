package com.sgorokh.TimeSaver.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HomeImagesResponse {

    private List<HomeImage> images = new ArrayList<>();

    public void addImage(Long id, String name, String content) {
        images.add(new HomeImage(id, name, content));
    }

    @Data
    @AllArgsConstructor
    public static class HomeImage {

        private Long id;
        private String name;
        private String content;

    }
}
