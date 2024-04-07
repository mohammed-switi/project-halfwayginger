package edu.bethlehem.scinexus.repository;

import edu.bethlehem.scinexus.JPARepository.PostRepository;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.Post.Post;
import edu.bethlehem.scinexus.User.Position;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)

public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;



    private  User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .firstName("Mohammed")
                .lastName("Sowaity")
                .username("mohammed")
                .email("mohammed@example.com")
                .password("MustEncrypt")
                .role(Role.ACADEMIC)
                .position(Position.ASSISTANT_PROFESSOR)
                .education("Bethlehem University")
                .phoneNumber("0593333333")
                .bio("NOTHING")
                .fieldOfWork("Software")
                .build();

        userRepository.save(user);
    }
    @Test
    public void Post_SaveAll_ReturnPost() {
        Post post = Post.builder()
                .content("Content 1") // Provide actual content
                .visibility(Visibility.PUBLIC) // Provide actual visibility
                .publisher(user) // Assuming getUserByPublisherId() returns a User entity based on the provided user ID
                .build();

        Post savedPost = postRepository.save(post);

        Assertions.assertThat(savedPost).isNotNull();
        Assertions.assertThat(savedPost.getId()).isNotNull();
    }

    @Test
    public void Post_GetAll_ReturnMoreThanOnePost() {
        Post post1 = Post.builder()
                .content("Content 1")
                .visibility(Visibility.PUBLIC)
                .publisher(user)
                .build();

        Post post2 = Post.builder()
                .content("Content 2")
                .visibility(Visibility.PRIVATE)
                .publisher(user)
                .build();

        postRepository.saveAll(List.of(post1, post2));

        List<Post> posts = postRepository.findAll();

        Assertions.assertThat(posts).isNotNull();
        Assertions.assertThat(posts.size()).isEqualTo(2);
    }

    @Test
    public void Post_FindByID_GetById() {
        Post post = Post.builder()
                .content("Content")
                .visibility(Visibility.PUBLIC)
                .publisher(user)
                .build();

        postRepository.save(post);

        Optional<Post> foundPost = postRepository.findById(post.getId());

        Assertions.assertThat(foundPost).isPresent();
    }

    @Test
    public void Post_FindByStatus_ReturnPostNotNull() {
        Post post = Post.builder()
                .content("Content")
                .visibility(Visibility.PUBLIC)
                .publisher(user)
                .build();

        postRepository.save(post);

        Optional<Post> foundPost = postRepository.findByVisibility(Visibility.PUBLIC);

        Assertions.assertThat(foundPost).isPresent();
    }

    @Test
    public void Post_UpdatePost_ReturnPostNotNull() {
        Post post = Post.builder()
                .content("Content")
                .visibility(Visibility.PUBLIC)
                .publisher(user)
                .build();

        Post savedPost = postRepository.save(post);

        savedPost.setVisibility(Visibility.PRIVATE);
        savedPost.setContent("Updated Content");

        Post updatedPost = postRepository.save(savedPost);

        Assertions.assertThat(updatedPost.getVisibility()).isEqualTo(Visibility.PRIVATE);
        Assertions.assertThat(updatedPost.getContent()).isEqualTo("Updated Content");
    }

    @Test
    public void Post_DeletePost_ReturnPostNotNull() {
        Post post = Post.builder()
                .content("Content")
                .visibility(Visibility.PUBLIC)
                .publisher(user)
                .build();

        Post savedPost = postRepository.save(post);

        postRepository.deleteById(savedPost.getId());

        Optional<Post> deletedPost = postRepository.findById(savedPost.getId());

        Assertions.assertThat(deletedPost).isEmpty();
    }

    // Method to create and save a user (you can replace it with your actual implementation)

}
