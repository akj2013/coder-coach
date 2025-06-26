package com.codercoach.springbootdeveloper.dto;

import com.codercoach.springbootdeveloper.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 서비스 계층에서 요청을 받을 객체
 *
 * 컨트롤러에서 요청한 본문을 받을 객체이다.
 * DTO : data transfer object
 * 계층끼리 데이터를 교환하기 위해 사용하는 객체이다.
 * DTO는 단순하게 데이터를 옮기기 위해 사용하는 전달자 역할을 하는 객체이기 때문에
 * 별도의 비즈니스 로직을 포함하지 않는다.
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {
    private String title;
    private String content;

    public Article toEntity(String author) {
        return Article.builder()
                .author(author)
                .title(title)
                .content(content)
                .build();
    }
}
