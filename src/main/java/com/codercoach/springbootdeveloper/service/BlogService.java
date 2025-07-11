package com.codercoach.springbootdeveloper.service;

import com.codercoach.springbootdeveloper.domain.Article;
import com.codercoach.springbootdeveloper.dto.AddArticleRequest;
import com.codercoach.springbootdeveloper.dto.UpdateArticleRequest;
import com.codercoach.springbootdeveloper.repository.BlogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor        // final이 붙거나 NotNull이 붙은 필드의 생성자를 추가한다.
@Service                        // 해당 클래스를 빈으로 서블릿 컨테이너에 등록해준다.
public class BlogService {

    private final BlogRepository blogRepository;

    // 블로그 글 추가 메서드
    public Article save(AddArticleRequest request, String userName) {
        return blogRepository.save(request.toEntity(userName));
    }

    // 블로그 글 모두 가져오는 메서드
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    // 블로그 글 하나를 조회
    // 없으면 에러 발생
    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found article id : " + id));
    }

    /**
     * ID값으로 블로그 글 하나를 삭제
     */
    public void delete(long id) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        authorizeArticleAuthor(article);
        blogRepository.delete(article);
    }

    @Transactional // 매칭한 메서드를 하나의 트랜잭션으로 묶는 역할을 한다.
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("article not found by id : " + id));
        authorizeArticleAuthor(article);
        article.update(request.getTitle(), request.getContent());

        return article;
    }

    // 게시글을 작성한 유저인지 확인
    private static void authorizeArticleAuthor(Article article) {
//        User customUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println(customUser);

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("작성한 당사자가 아닙니다.");
        }
    }
}
