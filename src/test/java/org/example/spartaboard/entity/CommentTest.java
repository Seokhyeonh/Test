package org.example.spartaboard.entity;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CommentTest {

    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserid("testuser");
        user.setUsername("Test User");

        post = new Post();
        post.setTitle("Test Post");
        post.setContents("This is a test post");
        post.setUser(user);
    }

    @Test
    void testCommentCreation() {
        // Given
        String commentContent = "This is a test comment";

        // When
        Comment comment = new Comment(post, user, commentContent);

        // Then
        assertNotNull(comment);
        assertEquals(commentContent, comment.getContents());
        assertEquals(post, comment.getPost());
        assertEquals(user, comment.getUser());
    }

    @Test
    void testUpdateComment_ValidContent() {
        // Given
        String initialContent = "Initial comment";
        Comment comment = new Comment(post, user, initialContent);

        String updatedContent = "Updated comment";

        // When
        comment.updateComment(updatedContent);

        // Then
        assertEquals(updatedContent, comment.getContents());
    }

    @Test
    void testUpdateComment_NullOrBlankContent() {
        // Given
        String initialContent = "Initial comment";
        Comment comment = new Comment(post, user, initialContent);

        // When
        comment.updateComment(null);
        String afterNullUpdate = comment.getContents();
        comment.updateComment("");
        String afterBlankUpdate = comment.getContents();
        comment.updateComment("   ");
        String afterWhitespaceUpdate = comment.getContents();

        // Then
        assertEquals(initialContent, afterNullUpdate);
        assertEquals(initialContent, afterBlankUpdate);
        assertEquals(initialContent, afterWhitespaceUpdate);
    }

    @Test
    void testCommentRelationships() {
        // Given
        String commentContent = "Relationship test comment";

        // When
        Comment comment = new Comment(post, user, commentContent);

        // Then
        assertEquals(post, comment.getPost());
        assertEquals(user, comment.getUser());
        assertEquals(commentContent, comment.getContents());
    }
}