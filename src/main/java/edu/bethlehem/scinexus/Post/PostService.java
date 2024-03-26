package edu.bethlehem.scinexus.Post;

import edu.bethlehem.scinexus.Auth.UserNotAuthorizedException;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.User.UserService;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserRepository;
import edu.bethlehem.scinexus.User.UserRequestDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostModelAssembler assembler;
    private final JwtService jwtService;
    private final UserService userService;

    public Post convertPostDtoToPostEntity(Authentication authentication, PostRequestDTO postRequestDTO) {

        return Post.builder()
                .content(postRequestDTO.getContent())
                .visibility(postRequestDTO.getVisibility())
                .publisher(getUserByPublisherId(jwtService.extractId(authentication)))
                .build();
    }

    public User getUserByPublisherId(long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found", HttpStatus.NOT_FOUND));

    }

    public User findUserById(Long userId) {
        return (userRepository.findById(userId))
                .orElseThrow(UserNotFoundException::new);

    }

    public EntityModel<Post> findPostById(Long postId, Authentication authentication) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        hasAuthorizedVision(authentication, post);

        return assembler.toModel(post);
    }

    // We Should Specify An Admin Authority To get All Posts
    public List<EntityModel<Post>> findAllPosts() {

        return postRepository
                .findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public EntityModel<Post> createPost(Authentication authentication, PostRequestDTO newPostRequestDTO) {
        Post newPost = convertPostDtoToPostEntity(authentication, newPostRequestDTO);
        return assembler.toModel(savePost(newPost));
    }

    public EntityModel<Post> updatePost(Long postId, Authentication authentication,
            PostRequestDTO newPostRequestDTO) {

        return postRepository.findById(postId)
                .filter(post -> isUserAuthorized(authentication, post))
                .map(post -> {
                    post.setContent(newPostRequestDTO.getContent());
                    post.setVisibility(newPostRequestDTO.getVisibility());
                    return assembler.toModel(postRepository.save(post));
                })
                .orElseThrow(() -> new PostNotFoundException(postId));
    }

    public EntityModel<Post> updatePostPartially(Long postId, Authentication authentication,
            PostRequestPatchDTO newPostRequestDTO) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId, HttpStatus.UNPROCESSABLE_ENTITY));

        isUserAuthorized(authentication, post);

        try {
            for (Method method : PostRequestPatchDTO.class.getMethods()) {
                if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                    Object value = method.invoke(newPostRequestDTO);
                    if (value != null) {
                        String propertyName = method.getName().substring(3); // remove "get"
                        Method setter = Post.class.getMethod("set" + propertyName, method.getReturnType());
                        setter.invoke(post, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return assembler.toModel(postRepository.save(post));

    }

    public void deletePost(Long postId, Authentication authentication) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId, HttpStatus.UNPROCESSABLE_ENTITY));

        isUserAuthorized(authentication, post);

        postRepository.delete(post);
    }

    private boolean isUserAuthorized(Authentication authentication, Post post) {
        Long userRequestId = jwtService.extractId(authentication);
        Long publisherId = post.getPublisher().getId();

        if (Objects.equals(userRequestId, publisherId))
            return true;
        else
            throw new UserNotAuthorizedException();
    }

    private boolean hasAuthorizedVision(Authentication authentication, Post post) {

        Long userRequestId = jwtService.extractId(authentication);
        Long publisherId = post.getPublisher().getId();

        if (post.getVisibility().equals(Visibility.PRIVATE) && isUserAuthorized(authentication, post))
            return true;
        else if (post.getVisibility().equals(Visibility.LINKS) &&
                (userService.areUsersLinked(findUserById(userRequestId), findUserById(publisherId)))
                || isUserAuthorized(authentication, post))
            return true;
        else if (post.getVisibility().equals(Visibility.PUBLIC))
            return true;
        else
            throw new UserNotAuthorizedException(userRequestId);

    }
}
