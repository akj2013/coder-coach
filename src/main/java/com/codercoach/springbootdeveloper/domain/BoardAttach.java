package com.codercoach.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "T_BOARD_ATTACH")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardAttach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bno", updatable = false)
    private Long bno;

    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "uploadPath", nullable = false)
    private String uploadPath;

    @Column(name = "fileName", nullable = false)
    private String fileName;

    @Column(name = "fileType", nullable = false)
    private boolean fileType;

    @CreatedDate // 엔티티가 생성될 때 생성 시간 지정
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate // 엔티티가 수정될 때 수정 시간 지정
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public BoardAttach(Long bno, String uuid, String uploadPath, String fileName, boolean fileType) {
        this.bno = bno;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public void update(String uuid, String uploadPath, String fileName, boolean fileType) {
        this.uuid = uuid;
        this.uploadPath = uploadPath;
        this.fileName = fileName;
        this.fileType = fileType;
    }
}
