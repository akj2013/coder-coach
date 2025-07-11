package com.codercoach.springbootdeveloper.dto;

import com.codercoach.springbootdeveloper.domain.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class BoardViewResponse {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String author;

    public BoardViewResponse(Board board) {
        this.id = board.getBno();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
        this.author = board.getWriter();
    }
}
