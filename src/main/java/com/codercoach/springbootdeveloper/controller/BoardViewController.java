package com.codercoach.springbootdeveloper.controller;

import com.codercoach.springbootdeveloper.domain.Board;
import com.codercoach.springbootdeveloper.dto.BoardListViewResponse;
import com.codercoach.springbootdeveloper.dto.BoardViewResponse;
import com.codercoach.springbootdeveloper.service.BoardService;
import com.example.util.MarkdownUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardViewController {

    private final BoardService boardService;

    @GetMapping(value = "/boards", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getBoards(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int size,
                            HttpServletRequest request,
                            Model model) {
        Page<BoardListViewResponse> boardPage = boardService.findAll(page, size);
        model.addAttribute("boardPage", boardPage);
        model.addAttribute("currentUri", request.getRequestURI());

        return "board/boardList";
    }

    @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getBoard(@PathVariable long bno, Model model) {
        Board board = boardService.findById(bno);

        String html = MarkdownUtil.toHtml(board.getContent());

        model.addAttribute("board", new BoardViewResponse(board));
        model.addAttribute("contentHtml", html);

        return "board/board";
    }

    @GetMapping(value = "/new-board", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String newBoard(@RequestParam(required = false) Long bno, Model model) {
        if (bno == null) {
            model.addAttribute("board", new BoardViewResponse());
        } else {
            Board board = boardService.findById(bno);
            model.addAttribute("board", new BoardViewResponse(board));
        }
        return "board/newBoard";
    }
}
