package com.sgorokh.TimeSaver.domain.servises;

import com.sgorokh.TimeSaver.domain.dtos.HomeImageDTO;
import com.sgorokh.TimeSaver.domain.dtos.ImageDTO;
import com.sgorokh.TimeSaver.domain.dtos.PortfolioImageDTO;
import com.sgorokh.TimeSaver.domain.helpers.DtoToEntityMapper;
import com.sgorokh.TimeSaver.domain.helpers.EntityToDtoMapper;
import com.sgorokh.TimeSaver.models.HomeImage;
import com.sgorokh.TimeSaver.models.Image;
import com.sgorokh.TimeSaver.models.PortfolioImage;
import com.sgorokh.TimeSaver.repositories.HomeImageRepository;
import com.sgorokh.TimeSaver.repositories.ImageRepository;
import com.sgorokh.TimeSaver.repositories.PortfolioImageRepository;
import com.sgorokh.TimeSaver.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageResizerService imageResizerService;
    private final HomeImageRepository homeImageRepository;
    private final SessionRepository sessionRepository;
    private final PortfolioImageRepository portfolioImageRepository;

    @Autowired
    ImageService(
            ImageRepository imageRepository,
            ImageResizerService imageResizerService,
            HomeImageRepository homeImageRepository,
            SessionRepository sessionRepository,
            PortfolioImageRepository portfolioImageRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.imageRepository = imageRepository;
        this.imageResizerService = imageResizerService;
        this.homeImageRepository = homeImageRepository;
        this.portfolioImageRepository = portfolioImageRepository;
    }

    @Transactional(readOnly = true)
    public List<HomeImageDTO> getHomeImages() {
        return homeImageRepository.findAll().stream()
                .map(EntityToDtoMapper::HomeImageToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PortfolioImageDTO> getPortfolioImages() {
        return portfolioImageRepository.findAll().stream()
                .map(EntityToDtoMapper::PortfolioImageToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ImageDTO> getSmallImageById(Long imageId) {
        Optional<Image> optionalImage = imageRepository.findById(imageId);
        return optionalImage.map(image -> EntityToDtoMapper.imageToDTO(image, Image::getSmallImage));
    }

    @Transactional(readOnly = true)
    public Optional<ImageDTO> getMediumImageById(Long imageId) {
        Optional<Image> optionalImage = imageRepository.findById(imageId);
        return optionalImage.map(image -> EntityToDtoMapper.imageToDTO(image, Image::getMediumImage));
    }

    @Transactional(readOnly = true)
    public Optional<ImageDTO> getOriginalImageById(Long imageId) {
        Optional<Image> optionalImage = imageRepository.findById(imageId);
        return optionalImage.map(image -> EntityToDtoMapper.imageToDTO(image, Image::getOriginalImage));
    }

    @Transactional(readOnly = true)
    public Map<Long, ImageDTO> getSmallImagesByIds(List<Long> imageIds) {
        return getImageDTOMap(imageIds, Image::getSmallImage);
    }

    @Transactional(readOnly = true)
    public Map<Long, ImageDTO> getMediumImagesByIds(List<Long> imageIds) {
        return getImageDTOMap(imageIds, Image::getMediumImage);
    }

    @Transactional(readOnly = true)
    public Map<Long, ImageDTO> getOriginalImagesByIds(List<Long> imageIds) {
        return getImageDTOMap(imageIds, Image::getOriginalImage);
    }

    private Map<Long, ImageDTO> getImageDTOMap(List<Long> imageIds, Function<Image, Byte[]> function) {
        Map<Long, ImageDTO> result = new HashMap<>();
        List<Image> images = imageRepository.getByIds(imageIds);
        imageIds.forEach(id -> {
            Optional<Image> first = images.stream().filter(image -> image.getId().equals(id)).findFirst();
            first.ifPresent(image -> result.put(id, EntityToDtoMapper.imageToDTO(image, function)));
        });
        return result;
    }

    //TODO leave unchanged images
    @Transactional
    public List<String> uploadHomePageImages(List<HomeImageDTO> homeImageDtos, List<ImageDTO> imageDtos) {
        if (homeImageDtos.size() != imageDtos.size())
            throw new RuntimeException("arguments uploadHomePageImages have different list sizes.");
        homeImageRepository.deleteAll();
        List<HomeImage> homeImages = DtoToEntityMapper.homeImageDtosAndImageDtosToHomeImages(homeImageDtos, imageDtos);
        List<HomeImage> savedHomeImages = homeImageRepository.saveAll(homeImages);
        return savedHomeImages.stream()
                .map(HomeImage::getName)
                .collect(Collectors.toList());
    }

    //TODO leave unchanged images
    @Transactional
    public List<String> uploadPortfolioImages(List<ImageDTO> imageDtos) {
        portfolioImageRepository.deleteAll();
        List<PortfolioImage> portfolioImages = imageDtos.stream()
                .map(DtoToEntityMapper::ImageDtoToPortfolioImage)
                .collect(Collectors.toList());
        List<PortfolioImage> savedPortfolioImages = portfolioImageRepository.saveAll(portfolioImages);
        return savedPortfolioImages.stream()
                .map(PortfolioImage::getName)
                .collect(Collectors.toList());
    }

    @Transactional
    public Map<Long, String> saveImages(List<ImageDTO> imagesDtos) {
        List<Image> images = imagesDtos.stream()
                .map(DtoToEntityMapper::ImageDtoToImage)
                .peek(image -> image.setSmallImage(imageResizerService.resizeToSmall(image.getOriginalImage())))
                .peek(image -> image.setMediumImage(imageResizerService.resizeToMedium(image.getOriginalImage())))
                .collect(Collectors.toList());
        List<Image> savedImages = imageRepository.saveAll(images);
        return savedImages.stream().collect(Collectors.toMap(Image::getId, Image::getName));
    }

    @Transactional
    public List<String> deleteImages(List<Long> imagesIds) {
        List<Image> images = imageRepository.getByIds(imagesIds);
        imageRepository.deleteAll(images);
        return images.stream()
                .map(Image::getName)
                .collect(Collectors.toList());
    }
}
