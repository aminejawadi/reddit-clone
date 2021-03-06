package com.example.reddit.Repository;

import com.example.reddit.Models.Post;
import com.example.reddit.Models.Subreddit;
import com.example.reddit.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}