package io.github.tato126.board.domain.comment;

import io.github.tato126.board.common.exception.NotFoundException;
import io.github.tato126.board.domain.post.Post;
import io.github.tato126.board.domain.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public Comment createComment(Long postId, String content, String author) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post", postId));

        Comment comment = Comment.builder()
                .content(content)
                .author(author)
                .post(post)
                .build();
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment createReply(Long commentId, String content, String author) {
        Comment parent = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment", commentId));

        Comment reply = Comment.builder()
                .content(content)
                .author(author)
                .post(parent.getPost())
                .parent(parent)
                .build();
        return commentRepository.save(reply);
    }

    public List<Comment> getComments(Long postId) {
        return commentRepository.findByPostIdAndParentIsNull(postId);
    }

    @Transactional
    public Comment updateComment(Long id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment", id));
        comment.updateContent(content);
        return comment;
    }

    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment", id));
        commentRepository.delete(comment);
    }
}
