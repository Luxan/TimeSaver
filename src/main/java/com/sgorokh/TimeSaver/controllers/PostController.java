package com.sgorokh.TimeSaver.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sgorokh.TimeSaver.controllers.exceptions.InvalidRequestException;
import com.sgorokh.TimeSaver.controllers.exceptions.ResourceNotFoundException;
import com.sgorokh.TimeSaver.controllers.helpers.ObjectMapper;
import com.sgorokh.TimeSaver.controllers.helpers.RequestToDTOMapper;
import com.sgorokh.TimeSaver.controllers.responses.MultiplePostsResponse;
import com.sgorokh.TimeSaver.controllers.responses.NameResponse;
import com.sgorokh.TimeSaver.controllers.responses.PostResponse;
import com.sgorokh.TimeSaver.domain.dtos.ImageDTO;
import com.sgorokh.TimeSaver.domain.dtos.PostDTO;
import com.sgorokh.TimeSaver.domain.servises.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.path}")
public class PostController {

    private final PostService postService;

    @Value("${api.path}")
    private String apiPath;

    @Autowired
    PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("posts")
    public ResponseEntity<MultiplePostsResponse> getPostsPaginated(
            @NotNull final Pageable pageable,
            @RequestParam("searchText") final String searchText,
            @RequestParam(value = "active", required = false) final Boolean active
    ) throws ResourceNotFoundException {
        Page<PostDTO> resultPage = active == null ? postService.findPostsPaginated(searchText, pageable) :
                postService.findPostsPaginated(searchText, pageable, active);

        if (pageable.getPageNumber() > resultPage.getTotalPages())
            throw new ResourceNotFoundException();

        MultiplePostsResponse response = ObjectMapper.postPageToMultiplePostsResponse(resultPage);
        return ResponseEntity.ok(response);
    }

    @GetMapping("posts/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable(value = "id") Long postId)
            throws ResourceNotFoundException {
        Optional<PostDTO> optionalPostDto = postService.getPostById(postId);

        if (!optionalPostDto.isPresent())
            throw new ResourceNotFoundException("Post with this id is not exist.");

        PostDTO postDto = optionalPostDto.get();

        PostResponse response = ObjectMapper.postDtoToPostResponse(postDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("posts")
    public ResponseEntity<NameResponse> createPost(
            @RequestParam("imageFiles") List<MultipartFile> multipartFiles,
            @RequestPart("jsonString") String jsonString,
            WebRequest request
    ) throws InvalidRequestException, URISyntaxException, JsonProcessingException {
        PostDTO postDto = RequestToDTOMapper.postDataToPostDTO(jsonString);
        List<ImageDTO> imageDtos = multipartFiles.stream()
                .map(RequestToDTOMapper::multipartFileToImageDto)
                .collect(Collectors.toList());
        PostDTO createdPost = postService.createPost(postDto, imageDtos);
        if (createdPost == null) {
            throw new InvalidRequestException("Post with this name already exist: "
                    + postDto.getName());
        }

        URI uri = new URI(request.getContextPath() + apiPath + "images");

        NameResponse response = ObjectMapper.nameToNameResponse(createdPost.getName());
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("posts")
    public ResponseEntity<NameResponse> updatePost(
            @RequestParam("imageFiles") List<MultipartFile> multipartFiles,
            @RequestPart("jsonString") String jsonString
    ) throws InvalidRequestException, JsonProcessingException {
        PostDTO postDto = RequestToDTOMapper.postDataToPostDTO(jsonString);
        List<ImageDTO> imageDtos = multipartFiles.stream()
                .map(RequestToDTOMapper::multipartFileToImageDto)
                .collect(Collectors.toList());
        PostDTO updatedPost = postService.updatePost(postDto, imageDtos);
        if (updatedPost == null)
            throw new InvalidRequestException("Post with this id does not exist" + postDto.getId());

        NameResponse response = ObjectMapper.nameToNameResponse(updatedPost.getName());
        return ResponseEntity.ok(response);
    }

    @PutMapping("posts/activate")
    public ResponseEntity<NameResponse> changePostActivity(
            @RequestPart("jsonString") String jsonString
    ) throws InvalidRequestException, JsonProcessingException {
        PostDTO postDto = RequestToDTOMapper.postActivityDataToPostDTO(jsonString);
        PostDTO updatedPost = postService.changeActivity(postDto);
        if (updatedPost == null)
            throw new InvalidRequestException("Post with this id does not exist" + postDto.getId());

        NameResponse response = ObjectMapper.nameToNameResponse(updatedPost.getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("posts/{id}")
    public ResponseEntity<NameResponse> deletePost(@PathVariable(value = "id") Long postId) throws InvalidRequestException {
        PostDTO deletedPost = postService.deletePost(postId);
        if (deletedPost == null)
            throw new InvalidRequestException("Post with this id does not exist" + postId);

        NameResponse response = ObjectMapper.nameToNameResponse(deletedPost.getName());
        return ResponseEntity.ok(response);
    }
}
