package com.sgorokh.TimeSaver.controllers.requests;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DeleteImagesRequest {

    private List<Long> imagesIds;

}
