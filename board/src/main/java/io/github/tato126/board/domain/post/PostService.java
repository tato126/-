package io.github.tato126.board.domain.post;

import io.github.tato126.board.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Post createPost(String title, String content, String author) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
        return postRepository.save(post);
    }

    public Page<Post> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post", id));
    }

    @Transactional
    public Post updatePost(Long id, String title, String content) {
        Post post = getPost(id);
        post.update(title, content);
        return post;
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = getPost(id);
        postRepository.delete(post);
    }
}
