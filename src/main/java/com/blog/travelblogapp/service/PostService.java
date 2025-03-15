package com.blog.travelblogapp.service;

import com.blog.travelblogapp.model.Posts;
import com.blog.travelblogapp.model.User;
import com.blog.travelblogapp.repo.PostRepo;
import com.blog.travelblogapp.repo.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepo postRepo;
    @Autowired
    private UserRepo userRepo;


    public Posts addPost(Posts post, MultipartFile imageFile, String username) throws IOException {
        post.setImageName(imageFile.getOriginalFilename());
        post.setImageType(imageFile.getContentType());
        post.setImageData(imageFile.getBytes());

        User user = userRepo.findByUsername(username);
        post.setAuthorId(user);
        post.setAuthor(user.getUsername());

        return postRepo.save(post);

    }

    public Posts updatePost(int id, Posts existingPost, Posts post, MultipartFile imageFile) throws IOException {

        //檢查沒傳檔案 and 傳空的檔案
        if (imageFile != null && !imageFile.isEmpty()){
            existingPost.setImageName(imageFile.getOriginalFilename());
            existingPost.setImageType(imageFile.getContentType());
            existingPost.setImageData(imageFile.getBytes());
        }
        if (post.getTitle() != null)
            existingPost.setTitle(post.getTitle());

        if (post.getContent() != null)
            existingPost.setContent(post.getContent());

        if (post.getTag() != null)
            existingPost.setTag(post.getTag());

        existingPost.setView(post.getView());
        existingPost.setLikeCount(post.getLikeCount());

        return postRepo.save(existingPost);
    }

    //用true false的方法
//    public boolean updatePost(int id, Posts existingPost, Posts post, MultipartFile imageFile) throws IOException {
//
//        Posts existingPost = postRepo.findById(id).orElse(null);
//
//        if(existingPost == null){
//            return false;
//        }
//
//        //檢查沒傳檔案 and 傳空的檔案
//        if (imageFile != null && !imageFile.isEmpty()){
//            existingPost.setImageName(imageFile.getOriginalFilename());
//            existingPost.setImageType(imageFile.getContentType());
//            existingPost.setImageData(imageFile.getBytes());
//        }
//        if (post.getTitle() != null)
//            existingPost.setTitle(post.getTitle());
//
//        if (post.getContent() != null)
//            existingPost.setContent(post.getContent());
//
//        if (post.getTag() != null)
//            existingPost.setTag(post.getTag());
//
//        existingPost.setView(post.getView());
//
//        postRepo.save(post);
//        return  true;
//    }

    public Posts getPostById(int id) {
        return postRepo.findById(id).orElse(new Posts(-1));

    }

    public List<Posts> getAllPosts() {
        return postRepo.findAll();
    }

    public void deletePost(int id) {
        postRepo.deleteById(id);
    }

    public List<Posts> searchKeyword(String keyword) {

        return  postRepo.searchPosts(keyword);
    }


}
