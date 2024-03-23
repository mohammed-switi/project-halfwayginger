package edu.bethlehem.scinexus.Post;


import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostModelAssembler assembler;



    public Post convertPostDtoToPostEntity(PostRequestDTO postRequestDTO){

        return (Post) Post.builder()
                .description(postRequestDTO.getDescription())
                .content(postRequestDTO.getContent())
                .visibility(postRequestDTO.getVisibility())
                .publisher(getUserByPublisherId(postRequestDTO.getPublisherId()))
                .build();
    }

    public User getUserByPublisherId(long id){
        Optional<User> user = Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found", HttpStatus.NOT_FOUND)));

        return user.get();
    }


    public User findUserById(Long userId) {
        Optional<User> optionalUser= Optional.ofNullable((userRepository.findById(userId))
                .orElseThrow(UserNotFoundException::new));

        return  optionalUser.get();

    }

    public EntityModel<Post> findPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        return assembler.toModel(post);
    }

    public List<EntityModel<Post>> findAllPosts(){

        List<EntityModel<Post>> posts = postRepository
                .findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return posts;
    }

    public Post savePost(Post post){
        return postRepository.save(post);
    }

    public EntityModel<Post> createPost(PostRequestDTO newPostRequestDTO){
        Post newPost=convertPostDtoToPostEntity(newPostRequestDTO);
       return toModel(savePost(newPost));
    }


    public EntityModel<Post> updatePost(PostRequestDTO newPostRequestDTO, Long postId){

        return postRepository.findById(postId)
                .map(post -> {
                    post.setContent(newPostRequestDTO.getContent());
                    post.setVisibility(newPostRequestDTO.getVisibility());
                    post.setDescription(newPostRequestDTO.getDescription());
                    EntityModel<Post> entityModel = assembler.toModel(postRepository.save(post));
                    return entityModel;
                })
                .orElseThrow(() -> new PostNotFoundException(postId));
    }

    public EntityModel<Post> updatePostPartially(Long postId, PostRequestPatchDTO newPostRequestDTO){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId,HttpStatus.UNPROCESSABLE_ENTITY));

        if (newPostRequestDTO.getDescription() != null)
            post.setDescription(newPostRequestDTO.getDescription());
        if (newPostRequestDTO.getContent() != null)
            post.setContent(newPostRequestDTO.getContent());
        if (newPostRequestDTO.getVisibility() != null)
            post.setVisibility(newPostRequestDTO.getVisibility());
        if (newPostRequestDTO.getPublisherId() >=1)
            post.setPublisher(getUserByPublisherId(newPostRequestDTO.getPublisherId()));


        return toModel(postRepository.save(post));

    }


    public void deletePost(Long postId){

        postRepository.delete(findPostById(postId).getContent());
    }
    public EntityModel<Post> toModel(Post post){
        return assembler.toModel(post);
    }
}
