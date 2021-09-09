package com.sgorokh.TimeSaver.controllers.jsondata;

import lombok.Data;

import java.util.List;

@Data
public class UploadImageData {
    Document document;

    @Data
    public static class Document {
        List<ImageData> data;

        @Data
        public static class ImageData {
            String content;
        }
    }
}
