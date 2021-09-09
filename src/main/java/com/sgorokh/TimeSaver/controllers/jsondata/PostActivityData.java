package com.sgorokh.TimeSaver.controllers.jsondata;

import lombok.Data;

@Data
public class PostActivityData {
    Document document;

    @Data
    public static class Document {
        PostActivityDetails data;

        @Data
        public static class PostActivityDetails {
            private Long id;
            private Boolean active;
        }
    }
}
