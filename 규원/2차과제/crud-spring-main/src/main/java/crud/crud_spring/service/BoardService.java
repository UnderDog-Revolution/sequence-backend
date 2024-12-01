package crud.crud_spring.service;

import crud.crud_spring.entity.Board;
import crud.crud_spring.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    private final RestTemplate restTemplate;

    public BoardService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //게시글 작성
    public void write(Board board, MultipartFile file) throws Exception {

        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";

        //파일 이름 앞에 붙일 랜덤 이름 새성
        UUID uuid = UUID.randomUUID();

        String fileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath, fileName);

        file.transferTo(saveFile);

        board.setFilename(fileName);
        board.setFilepath("/files/"+fileName);

        boardRepository.save(board);
    }

    //게시글 전부 불러오기
    public Page<Board> boardList(Pageable pageable){
        return boardRepository.findAll(pageable);
    }

    //특정 게시글 불러오기
    public Board boardView(Integer id){
        return boardRepository.findById(id).get();
    }

    //게시글 삭제하기
    public void boardDelete(Integer id){
        boardRepository.deleteById(id);
    }

    //키워드를 포함한 게시글 불러오기
    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable){
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    //8080포트로 요청을 보내어 로그인 검사
    public boolean validateUser(String user_id, String randomKey){
        String url = "http://localhost:8080/api/member/validate";  // 8080 서버의 API 엔드포인트

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("user_id", user_id)
                .queryParam("randomKey", randomKey);

        // GET 요청 보내기
        ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
        return response.getStatusCode().is2xxSuccessful();
    }

}
