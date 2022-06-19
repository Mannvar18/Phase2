package com.Security;

//import com.Repository.EmailRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    public void configure(HttpSecurity httpSecurity) throws Exception {
////        httpSecurity
////                .antMatcher("/**").authorizeRequests()
////                .antMatchers("/").permitAll()
////                .anyRequest().authenticated();
////                .and()
////                .oauth2Login();
//
////        httpSecurity
////                .authorizeRequests()
////                .antMatchers("/", "/login", "/oauth/**").permitAll()
////                .antMatchers("/**").authenticated ()
////                .anyRequest().permitAll()
////                .and()
////                .formLogin()
////                .loginPage("/login")
////                .usernameParameter("email")
////                .permitAll()
////                .defaultSuccessUrl("/")
////                .and ()
////                .oauth2Login()
//////                .loginPage("/login")
//////                .userInfoEndpoint()
//////                .userService (oAuth2UserService)
//////                .and()
//////                .successHandler(oAuth2LoginSuccessHandler)
////                .and()
////                .logout ().permitAll()
////                .and()
//////                .rememberMe()
//////                .tokenRepository(persistentTokenRepository())
////            ;
//
//    }
//
////    @Autowired
////    private CustomOAuth2UserService oAuth2UserService;
////
////    @Autowired
////    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
//}
