package com.sgorokh.TimeSaver.controllers.helpers;

import com.sgorokh.TimeSaver.controllers.responses.*;
import com.sgorokh.TimeSaver.domain.dtos.*;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectMapper {

    @SneakyThrows
    public static ImageDTO fileToImageDto(MultipartFile file) {
        return ImageDTO.builder()
                .name(file.getOriginalFilename())
                .image(file.getBytes())
                .build();
    }

    public static MultipleClientResponse clientToMultipleClientDetailsResponse(List<ClientDTO> clients) {
        List<MultipleClientResponse.ClientDetails> clientDetails = clients.stream()
                .map(client -> MultipleClientResponse.ClientDetails.builder()
                        .id(client.getId())
                        .name(client.getName())
                        .email(client.getEmail())
                        .phone(client.getPhone())
                        .build()).collect(Collectors.toList());
        return new MultipleClientResponse(clientDetails);
    }

    public static ImageResponse imageDtoToImageResponse(ImageDTO image) {
        return ImageResponse.builder()
                .bytes(new String(image.getImage()))
                .build();
    }

    public static HomeImagesResponse homeImagesToHomeImagesResponse(List<HomeImageDTO> homeImages) {
        HomeImagesResponse response = new HomeImagesResponse();
        homeImages.forEach(homeImage ->
                response.addImage(homeImage.getId(), homeImage.getName(), homeImage.getContent()));
        return response;
    }

    public static PortfolioImagesResponse portfolioImagesToHomeImagesResponse(List<PortfolioImageDTO> portfolioImages) {
        PortfolioImagesResponse response = new PortfolioImagesResponse();
        portfolioImages.forEach(homeImage ->
                response.addImage(homeImage.getId(), homeImage.getName()));
        return response;
    }

    public static ImageNameListResponse imageNamesToImageNameListResponse(List<String> selectedImageNames) {
        return ImageNameListResponse.builder()
                .imagesNames(selectedImageNames)
                .build();
    }

    public static MultipleImagesResponse sessionDtoToMultipleImagesResponse(SessionDTO session) {
        List<String> byteList = session.getImages().stream()
                .map(ImageDTO::getImage)
                .map(String::new)
                .collect(Collectors.toList());
        return new MultipleImagesResponse(byteList);
    }

    public static ImageMapResponse imageDtoMapToMultipleImagesResponse(Map<Long, ImageDTO> map) {
        Map<Long, String> responseMap = map.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        imageDto -> new String((imageDto.getValue().getImage()))));

        return new ImageMapResponse(responseMap);
    }

    public static ImageIdNameMapResponse imageIdNameMapToImageIdNameMapResponse(Map<Long, String> map) {
        return new ImageIdNameMapResponse(map);
    }

    public static ClientDetailsResponse clientDetailsDtoToClientDetailsResponse(ClientDetailsDTO client) {
        List<ClientDetailsResponse.SessionDetails> sessionDetails = client.getSessionDetails().stream()
                .map(ObjectMapper::sessionDetailsDtoToSessionDetails)
                .collect(Collectors.toList());
        return ClientDetailsResponse.builder()
                .id(client.getId())
                .name(client.getName())
                .phone(client.getPhone())
                .email(client.getEmail())
                .sessions(sessionDetails)
                .build();
    }

    private static ClientDetailsResponse.SessionDetails sessionDetailsDtoToSessionDetails(SessionDetailsDTO sessionDetailsDto) {
        return ClientDetailsResponse.SessionDetails.builder()
                .id(sessionDetailsDto.getId())
                .name(sessionDetailsDto.getName())
                .imageId(sessionDetailsDto.getImageId())
                .build();
    }

    public static NameResponse nameToNameResponse(String name) {
        return new NameResponse(name);
    }

    public static MultiplePostsResponse postPageToMultiplePostsResponse(Page<PostDTO> resultPage) {
        List<MultiplePostsResponse.PostDetails> postDetails = resultPage.get()
                .map(postDto -> {
                    Long coverImageId = postDto.getImagesIds().isEmpty() ? null : postDto.getImagesIds().get(0);
                    return MultiplePostsResponse.PostDetails.builder()
                            .id(postDto.getId())
                            .name(postDto.getName())
                            .youtubeLink(postDto.getYoutubeLink())
                            .date(dateToString(postDto.getDate()))
                            .content(postDto.getContent())
                            .active(postDto.getActive())
                            .coverImageId(coverImageId)
                            .build();
                })
                .collect(Collectors.toList());
        return MultiplePostsResponse.builder()
                .postDetails(postDetails)
                .page(resultPage.getNumber())
                .postCount(resultPage.getTotalElements())
                .build();
    }

    public static PostResponse postDtoToPostResponse(PostDTO postDto) {
        return PostResponse.builder()
                .id(postDto.getId())
                .name(postDto.getName())
                .youtubeLink(postDto.getYoutubeLink())
                .date(dateToString(postDto.getDate()))
                .content(postDto.getContent())
                .active(postDto.getActive())
                .imagesIds(postDto.getImagesIds())
                .build();
    }

    private static String dateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dateFormat.format(date);
    }
}
