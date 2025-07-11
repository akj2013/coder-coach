package com.codercoach.springbootdeveloper.controller;

import com.codercoach.springbootdeveloper.domain.Board;
import com.codercoach.springbootdeveloper.dto.AddBoardRequest;
import com.codercoach.springbootdeveloper.dto.BoardResponse;
import com.codercoach.springbootdeveloper.dto.UpdateBoardRequest;
import com.codercoach.springbootdeveloper.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController             // HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping(value = "/api/boards", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Board> addBoard(@RequestBody AddBoardRequest request, Principal principal) {
        Board savedBoard = boardService.save(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBoard);
    }

    @GetMapping(value = "/api/boards", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<BoardResponse>> findAllBoards(@RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        List<BoardResponse> boards = boardService.findAll()
                .stream()
                .map(BoardResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(boards);
    }

    @GetMapping(value = "/api/board/{bno}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<BoardResponse> findBoard(@PathVariable long bno) {
        Board board = boardService.findById(bno);
        return ResponseEntity.ok()
                .body(new BoardResponse(board));
    }

    @DeleteMapping(value = "/api/board/{bno}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> deleteBoard(@PathVariable long bno) {
        boardService.delete(bno);
        return ResponseEntity.ok()
                .build();
    }

    @PutMapping(value = "/api/board/{bno}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Board> updateBoard(@PathVariable long bno, @RequestBody UpdateBoardRequest request) {
        Board updateBoard = boardService.update(bno, request);

        return ResponseEntity.ok()
                .body(updateBoard);
    }

}
