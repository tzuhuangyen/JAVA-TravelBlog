package com.blog.travelblogapp.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

// 實現userDetails
public class UserPrincipal implements UserDetails {

    private User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    // 定義使用者權限有幾個
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //只設定一個
        return Collections.singleton(new SimpleGrantedAuthority("USER")) ;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 帳號是否過期
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    //帳號是否被封鎖
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 密碼是否過期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 帳號是否已經啟用
    @Override
    public boolean isEnabled() {
        return true;
    }
}
