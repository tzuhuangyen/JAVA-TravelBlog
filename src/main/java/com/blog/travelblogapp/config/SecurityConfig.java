package com.blog.travelblogapp.config;

import com.blog.travelblogapp.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // tell spring this is a setting class and manage  for us
@EnableWebSecurity // spring security載入自定義授權和認證到全域WebSecurity
public class SecurityConfig {

    @Autowired //會自動從我創的MyUserDetailsService 注入
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    // replace the default spring AuthenticationProvider with our custom implementation
    @Bean //AuthenticationProvider is used to authentication username and password 認證帳號密碼
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){
        // 實現這個interface
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        //connect database
        provider.setUserDetailsService(userDetailsService);

        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));

        return provider;
    }


    @Bean
    //預設SecurityFilterChain是使用HttpSecurity 來進行配置的！
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http    //進用csrf
                .csrf(customizer->customizer.disable())
                //所有請求要授權
                .authorizeHttpRequests(request->request
                        .requestMatchers("/api/user/register", "api/user/login")//選定這些網址
                        .permitAll()//不用授權訪問
                        .anyRequest().authenticated())
                //讓我們能提供帳密給伺服器
                .httpBasic(Customizer.withDefaults())
                //讓session是無狀態的
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);//在這個filter加上我們自訂義的JwtFilter

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
