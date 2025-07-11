package com.codercoach.springbootdeveloper.service;

import com.codercoach.springbootdeveloper.domain.Board;
import com.codercoach.springbootdeveloper.domain.BoardAttach;
import com.codercoach.springbootdeveloper.dto.AddBoardRequest;
import com.codercoach.springbootdeveloper.dto.BoardListViewResponse;
import com.codercoach.springbootdeveloper.dto.BoardViewResponse;
import com.codercoach.springbootdeveloper.dto.UpdateBoardRequest;
import com.codercoach.springbootdeveloper.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    // 게시판 글 추가
    @Override
    public Board save(AddBoardRequest request, String writer) {
        return boardRepository.save(request.toEntity(writer));
    }

    // bno값으로 게시판 글 하나를 조회
    // 없으면 에러 발생
    @Override
    public Board findById(Long bno) {
        return boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("not found board id : " + bno));
    }

    @Transactional // 매칭한 메서드를 하나의 트랜잭션으로 묶는 역할을 한다.
    @Override
    public Board update(Long bno, UpdateBoardRequest request) {
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("not found board id : " + bno));
        authorizeBoardWriter(board);
        board.update(request.getTitle(), request.getContent());

        return board;
    }

    /**
     * ID값으로 블로그 글 하나를 삭제
     */
    @Override
    public void delete(Long bno) {
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("not found board id : " + bno));
        authorizeBoardWriter(board);
        boardRepository.delete(board);
    }

    @Override
    public Page<BoardListViewResponse> findAll(int pageNum, int size) {
        Pageable pageable = PageRequest.of(pageNum - 1, size, Sort.by("createdAt").descending());
        Page<Board> boardPage = boardRepository.findAll(pageable);

        // Board → BoardListViewResponse 변환
        return boardPage.map(BoardListViewResponse::new);
    }

    @Override
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

//    @Override
//    public int getTotal(Criteria cri) {
//        return 0;
//    }

    @Override
    public List<BoardAttach> getAttachList(Long bno) {
        return List.of();
    }

    @Override
    public List<Board> getDataTablesList() {
        return List.of();
    }

    @Override
    public void getApi() {

    }

    private static void authorizeBoardWriter(Board board) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!board.getWriter().equals(userName)) {
            throw new IllegalArgumentException("작성한 당사자가 아닙니다.");
        }
    }
}
