package com.codercoach.springbootdeveloper.dto;

import com.codercoach.springbootdeveloper.domain.Article;
import com.example.util.MarkdownUtil;
import lombok.Getter;

@Getter
public class ArticleListViewResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String contentHtml;

    public ArticleListViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.contentHtml = MarkdownUtil.toHtml(article.getContent());
    }
}
