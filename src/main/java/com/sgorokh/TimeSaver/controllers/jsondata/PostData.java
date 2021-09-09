package com.sgorokh.TimeSaver.controllers.jsondata;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostData {
    Document document;

    @Data
    public static class Document {
        PostDetails data;

        @Data
        public static class PostDetails {
            private Long id;
            private String name;
            private String youtubeLink;
            private String content;
            private Boolean active;
        }
    }
}
