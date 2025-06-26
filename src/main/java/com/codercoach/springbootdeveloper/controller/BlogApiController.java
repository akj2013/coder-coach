package com.codercoach.springbootdeveloper.controller;

import com.codercoach.springbootdeveloper.domain.Article;
import com.codercoach.springbootdeveloper.dto.AddArticleRequest;
import com.codercoach.springbootdeveloper.dto.ArticleResponse;
import com.codercoach.springbootdeveloper.dto.UpdateArticleRequest;
import com.codercoach.springbootdeveloper.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController             // HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
public class BlogApiController {

    private final BlogService blogService;

    /**
     * 블로그 글 하나 생성
     * @param request
     * @return
     */
    // HTTP 메서드가 POST일 때 전달받은 URL과 동일하면 메서드로 매핑
    @PostMapping(value ="/api/articles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    // @RequestBody로 요청 본문 값 매핑
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request, Principal principal) {
        Article savedArticle = blogService.save(request, principal.getName());
        // 요청한 자원이 성공적으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송한다.
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    /**
     * 블로그 글 모두 조회
     * @return
     */
    @GetMapping(value = "/api/articles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        // 스트림 변환과정
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                //.map(article -> new ArticleResponse(article))
                // 아래는 생성자 참조(Constructor Reference)
                .map(ArticleResponse::new) // 각 Article -> ArticleResponse로 형변환
                .toList(); // List<ArticleResponse>로 다시 모음 (자바 16부터 지원)

        return ResponseEntity.ok()
                .body(articles);
    }

    /**
     * GET 요청 : 블로그 글 하나 조회 ID
     * @param id 블로그 ID
     * @return 블로그 글 응답객체
     */
    @GetMapping(value = "/api/article/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {     // PathVariable : URL에서 값을 가져오는 어노테이션
        Article article = blogService.findById(id);
        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    /**
     * DELETE 요청 : 블로그 글 하나 삭제 ID
     * @param id 블로그 ID
     * @return ResponseEntity<Void> HTTP 응답 본문 없이 상태 코드와 헤더만 포함된 응답
     */
    @DeleteMapping(value = "/api/article/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
        blogService.delete(id);
        return ResponseEntity.ok()
                .build();
    }

    /**
     * PUT 요청 : 블르고 글 하나 수정 ID
     * @param id
     * @param request
     * @return 수정된 블로그 글 객체
     */
    @PutMapping(value = "/api/article/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest request) {
        Article updateArticle = blogService.update(id, request);

        return ResponseEntity.ok()
                .body(updateArticle);
    }
}
