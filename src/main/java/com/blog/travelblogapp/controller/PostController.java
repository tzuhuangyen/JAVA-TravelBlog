package com.blog.travelblogapp.controller;

import com.blog.travelblogapp.model.Posts;
import com.blog.travelblogapp.service.JwtService;
import com.blog.travelblogapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/travelPost")
@CrossOrigin("http://localhost:3000")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/posts")
    public ResponseEntity<List<Posts>> getPosts(){
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<?> getPostById(@PathVariable int id){

        Posts post = postService.getPostById(id);

        if (post.getPostId()>0)
            return new ResponseEntity<>(post, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/post")
    public ResponseEntity<?> addPost(@RequestPart Posts post, @RequestPart MultipartFile imageFile, @RequestHeader("Authorization") String authorizationHeader){

        String token = authorizationHeader.substring(7);

        String username = jwtService.extractUserName(token);

        Posts savedPost = null;
        try {
            savedPost = postService.addPost(post, imageFile, username);
            return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<?> updatePost(@PathVariable int id,
                                        @RequestPart Posts post,
                                        @RequestPart(required = false) MultipartFile imageFile //false表示，可以是空照片
                                        ){
        Posts existingPost = postService.getPostById(id);
        try{
        if (existingPost.getPostId()>0){
            Posts updatePost = postService.updatePost(id, existingPost, post, imageFile);
            return new ResponseEntity<>(updatePost, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {// IOE通常跟檔案有關，這裡是照片
            return new ResponseEntity<>("Failed to process image"+e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Unexpected error " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }



//        boolean updated = false;
//        try {
//            updated = postService.updatePost(id, post, imageFile);
//            if(!updated)
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
//        } catch (IOException e) {// IOE通常跟檔案有關
//            return new ResponseEntity<>("Failed to process image"+e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return new ResponseEntity<>("Unexpected error " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }



    @DeleteMapping("post/{id}")
    public ResponseEntity<String> deletePost(@PathVariable int id){
        Posts post = postService.getPostById(id);

        if (post.getPostId() >0){
            postService.deletePost(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("posts/search")
    //url -> search?keyword=(使用者輸入內容)
    public ResponseEntity<List<Posts>> searchPost(@RequestParam String keyword){
        List<Posts> posts = postService.searchKeyword(keyword);
        System.out.println("searching with" + keyword);

        return new ResponseEntity<>(posts, HttpStatus.OK);

    }


}
