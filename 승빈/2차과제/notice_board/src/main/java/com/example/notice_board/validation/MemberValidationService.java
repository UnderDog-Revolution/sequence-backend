package com.example.notice_board.validation;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MemberValidationService {

    private final WebClient webClient;

    public MemberValidationService(WebClient webClient) {
        this.webClient = webClient;
    }

    public boolean isMemberValid(Long userId) {
        try {
            // 멤버 서비스의 검증 API 호출
            return webClient.get()
                    .uri("/api/member/validate/{id}/exists", userId) // {id} 경로 변수 전달
                    .retrieve()
                    .bodyToMono(Boolean.class) // Boolean 결과 반환
                    .block(); // 비동기 호출을 동기로 처리
        } catch (Exception e) {
            // 통신 실패 시 예외 처리
            System.err.println("Failed to validate member: " + e.getMessage());
            return false;
        }
    }
}
