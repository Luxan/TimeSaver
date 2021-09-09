package com.sgorokh.TimeSaver.domain.servises;

import com.sgorokh.TimeSaver.domain.dtos.ImageDTO;
import com.sgorokh.TimeSaver.domain.dtos.PostDTO;
import com.sgorokh.TimeSaver.domain.helpers.DtoToEntityMapper;
import com.sgorokh.TimeSaver.domain.helpers.EntityToDtoMapper;
import com.sgorokh.TimeSaver.models.Image;
import com.sgorokh.TimeSaver.models.Post;
import com.sgorokh.TimeSaver.repositories.ImageRepository;
import com.sgorokh.TimeSaver.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Value("${post.content.limit}")
    private int contentSizeLimit;

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    @Autowired
    PostService(
            PostRepository postRepository,
            ImageRepository imageRepository
    ) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
    }

    @Transactional(readOnly = true)
    public Page<PostDTO> findPostsPaginated(String searchText, Pageable pageable) {
        Page<Post> allPosts = postRepository.getAllBySearchString(searchText, pageable);
        return postListToPageOfDtos(allPosts);
    }

    @Transactional(readOnly = true)
    public Page<PostDTO> findPostsPaginated(String searchText, Pageable pageable, Boolean active) {
        Page<Post> allPosts = postRepository.getAllBySearchStringAndActive(searchText, active, pageable);
        return postListToPageOfDtos(allPosts);
    }

    private PageImpl<PostDTO> postListToPageOfDtos(Page<Post> postPage) {
        List<PostDTO> postDtos = postPage.getContent().stream()
                .map(EntityToDtoMapper::postToDto)
                .peek(postDTO -> postDTO.setContent(limitContent(postDTO.getContent())))
                .collect(Collectors.toList());
        return new PageImpl<>(postDtos, postPage.getPageable(), postPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Optional<PostDTO> getPostById(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            return Optional.of(EntityToDtoMapper.postToDto(post));
        } else return Optional.empty();
    }

    private String limitContent(String content) {
        if (content.length() <= contentSizeLimit)
            return content;

        return content.substring(0, contentSizeLimit);
    }

    @Transactional
    public PostDTO createPost(PostDTO postDto, List<ImageDTO> imageDtos) {
        Post post = DtoToEntityMapper.PostDtoAndImagesToPost(postDto, imageDtos);
        post.setDate(new Date());
        Post savedPost = postRepository.save(post);
        return EntityToDtoMapper.postToDto(savedPost);
    }

    // TODO leave unchanged
    @Transactional
    public PostDTO updatePost(PostDTO postDto, List<ImageDTO> imageDtos) {
        Optional<Post> foundPost = postRepository.findById(postDto.getId());
        if (!foundPost.isPresent()) return null;
        Post post = foundPost.get();

        post.setName(postDto.getName());
        post.setYoutubeLink(postDto.getYoutubeLink());
        post.setDate(new Date());
        post.setContent(postDto.getContent());
        post.setActive(postDto.getActive());

        List<Image> images = imageDtos.stream()
                .map(DtoToEntityMapper::ImageDtoToImage)
                .collect(Collectors.toList());

        post.getImages().forEach(imageRepository::delete);
        post.setImages(images);

        Post savedPost = postRepository.save(post);
        return EntityToDtoMapper.postToDto(savedPost);
    }

    @Transactional
    public PostDTO changeActivity(PostDTO postDto) {
        Optional<Post> foundPost = postRepository.findById(postDto.getId());
        if (!foundPost.isPresent()) return null;
        Post post = foundPost.get();
        post.setActive(postDto.getActive());
        Post savedPost = postRepository.save(post);
        return EntityToDtoMapper.postToDto(savedPost);
    }

    @Transactional
    public PostDTO deletePost(Long postId) {
        Optional<Post> foundPost = postRepository.findById(postId);
        if (!foundPost.isPresent()) return null;

        Post post = foundPost.get();
        postRepository.delete(post);

        return EntityToDtoMapper.postToDto(post);
    }
}
