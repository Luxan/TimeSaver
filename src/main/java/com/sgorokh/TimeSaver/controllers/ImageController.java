package com.sgorokh.TimeSaver.controllers;

import com.sgorokh.TimeSaver.controllers.exceptions.ResourceNotFoundException;
import com.sgorokh.TimeSaver.controllers.helpers.ImageDecoder;
import com.sgorokh.TimeSaver.controllers.helpers.ObjectMapper;
import com.sgorokh.TimeSaver.controllers.helpers.RequestToDTOMapper;
import com.sgorokh.TimeSaver.controllers.helpers.ZipHelper;
import com.sgorokh.TimeSaver.controllers.requests.DeleteImagesRequest;
import com.sgorokh.TimeSaver.controllers.responses.*;
import com.sgorokh.TimeSaver.domain.dtos.*;
import com.sgorokh.TimeSaver.domain.servises.ImageService;
import com.sgorokh.TimeSaver.domain.servises.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.path}")
public class ImageController {

    private final ImageService imageService;
    private final SessionService sessionService;

    @Value("${api.path}")
    private String apiPath;

    @Autowired
    ImageController(ImageService imageService, SessionService sessionService) {
        this.imageService = imageService;
        this.sessionService = sessionService;
    }

    @GetMapping("images/home")
    public ResponseEntity<HomeImagesResponse> getHomeImages() {
        List<HomeImageDTO> homeImages = imageService.getHomeImages();
        HomeImagesResponse response = ObjectMapper.homeImagesToHomeImagesResponse(homeImages);
        return ResponseEntity.ok(response);
    }

    @GetMapping("images/portfolio")
    public ResponseEntity<PortfolioImagesResponse> getPortfolioImages() {
        List<PortfolioImageDTO> portfolioImages = imageService.getPortfolioImages();
        PortfolioImagesResponse response = ObjectMapper.portfolioImagesToHomeImagesResponse(portfolioImages);
        return ResponseEntity.ok(response);
    }

    @PostMapping("images/home")
    public ResponseEntity<ImageNameListResponse> uploadHomeImages(
            @RequestParam("imageFiles") List<MultipartFile> multipartFiles,
            @RequestPart("jsonString") String jsonString
    ) throws IOException {
        List<ImageDTO> imageDtos = multipartFiles.stream()
                .map(RequestToDTOMapper::multipartFileToImageDto)
                .collect(Collectors.toList());
        List<HomeImageDTO> homeImageDtos =
                RequestToDTOMapper.jsonDataToHomeImageDtos(jsonString);
        List<String> uploadedImageNames = imageService.uploadHomePageImages(homeImageDtos, imageDtos);
        ImageNameListResponse response = ObjectMapper.imageNamesToImageNameListResponse(uploadedImageNames);
        return ResponseEntity.ok(response);
    }

    @PostMapping("images/portfolio")
    public ResponseEntity<ImageNameListResponse> uploadPortfolioImages(
            @RequestParam("imageFiles") List<MultipartFile> multipartFiles
    ) {
        List<ImageDTO> imageDtos = multipartFiles.stream()
                .map(RequestToDTOMapper::multipartFileToImageDto)
                .collect(Collectors.toList());

        List<String> uploadedImageNames = imageService.uploadPortfolioImages(imageDtos);
        ImageNameListResponse response = ObjectMapper.imageNamesToImageNameListResponse(uploadedImageNames);
        return ResponseEntity.ok(response);
    }

    @GetMapping("images/download/{id}")
    public ResponseEntity<byte[]> downloadImageById(@PathVariable(value = "id") Long imageId)
            throws ResourceNotFoundException, IOException {
        ImageDTO image = imageService.getOriginalImageById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not get Image by id = " + imageId));
        byte[] bytes = ImageDecoder.decodeImage(image);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + image.getName())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(bytes);
    }

    @GetMapping("images/download/session/{id}")
    public ResponseEntity<byte[]> downloadImagesBySessionId(@PathVariable(value = "id") Long sessionId)
            throws IOException, ResourceNotFoundException {
        SessionDTO sessionDTO = sessionService.getOriginalImageSessionDtoBySessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not get Session by id = " + sessionId));

        byte[] os = ZipHelper.zipImages(sessionDTO.getImages());
        return ResponseEntity.ok()
                .contentType(new MediaType("application/zip"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + sessionDTO.getName() + ".zip")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(os);
    }

    @GetMapping("images/small/{id}")
    public ResponseEntity<ImageResponse> getSmallImageById(@PathVariable(value = "id") Long imageId)
            throws ResourceNotFoundException {
        ImageDTO image = imageService.getSmallImageById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not get Image by id = " + imageId));
        ImageResponse response = ObjectMapper.imageDtoToImageResponse(image);
        return ResponseEntity.ok(response);
    }

    @GetMapping("images/medium/{id}")
    public ResponseEntity<ImageResponse> getMediumImageById(@PathVariable(value = "id") Long imageId)
            throws ResourceNotFoundException {
        ImageDTO image = imageService.getMediumImageById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not get Image by id = " + imageId));
        ImageResponse response = ObjectMapper.imageDtoToImageResponse(image);
        return ResponseEntity.ok(response);
    }

    @GetMapping("images/original/{id}")
    public ResponseEntity<ImageResponse> getOriginalImageById(@PathVariable(value = "id") Long imageId)
            throws ResourceNotFoundException {
        ImageDTO image = imageService.getOriginalImageById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not get Image by id = " + imageId));
        ImageResponse response = ObjectMapper.imageDtoToImageResponse(image);
        return ResponseEntity.ok(response);
    }

    @GetMapping("images/small")
    public ResponseEntity<ImageMapResponse> getSmallImagesByIds(@RequestParam(value = "ids") Long[] imageIds) {
        Map<Long, ImageDTO> map = imageService.getOriginalImagesByIds(Arrays.asList(imageIds));
        ImageMapResponse response = ObjectMapper.imageDtoMapToMultipleImagesResponse(map);
        return ResponseEntity.ok(response);
    }

    @GetMapping("images/medium")
    public ResponseEntity<ImageMapResponse> getMediumImagesByIds(@RequestParam(value = "ids") Long[] imageIds) {
        Map<Long, ImageDTO> map = imageService.getOriginalImagesByIds(Arrays.asList(imageIds));
        ImageMapResponse response = ObjectMapper.imageDtoMapToMultipleImagesResponse(map);
        return ResponseEntity.ok(response);
    }

    @GetMapping("images/original")
    public ResponseEntity<ImageMapResponse> getOriginalImagesByIds(@RequestParam(value = "ids") Long[] imageIds) {
        Map<Long, ImageDTO> map = imageService.getOriginalImagesByIds(Arrays.asList(imageIds));
        ImageMapResponse response = ObjectMapper.imageDtoMapToMultipleImagesResponse(map);
        return ResponseEntity.ok(response);
    }

    @GetMapping("images/session/{id}")
    public ResponseEntity<MultipleImagesResponse> getImagesBySessionId(@PathVariable(value = "id") Long sessionId)
            throws ResourceNotFoundException {
        SessionDTO sessionDTO = sessionService.getSmallImageSessionDtoBySessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not get Session by id = " + sessionId));
        MultipleImagesResponse response = ObjectMapper.sessionDtoToMultipleImagesResponse(sessionDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("images")
    public ResponseEntity<ImageIdNameMapResponse> uploadImages(
            @RequestParam("images") MultipartFile[] files,
            WebRequest request
    ) throws URISyntaxException {
        List<ImageDTO> images = Arrays.stream(files)
                .map(ObjectMapper::fileToImageDto)
                .collect(Collectors.toList());
        Map<Long, String> map = imageService.saveImages(images);
        URI uri = new URI(request.getContextPath() + apiPath + "images");
        ImageIdNameMapResponse response = ObjectMapper.imageIdNameMapToImageIdNameMapResponse(map);
        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping("images")
    public ResponseEntity<ImageNameListResponse> deleteImages(@RequestBody DeleteImagesRequest imagesRequest) {
        List<String> deletedImageNames = imageService.deleteImages(imagesRequest.getImagesIds());
        ImageNameListResponse response = ObjectMapper.imageNamesToImageNameListResponse(deletedImageNames);
        return ResponseEntity.ok().body(response);
    }

}
