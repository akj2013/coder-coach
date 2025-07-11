package com.codercoach.springbootdeveloper.dto;

import com.codercoach.springbootdeveloper.domain.Board;
import com.example.util.MarkdownUtil;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardListViewResponse {

    private final Long bno;
    private final String title;
    private final String content;
    private final String contentHtml;
    private final LocalDateTime createdAt;
    private final String writer;

    public BoardListViewResponse(Board board) {
        this.bno = board.getBno();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.contentHtml = MarkdownUtil.toHtml(board.getContent());
        this.createdAt = board.getCreatedAt();
        this.writer = board.getWriter();
    }
}
