package com.codercoach.springbootdeveloper.controller;

import com.codercoach.springbootdeveloper.domain.Article;
import com.codercoach.springbootdeveloper.domain.User;
import com.codercoach.springbootdeveloper.dto.AddArticleRequest;
import com.codercoach.springbootdeveloper.dto.ArticleResponse;
import com.codercoach.springbootdeveloper.dto.UpdateArticleRequest;
import com.codercoach.springbootdeveloper.repository.BlogRepository;
import com.codercoach.springbootdeveloper.repository.UserRepository;
import com.codercoach.springbootdeveloper.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 테스트용 애플리케이션 컨텍스트
@AutoConfigureMockMvc   // MockMVC 생성 및 자동 구성
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper; // 직렬화, 역직렬화를 위한 클래스

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
    }

    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                .email("user@gamil.com")
                .password("test")
                .build());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
    }

    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        // given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        // 객체 JSON으로 직렬화
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        // Mockito는 자바에서 단위 테스트를 위해 객체를 "가짜(mock)"로 만들어주는 라이브러리입니다.
        // 특히 의존성 주입이 필요한 객체나 외부 시스템과의 의존을 제거하고 테스트에 집중할 수 있도록 돕습니다.
        Principal principal = Mockito.mock(Principal.class); // 실제 인증된 사용자가 없어도 테스트할 수 있도록 함.
        Mockito.when(principal.getName()).thenReturn("username"); // 로그인한 사용자 이름을 "username"이라고 가정함.

        // when
        // 설정한 내용을 바탕으로 요청 전송
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("findAllArticles: 블로그 글 목록 조회에 성공한다.")
    @Test
    public void findAllArticles() throws Exception {
        // GIVEN 블로그 글을 저장합니다.
        final String url = "/api/articles";
        Article savedArticle = createDefaultArticle();

        // WHEN 목록 조회 API를 호출합니다.
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // THEN 응답 코드가 200 OK이고, 반환받은 값 중에 0번째 요소의 값을 확인합니다.
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(savedArticle.getContent()))
                .andExpect(jsonPath("$[0].title").value(savedArticle.getTitle()));

        // AssertJ 형식
        final MvcResult mvcResult = resultActions.andReturn();
        final String responseBody = mvcResult.getResponse().getContentAsString();

        List<ArticleResponse> articleResponses = objectMapper.readValue(
                responseBody,
                new TypeReference<List<ArticleResponse>>() {}
        );

        assertThat(articleResponses).hasSize(1);
        assertThat(articleResponses.get(0).getTitle()).isEqualTo(savedArticle.getTitle());
        assertThat(articleResponses.get(0).getContent()).isEqualTo(savedArticle.getContent());
    }

    /**
     * ID값으로 블로그 글 1개 조회 테스트
     */
    @DisplayName("findArticle: 블로그 글 조회에 성공한다.")
    @Test
    public void findArticle() throws Exception {
        // GIVEN : 블로그 글을 저장합니다.
        final String url = "/api/article/{id}";
        final String title = "title";
        final String content = "content";
        final String author = "author";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .build());

        // WHEN : 저장한 블로그 글의 id값으로 API를 호출합니다.
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

        // THEN : 응답 코드가 200 OK이고, 반환받은 content와 title이 저장된 값과 같은지 확인합니다.
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
    }

    @DisplayName("BlogApiController.delete() : 블로그 글 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception {
        // GIVEN : 블로그 글을 저장합니다.
        final String url = "/api/article/{id}";
        final String title = "title";
        final String content = "content";
        final String author = "author";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .author(user.getUsername())
                .build());

        // WHEN : 저장한 블로그 글의 id값으로 삭제API를 호출합니다.
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());

        // THEN : 응답 코드가 200 OK이고, 블로그 글 리스트를 전체 조회해 조회한 배열 크기가 0인지 확인합니다.
        List<Article> articles = blogRepository.findAll();

        assertThat(articles).isEmpty();
    }

    @DisplayName("BlogApiController.updateArticle() : 블로그 글 수정에 성공한다.")
    @Test
    public void updateArticle() throws Exception {
        // GIVEN : 블로그 글을 저장하고, 글 수정에 필요한 요청 객체를 만든다.
        final String url = "/api/article/{id}";
        final String title = "new title";
        final String content = "new content";

        Article article = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .author(user.getUsername())
                .build());

        UpdateArticleRequest request = new UpdateArticleRequest(title, content);

        // WHEN : UPDATE API로 수정 요청을 보낸다. 요청 타입은 JSON이며, GIVEN절에서 미리 만든 객체를 요청 본문에 함께 보낸다.
        ResultActions result = mockMvc.perform(put(url, article.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        // THEN : 응답 코드가 200 OK인지 확인한다. 블로그 글을 ID로 조회한 후에 값이 수정되었는지 확인한다.
        result.andExpect(status().isOk());

        Article updatedArticle = blogRepository.findById(article.getId()).get();
        assertThat(updatedArticle.getTitle()).isEqualTo(title);
        assertThat(updatedArticle.getContent()).isEqualTo(content);
    }

    private Article createDefaultArticle() {
        return blogRepository.save(Article.builder()
                .title("title")
                .author(user.getUsername())
                .content("content")
                .build());
    }
}