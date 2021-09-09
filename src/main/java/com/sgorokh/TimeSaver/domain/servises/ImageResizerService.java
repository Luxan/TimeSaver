package com.sgorokh.TimeSaver.domain.servises;


import com.sgorokh.TimeSaver.controllers.helpers.ByteWrapper;
import lombok.SneakyThrows;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ImageResizerService {

    @Value("${imagesizedef.medium.height}")
    private Integer mediumHeight;
    @Value("${imagesizedef.medium.width}")
    private Integer mediumWidth;
    @Value("${imagesizedef.small.height}")
    private Integer smallHeight;
    @Value("${imagesizedef.small.width}")
    private Integer smallWidth;

    @SneakyThrows
    public Byte[] resizeToMedium(Byte[] bytes) {
        return resizeImage(bytes, mediumWidth, mediumHeight);
    }

    @SneakyThrows
    public Byte[] resizeToSmall(Byte[] bytes) {
        return resizeImage(bytes, smallWidth, smallHeight);
    }

    private Byte[] resizeImage(Byte[] bytes, Integer width, Integer height) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(ByteWrapper.bytesToBytes(bytes));
        BufferedImage originalImage = ImageIO.read(byteArrayInputStream);
        BufferedImage resizedImage = resize(originalImage, width, height);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpeg", byteArrayOutputStream);
        return ByteWrapper.bytesToBytes(byteArrayOutputStream.toByteArray());
    }

    private BufferedImage resize(BufferedImage originalImage, int targetWidth, int targetHeight) {
        return Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
    }

}
