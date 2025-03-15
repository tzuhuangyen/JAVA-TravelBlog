package com.blog.travelblogapp.filter;

import com.blog.travelblogapp.service.JwtService;
import com.blog.travelblogapp.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//認證JWT
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // get Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        //check header
        if (authHeader != null && authHeader.startsWith("Bearer")){
            // get jwt and username
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
        }

        // username exist and not authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){

            // avoid Cyclic dependency
            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);

            if (jwtService.validateToken(token, userDetails)){
                // 驗證成功會返回 UsernamePasswordAuthenticationToken
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null, userDetails.getAuthorities());//第一個參數principle

                // 上傳request(前端傳來的客戶資訊)進去UsernamePasswordAuthentication的token裡面
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //把這個 UsernamePasswordAuthentication的token傳給SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // continue filter chain
        filterChain.doFilter(request, response);

    }
}
