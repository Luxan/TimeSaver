package com.sgorokh.TimeSaver.controllers.helpers;

import com.sgorokh.TimeSaver.domain.dtos.ImageDTO;
import sun.misc.BASE64Decoder;

import java.io.IOException;

public class ImageDecoder {

    public static byte[] decodeImage(ImageDTO image) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        String stringBytes = new String(image.getImage());
        String[] split = stringBytes.split(",");
        stringBytes = split.length > 1 ? split[1] : split[0];
        return decoder.decodeBuffer(stringBytes);
    }
}
