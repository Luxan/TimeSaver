package com.sgorokh.TimeSaver.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class MultiplePostsResponse {

    private long postCount;
    private int page;
    private List<PostDetails> postDetails;

    @Builder
    @Data
    public static class PostDetails {
        private Long id;
        private String name;
        private String youtubeLink;
        private String date;
        private String content;
        private Boolean active;
        private Long coverImageId;
    }

}
