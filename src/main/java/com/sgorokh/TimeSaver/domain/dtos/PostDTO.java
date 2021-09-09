package com.sgorokh.TimeSaver.domain.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class PostDTO {

    private Long id;
    private String name;
    private String youtubeLink;
    private Date date;
    private String content;
    private Boolean active;
    private List<Long> imagesIds;

}