package com.example.security3.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.security3.config.auth.PrincipalDetails;
import com.example.security3.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

// UsernamePasswordAuthenticationFilter: /login URL로 username, password 파라미터로 넘겨주면 잘 동작한다.

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // PrincipalDetailsService 클래스의 loadUserByname 함수가 실행된다.
            // Authentication 객체에는 유저의 로그인 정보가 담긴다. (로그인이 정상적으로 실행되었을 경우)
            Authentication authentication = authenticationManager.authenticate(token);

            // 리턴되면서 Authentication 객체가 세션 영역에 저장된다.
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // attemptAuthentication 메소드에서 인증이 성공적으로 진행되면 이 함수가 실행된다.
    // 앞에서는 인증을 하고, 여기서 JWT 토큰을 발급하면 된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * 60 * 10)))
                        .withClaim("id", principalDetails.getUser().getId())
                                .withClaim("username", principalDetails.getUsername())
                                        .sign(Algorithm.HMAC256("3Q3Min"));

        response.addHeader("Authorization", "Bearer " + jwtToken);
    }
}
