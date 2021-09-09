package com.sgorokh.TimeSaver.domain.helpers;

import com.sgorokh.TimeSaver.controllers.helpers.ByteWrapper;
import com.sgorokh.TimeSaver.domain.dtos.ClientDTO;
import com.sgorokh.TimeSaver.domain.dtos.HomeImageDTO;
import com.sgorokh.TimeSaver.domain.dtos.ImageDTO;
import com.sgorokh.TimeSaver.domain.dtos.PostDTO;
import com.sgorokh.TimeSaver.models.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DtoToEntityMapper {

    public static HomeImage homeImageDTOToHomeImage(HomeImageDTO homeImageDto) {
        return HomeImage.builder()
                .id(homeImageDto.getId())
                .name(homeImageDto.getName())
                .content(homeImageDto.getContent())
                .build();
    }

    public static Image ImageDtoToImage(ImageDTO imageDTO) {
        return Image.builder()
                .name(imageDTO.getName())
                .originalImage(ByteWrapper.bytesToBytes(imageDTO.getImage()))
                .build();
    }

    public static Client clientDtoToClient(ClientDTO clientDTO) {
        return Client.builder()
                .id(clientDTO.getId())
                .name(clientDTO.getName())
                .email(clientDTO.getEmail())
                .phone(clientDTO.getPhone())
                .build();
    }

    public static Post PostDtoAndImagesToPost(PostDTO postDto, List<ImageDTO> imageDtos) {
        List<Image> images = imageDtos.stream()
                .map(DtoToEntityMapper::ImageDtoToImage)
                .collect(Collectors.toList());
        return Post.builder()
                .id(postDto.getId())
                .name(postDto.getName())
                .youtubeLink(postDto.getYoutubeLink())
                .date(postDto.getDate())
                .content(postDto.getContent())
                .active(postDto.getActive())
                .images(images)
                .build();
    }

    public static List<HomeImage> homeImageDtosAndImageDtosToHomeImages(List<HomeImageDTO> homeImageDtos, List<ImageDTO> imageDtos) {
        return IntStream.range(0, homeImageDtos.size())
                .mapToObj(i -> {
                    HomeImageDTO homeImageDto = homeImageDtos.get(i);
                    HomeImage homeImage = DtoToEntityMapper.homeImageDTOToHomeImage(homeImageDto);
                    ImageDTO imageDto = imageDtos.get(i);
                    homeImage.setName(imageDto.getName());
                    Image image = Image.builder()
                            .name(imageDto.getName())
                            .originalImage(ByteWrapper.bytesToBytes(imageDto.getImage()))
                            .build();
                    homeImage.setImage(image);
                    return homeImage;
                }).collect(Collectors.toList());
    }

    public static PortfolioImage ImageDtoToPortfolioImage(ImageDTO imageDto) {
        Image image = DtoToEntityMapper.ImageDtoToImage(imageDto);
        return PortfolioImage.builder()
                .name(image.getName())
                .image(image)
                .build();
    }
}
