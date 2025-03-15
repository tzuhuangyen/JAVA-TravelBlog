package com.blog.travelblogapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String username;
//    @JsonIgnore 用這個註解會沒辦法驗證密碼!
    private String password;
    private String email;

//    @JsonManagedReference// 放在多的屬性上方
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL) //user的操作會影響到Posts
    private List<Posts> post;

}
