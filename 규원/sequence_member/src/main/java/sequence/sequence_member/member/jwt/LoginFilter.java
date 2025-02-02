//package sequence.sequence_member.member.jwt;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import sequence.sequence_member.member.service.TokenReissueService;
//
//public class LoginFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final TokenReissueService tokenReissueService;
//    private final AuthenticationManager authenticationManager;
//    private final JWTUtil jwtUtil;
//
//    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, TokenReissueService tokenReissueService) {
//        this.authenticationManager = authenticationManager;
//        this.jwtUtil = jwtUtil;
//        this.tokenReissueService = tokenReissueService;
//        //spring security는 대부분의 로직이 필터 단에서 동작하게 된다. 로그인 또한, 필터에서 처리되고, (자동으로 엔드포인트는 "/login" 이 된다.)
//        //UsernamePasswordAuthenticationFilter에서 매핑되어 처리된다. 이 필터를 상속받아 LoginFilter를 만들게 된다.
//        //security에서 설정해주는 기본 url("/login")을 /api/login으로 변경
//        setFilterProcessesUrl("/api/login");
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//
//        //클라이언트 요청으로 부터 username, password를 추출한다.
//        String username = obtainUsername(request);
//        String password = obtainPassword(request);
//
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
//
//        return authenticationManager.authenticate(authToken);
//
//    }
//
//    //로그인 성공시 실행하는 메서드 (여기서 jwt를 발급하면 된다)
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication){
//        //유저 이름 찾기
//        String username = authentication.getName();
//
//        String access = jwtUtil.createJwt("access",username, 600000L);
//        String refresh = jwtUtil.createJwt("refresh", username, 86400000L);
//
//        tokenReissueService.RefreshTokenSave(username,refresh,86400000L);
//
//        response.setHeader("access", access);
//        response.addCookie(createCookie("refresh", refresh));
//        response.setStatus(HttpStatus.OK.value());
//    }
//
//    //로그인 실패시 실행하는 메서드
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException failed){
//        response.setStatus(401);
//    }
//
//    private Cookie createCookie(String key, String value) {
//
//        Cookie cookie = new Cookie(key, value);
//        cookie.setMaxAge(24*60*60);
////        cookie.setSecure(true); // https일 경우 설정
////        cookie.setPath("/"); // 쿠키의 적용 범위 설정
//
//        //js로 쿠키에 접근 못하게 함
//        cookie.setHttpOnly(true);
//
//        return cookie;
//    }
//}
package sequence.sequence_member.member.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sequence.sequence_member.member.dto.LoginDTO;
import sequence.sequence_member.member.service.TokenReissueService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenReissueService tokenReissueService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, TokenReissueService tokenReissueService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.tokenReissueService = tokenReissueService;
        setFilterProcessesUrl("/api/login"); // 🔥 Fix: Ensure correct login URL is set
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            BufferedReader reader = request.getReader();
            String jsonRequest = reader.lines().collect(Collectors.joining());

            System.out.println("📩 로그인 요청 데이터: " + jsonRequest);

            ObjectMapper objectMapper = new ObjectMapper();
            LoginDTO loginDTO = objectMapper.readValue(jsonRequest, LoginDTO.class);

            String username = loginDTO.getUsername();
            String password = loginDTO.getPassword();

            System.out.println("🔍 로그인 시도: " + username);
            System.out.println("🔐 입력된 비밀번호: " + password);

            if (username == null || password == null) {
                throw new AuthenticationException("아이디 또는 비밀번호가 없습니다.") {};
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(authToken);

            System.out.println("✅ 로그인 성공!");

            return authentication;
        } catch (IOException e) {
            throw new AuthenticationException("잘못된 로그인 요청 형식입니다.") {};
        } catch (AuthenticationException e) {
            System.out.println("❌ 로그인 실패: " + e.getMessage());
            throw e;
        }
    }



    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        // 🔥 로그인한 사용자의 username 가져오기
        String username = authentication.getName();

        // 🔥 JWT 토큰 생성 (access + refresh)
        String access = jwtUtil.createJwt("access", username, 600000L);
        String refresh = jwtUtil.createJwt("refresh", username, 86400000L);

        // 🔥 Refresh 토큰을 DB에 저장
        tokenReissueService.RefreshTokenSave(username, refresh, 86400000L);

        // 🔥 응답 헤더에 Access 토큰 추가
        response.setHeader("access", access);

        // 🔥 Refresh 토큰을 쿠키에 추가 (HttpOnly 설정)
        response.addCookie(createCookie("refresh", refresh));

        // 🔥 JSON 응답으로 Access 토큰과 Refresh 토큰 반환
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON 형식으로 응답
        String jsonResponse = String.format("{ \"accessToken\": \"%s\", \"refreshToken\": \"%s\" }", access, refresh);
        response.getWriter().write(jsonResponse);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    // 🔥 Refresh 토큰을 HttpOnly 쿠키로 저장
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60); // 1일 (초 단위)
        cookie.setHttpOnly(true); // JavaScript에서 접근 불가
        cookie.setPath("/"); // 모든 경로에서 사용 가능
        return cookie;
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    //ㅃ
}


