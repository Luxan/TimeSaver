package com.sgorokh.TimeSaver.controllers.helpers;


import com.sgorokh.TimeSaver.domain.dtos.ImageDTO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipHelper {

    public static byte[] zipImages(List<ImageDTO> images) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream);
        try {
            for (ImageDTO image : images) {
                try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(image.getImage())) {
                    String imageName = String.format("%s.jpeg", images.indexOf(image));
                    addZipEntry(imageName, zipOut, byteArrayInputStream);
                }
            }
            return byteArrayOutputStream.toByteArray();
        } finally {
            zipOut.close();
            byteArrayOutputStream.close();
        }
    }

    private static void addZipEntry(String zipName, ZipOutputStream zipOut, ByteArrayInputStream byteArrayInputStream) throws IOException {
        ZipEntry zipEntry = new ZipEntry(zipName);
        zipOut.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = byteArrayInputStream.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
    }
}
