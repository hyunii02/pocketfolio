package com.ssafy.pocketfolio.config;


import com.ssafy.pocketfolio.security.filter.ApiCheckFilter;
import com.ssafy.pocketfolio.security.handler.CustomLogoutSuccessHandler;
import com.ssafy.pocketfolio.security.handler.LoginSuccessHandler;
import com.ssafy.pocketfolio.security.service.OAuthService;
import com.ssafy.pocketfolio.security.service.UserDetailsServiceImpl;
import com.ssafy.pocketfolio.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private OAuthService oAuthService;

    @Value("${server.servlet.context-path:''}")
    private String contextPath;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //AuthenticationManager??????
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        // Get AuthenticationManager
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        //????????? ??????
        http.authenticationManager(authenticationManager);

//        // ?????? ??????
//        http.authorizeHttpRequests((auth) -> {
//            auth
//                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/login", "/logout", "/swagger",
//                            "/swagger/**", "/swagger-ui/**", "/users/signup", "/users/login", "/users/logout").permitAll()
//                    .antMatchers(HttpMethod.GET, "/rooms/like", "/portfolios/room/*", "/users/profile").authenticated()
//                    .antMatchers(HttpMethod.GET, "/**").permitAll()
//                    .anyRequest().authenticated();
////            auth.antMatchers("/sample/member").hasRole("USER");
//        });

//        http.authorizeHttpRequests((auth) -> {
//            auth.anyRequest().permitAll();
//        });

//        http.formLogin(); // ?????? ??? ????????? ??? ?????? ????????? ???????????? ??????
        http.csrf().disable(); // CSRF ?????? ?????? X
//        http.oauth2Login(); // OAuth ?????????
        http.oauth2Login().successHandler(successHandler()); // OAuth ????????? ??? redirect ??????
        http.logout().logoutUrl("/oauth/logout").logoutSuccessHandler(logoutSuccessHandler());

//        http.rememberMe().tokenValiditySeconds(60*60*24*7).userDetailsService(userDetailsService);
        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.cors();

//        http.addFilterBefore(apiLoginFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**");
    }

//    public ApiLoginFilter apiLoginFilter(AuthenticationManager authenticationManager) throws Exception {
//
//        ApiLoginFilter apiLoginFilter =  new ApiLoginFilter("/users/login", jwtUtil());
//        apiLoginFilter.setAuthenticationManager(authenticationManager);
//
//        apiLoginFilter
//                .setAuthenticationFailureHandler(new ApiLoginFailHandler());
//
//        return apiLoginFilter;
//    }


    @Bean
    public JWTUtil jwtUtil() {
        return new JWTUtil();
    }

    @Bean
    public LoginSuccessHandler successHandler() {
        return new LoginSuccessHandler(passwordEncoder(), jwtUtil(), oAuthService);
    }

    @Bean
    public CustomLogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler(jwtUtil(), oAuthService);
    }

    @Bean
    public ApiCheckFilter apiCheckFilter() {
//        String[] patterns = {
//                contextPath + "/rooms/like", contextPath + "/portfolios/room/*", contextPath + "/users/profile",
//                contextPath + "/users/search**", contextPath + "/follows/follower", contextPath + "/follows/following",
//                contextPath + "/users/search**", contextPath + "/follows/**", contextPath + "/rooms/*"
//        };
//        // GET??? ?????? ????????? ????????? ?????? ?????????

        String[] postForGuestPatterns = {contextPath + "/users/logout"};
        // POST??? ?????? ????????? ?????? ?????? ?????????

//        return new ApiCheckFilter(patterns, postForGuestPatterns, jwtUtil()); // ! patterns ??? ???????????? ?????? ?????? ???
        return new ApiCheckFilter(postForGuestPatterns, jwtUtil()); // ! patterns ??? ???????????? ?????? ?????? ???
    }

//    public ApiLoginFilter apiLoginFilter(AuthenticationManager authenticationManager) throws Exception{
//
//        ApiLoginFilter apiLoginFilter =  new ApiLoginFilter("/api/login", jwtUtil());
//        apiLoginFilter.setAuthenticationManager(authenticationManager);
//
//        return apiLoginFilter;
//    }

}
