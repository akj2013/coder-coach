package com.codercoach.springbootdeveloper.controller;

import com.codercoach.springbootdeveloper.domain.Article;
import com.codercoach.springbootdeveloper.dto.ArticleListViewResponse;
import com.codercoach.springbootdeveloper.dto.ArticleViewResponse;
import com.codercoach.springbootdeveloper.service.BlogService;
import com.example.util.MarkdownUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final BlogService blogService;

    // 블로그 글 목록 조회
    @GetMapping(value = "/articles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getArticles(HttpServletRequest request, Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles", articles); // 블로그 글 리스트 저장
        model.addAttribute("currentUri", request.getRequestURI()); // 컨트롤러에서 현재 URI를 model로 넘기기

        return "articleList"; // articleList.html 뷰 조회
    }

    // 블로그 글 조회
    @GetMapping(value = "/articles/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = blogService.findById(id);

        // Markdown → HTML 변환
        String html = MarkdownUtil.toHtml(article.getContent());

        model.addAttribute("article", new ArticleViewResponse(article));
        model.addAttribute("contentHtml", html);

        return "article";
    }

    // 블로그 글 수정 및 신규
    @GetMapping(value = "/new-article", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("article", new ArticleViewResponse());
        } else {
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }
        return "newArticle";
    }
}
