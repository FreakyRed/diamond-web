package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.comment.CommentException;
import sk.tuke.gamestudio.service.comment.CommentService;
import sk.tuke.gamestudio.service.comment.CommentServiceJDBC;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommentServiceJDBCTest {

    @Test
    public void addComment() throws CommentException {
        CommentService commentService = new CommentServiceJDBC();
        Comment comment = new Comment("Martin", "TestAdd", "Random comment", new Timestamp(new Date().getTime()));

        commentService.addComment(comment);
        List<Comment> listComment = commentService.getComments("TestAdd");

        assertEquals(1, listComment.size());
        assertEquals("Martin", listComment.get(0).getPlayer());
        assertEquals("Random comment", listComment.get(0).getComment());
    }

    @Test
    public void testCommentExceptionThrownWhenNullIsPassed() {
        CommentService commentService = new CommentServiceJDBC();
        Comment comment = new Comment("Martin", null, "Random comment", new Timestamp(new Date().getTime()));

        assertThrows(CommentException.class, () -> commentService.addComment(comment));
    }
}
