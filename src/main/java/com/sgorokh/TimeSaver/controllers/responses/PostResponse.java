package com.sgorokh.TimeSaver.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String name;
    private String youtubeLink;
    private String date;
    private String content;
    private Boolean active;
    private List<Long> imagesIds;

}
