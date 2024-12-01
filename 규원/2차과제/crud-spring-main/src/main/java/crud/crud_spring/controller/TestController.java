package crud.crud_spring.controller;

import crud.crud_spring.entity.Board;
import crud.crud_spring.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
@RequiredArgsConstructor
public class TestController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @PostMapping("/test/write")
    public String testWrite(Board board, Model model,
                            @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {

        boardService.write(board, file);

        model.addAttribute("message", "게시글 작성이 완료 되었습니다.");
        model.addAttribute("searchUrl", "/test/list");

        return "message";
    }

    @GetMapping("/test/list")
    public String testList(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                           @RequestParam(name = "searchKeyword", defaultValue = "") String searchKeyword){

        Page<Board> list = null;

        if(searchKeyword==null){
            list = boardService.boardList(pageable);
        }else {
            list = boardService.boardSearchList(searchKeyword, pageable);
        }

        int nowPage=list.getPageable().getPageNumber() + 1;
        int startPage=Math.max(nowPage-4,1);
        int endPage=Math.min(nowPage+5, list.getTotalPages());

        model.addAttribute("list",list);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);

        return "boardList";
    }

    @GetMapping("/test/view")
    public String testView(Model model, @RequestParam("id") Integer id){
        model.addAttribute("board",boardService.boardView(id));
        return "boardView";
    }

    @GetMapping("/test/delete")
    public String testDelete(@RequestParam("id") Integer id){
        boardService.boardDelete(id);
        return "redirect:/test/list";
    }

    @GetMapping("/test/modify/{id}")
    public String testModify(Model model, @PathVariable("id") Integer id){
        model.addAttribute("board",boardService.boardView(id));
        return "boardModify";
    }

    @PostMapping("/test/update/{id}")
    public String testUpdate(@PathVariable("id") Integer id, Board board, Model model,@RequestParam(name="file", required = false) MultipartFile file) throws Exception {
        Board temp = boardService.boardView(id);
        //아래와 같이 덮어씌우기 방식은 jpa에서 사용하면 안된다.
        //jpa 변경감지 기능을 사용해야 한다.
        //그 개념이 방대하기 때문에 여기서는 그냥 덮어 씌우기를 사용한다.
        temp.setTitle(board.getTitle());
        temp.setContents(board.getContents());
        //여기서는 새롭게 글이 작성되어서 추가되는 것이 아니라,,
        //놀랍게도 해당 board를 바뀐 내용으로 업데이트 해준다는 것!
        boardService.write(temp, file);

        model.addAttribute("message", "게시글 수정이 완료되었습니다");
        model.addAttribute("searchUrl","/test/view?id="+id);

        return "message";
    }

    @PostMapping("/test/login/validate")
    public ResponseEntity<String> validate(@RequestParam(name = "user_id") String user_id, @RequestParam(name = "randomKey") String randomKey){
        boolean validate = boardService.validateUser(user_id,randomKey);
        if(!validate) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 인증 실패");

        return ResponseEntity.ok("게시글 작성이 가능합니다.");
    }
}
