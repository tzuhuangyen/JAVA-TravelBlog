package com.blog.travelblogapp.repo;

import com.blog.travelblogapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    Boolean existsByUsername(String username);
}
