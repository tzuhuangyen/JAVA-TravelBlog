package com.blog.travelblogapp.repo;

import com.blog.travelblogapp.model.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepo extends JpaRepository<Posts, Integer> {

    @Query("SELECT p from Posts p WHERE " +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.imageName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.tag) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.authorId.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "CAST(p.createdAt AS string ) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "CAST(p.updatedAt AS string) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Posts> searchPosts(String keyword);
}
