package com.codercoach.springbootdeveloper.dto;

import com.codercoach.springbootdeveloper.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자를 자동으로 생성한다.
@NoArgsConstructor(force = true) // final 필드도 강제로 기본값으로 초기화한다.
public class BoardResponse {

    private final String title;
    private final String content;

    // 모든 필드의 생성자가 있으므로, @AllArgsConstructor이 있더라도, 명시적 선언이 우선된다.
    public BoardResponse(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
    }
}
