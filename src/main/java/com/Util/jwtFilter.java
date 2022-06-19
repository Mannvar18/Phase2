package com.Util;



import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class jwtFilter extends OncePerRequestFilter{

    @Autowired
    UserDetailsService userService;

    @Autowired
    jwtUtil jutil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
                final String authHeader=request.getHeader("Authorization");
                String username=null;
                String jwt=null;
                if(authHeader!=null && authHeader.startsWith("Bearer "))
                {
                    jwt=authHeader.substring(7);
                    username=jutil.extractUsername(jwt);

                }
                if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
                {
                    UserDetails userdetails=this.userService.loadUserByUsername(username);
                    if(jutil.validateToken(jwt, userdetails))
                    {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userdetails,null,userdetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
                filterChain.doFilter(request, response);
        
    }
    
}

