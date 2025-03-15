package com.blog.travelblogapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;
    private String title;
    private String content;

//    @JsonBackReference //放在一的屬性上面
    @JsonIgnoreProperties({"post", "password"}) //忽略掉User裡面的post屬性！ 這樣就不會無限遞迴拉~
    @ManyToOne
    private User authorId;
    private String author;
    //只儲存一個圖片，未來優化儲存多張
    private String imageName;
    private String imageType;
    @Lob
    private byte[] imageData;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String tag;
    private int view;
    private int likeCount;

    public Posts(int postId) {
        this.postId = postId;
    }
}
