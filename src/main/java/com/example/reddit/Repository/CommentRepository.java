package com.example.reddit.Repository;

import com.example.reddit.Models.Comment;
import com.example.reddit.Models.Post;
import com.example.reddit.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}