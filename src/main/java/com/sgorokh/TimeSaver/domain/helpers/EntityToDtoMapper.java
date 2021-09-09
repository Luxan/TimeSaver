package com.sgorokh.TimeSaver.domain.helpers;

import com.sgorokh.TimeSaver.controllers.helpers.ByteWrapper;
import com.sgorokh.TimeSaver.domain.dtos.*;
import com.sgorokh.TimeSaver.models.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EntityToDtoMapper {

    public static HomeImageDTO HomeImageToDTO(HomeImage homeImage) {
        return HomeImageDTO.builder()
                .id(homeImage.getImage().getId())
                .name(homeImage.getName())
                .content(homeImage.getContent())
                .build();
    }

    public static PortfolioImageDTO PortfolioImageToDTO(PortfolioImage portfolioImage) {
        return PortfolioImageDTO.builder()
                .id(portfolioImage.getImage().getId())
                .name(portfolioImage.getName())
                .build();
    }

    public static ImageDTO imageToDTO(Image image, Function<Image, Byte[]> function) {
        return ImageDTO.builder()
                .name(image.getName())
                .image(ByteWrapper.bytesToBytes(function.apply(image)))
                .build();

    }

    public static ClientDTO clientToDto(Client client) {
        return ClientDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .phone(client.getPhone())
                .email(client.getEmail())
                .build();
    }

    public static ClientDetailsDTO clientToClientDetailsDto(Client client) {
        List<SessionDetailsDTO> sessionDetailsDtos = client.getSessions().stream()
                .map(EntityToDtoMapper::sessionToSessionDetailsDTO)
                .collect(Collectors.toList());
        return ClientDetailsDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .phone(client.getPhone())
                .email(client.getEmail())
                .sessionDetails(sessionDetailsDtos)
                .build();
    }

    private static SessionDetailsDTO sessionToSessionDetailsDTO(Session session) {
        List<Image> images = session.getImages();
        Long imageId = images.size() > 0 ? images.get(0).getId() : null;
        return SessionDetailsDTO.builder()
                .id(session.getId())
                .name(session.getName())
                .imageId(imageId)
                .build();
    }

    public static PostDTO postToDto(Post post) {
        return PostDTO.builder()
                .id(post.getId())
                .name(post.getName())
                .youtubeLink(post.getYoutubeLink())
                .date(post.getDate())
                .content(post.getContent())
                .active(post.getActive())
                .imagesIds(post.getImages().stream()
                        .map(Image::getId)
                        .collect(Collectors.toList()))
                .build();
    }
}
