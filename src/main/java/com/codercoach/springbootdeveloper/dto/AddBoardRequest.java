package com.codercoach.springbootdeveloper.dto;

import com.codercoach.springbootdeveloper.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddBoardRequest {
    private String title;
    private String content;

    public Board toEntity(String writer) {
        return Board.builder()
                .writer(writer)
                .title(title)
                .content(content)
                .build();
    }
}
