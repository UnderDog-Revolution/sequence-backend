package sequence.sequence_member.mypage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sequence.sequence_member.member.response.ApiResponseData;
import sequence.sequence_member.member.response.Code;
import sequence.sequence_member.mypage.dto.MyPageDTO;
import sequence.sequence_member.member.response.ResponseMsg;
import sequence.sequence_member.mypage.service.MyPageService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/api/mypage")
    public ResponseEntity<ApiResponseData> getMyPage(@RequestParam Long userId) {
        try {
            MyPageDTO myPageDTO = myPageService.searchMyPage(userId);
            // 성공 응답 생성
            return ResponseEntity.ok(ApiResponseData.success(myPageDTO, "사용자 정보를 성공적으로 가져왔습니다."));
        } catch (Exception e) {
            // 예외 발생 시 처리
            ApiResponseData errorResponse = ApiResponseData.failure(
                    Code.CAN_NOT_FIND_RESOURCE.getCode(),
                    e.getMessage()
            );
            return ResponseEntity.status(Code.CAN_NOT_FIND_RESOURCE.getStatus()).body(errorResponse);
        }
    }

    @PutMapping("/api/mypage")
    public ResponseEntity<ResponseMsg> updateMyPageInfo(@RequestBody MyPageDTO myPageDTO) {
        try {
            myPageService.updateMyPage(myPageDTO);
            // 성공 응답 생성
            ResponseMsg response = new ResponseMsg(
                    Code.SUCCESS.getCode(),
                    Code.SUCCESS.getMessage(),
                    Map.of("message", "마이페이지 정보가 성공적으로 수정되었습니다.")
            );
            return ResponseEntity.status(Code.SUCCESS.getStatus()).body(response);
        } catch (Exception e) {
            // 예외 발생 시 처리
            ResponseMsg errorResponse = new ResponseMsg(
                    Code.CAN_NOT_FIND_RESOURCE.getCode(),
                    Code.CAN_NOT_FIND_RESOURCE.getMessage(),
                    Map.of("error", e.getMessage())
            );
            return ResponseEntity.status(Code.CAN_NOT_FIND_RESOURCE.getStatus()).body(errorResponse);
        }
    }
}
