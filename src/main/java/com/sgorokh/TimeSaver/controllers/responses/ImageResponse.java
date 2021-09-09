package com.sgorokh.TimeSaver.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class ImageResponse {

    private final String bytes;

}
