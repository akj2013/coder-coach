package com.codercoach.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "T_BOARD")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bno", updatable = false)
    private Long bno;

    @Column(name = "title", nullable = false) // 'title' 이라는 not null 컬럼과 매핑
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "text") // columnDefinition 명시적 선언
    private String content;

    @Column(name = "writer", nullable = false)
    private String writer;

    @CreatedDate // 엔티티가 생성될 때 생성 시간 지정
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate // 엔티티가 수정될 때 수정 시간 지정
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

//    private int replyCnt;

    // 게시글을 등록할 때 첨부파일도 함께 등록할 수 있도록 인스턴스를 추가한다.
//    private List<BoardAttach> attachList;

    @Builder
    public Board(String title, String content, String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    /**
     * 요청받은 내용으로 엔티티 값을 수정
     * @param title
     * @param content
     */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
