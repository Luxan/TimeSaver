package com.sgorokh.TimeSaver.controllers.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgorokh.TimeSaver.controllers.jsondata.PostActivityData;
import com.sgorokh.TimeSaver.controllers.jsondata.PostData;
import com.sgorokh.TimeSaver.controllers.jsondata.UploadImageData;
import com.sgorokh.TimeSaver.controllers.requests.CreateClientRequest;
import com.sgorokh.TimeSaver.controllers.requests.UpdateClientRequest;
import com.sgorokh.TimeSaver.domain.dtos.*;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RequestToDTOMapper {

    public static List<HomeImageDTO> jsonDataToHomeImageDtos(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        UploadImageData uploadImageData = objectMapper.readValue(jsonString, UploadImageData.class);
        return uploadImageData.getDocument().getData().stream()
                .map(imageData -> HomeImageDTO.builder()
                        .content(imageData.getContent())
                        .build())
                .collect(Collectors.toList());
    }

    public static PostDTO postActivityDataToPostDTO(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        PostActivityData postData = objectMapper.readValue(jsonString, PostActivityData.class);
        PostActivityData.Document.PostActivityDetails data = postData.getDocument().getData();
        return PostDTO.builder()
                .id(data.getId())
                .active(data.getActive())
                .build();
    }

    public static PostDTO postDataToPostDTO(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        PostData postData = objectMapper.readValue(jsonString, PostData.class);
        PostData.Document.PostDetails data = postData.getDocument().getData();
        return PostDTO.builder()
                .id(data.getId())
                .name(data.getName())
                .youtubeLink(data.getYoutubeLink())
                .content(data.getContent())
                .active(data.getActive())
                .build();
    }

    @SneakyThrows
    public static ImageDTO multipartFileToImageDto(MultipartFile file) {
        return ImageDTO.builder()
            .name(file.getOriginalFilename())
            .image(file.getBytes())
            .build();
    }

    public static ClientDTO createClientRequestToClientDto(CreateClientRequest request) {
        return ClientDTO.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();
    }

    public static ClientDTO updateClientRequestToClientDto(UpdateClientRequest request) {
        return ClientDTO.builder()
                .id(request.getId())
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();
    }
}
