package com.codercoach.springbootdeveloper.service;

import com.codercoach.springbootdeveloper.domain.Board;
import com.codercoach.springbootdeveloper.domain.BoardAttach;
import com.codercoach.springbootdeveloper.dto.AddBoardRequest;
import com.codercoach.springbootdeveloper.dto.BoardListViewResponse;
import com.codercoach.springbootdeveloper.dto.BoardViewResponse;
import com.codercoach.springbootdeveloper.dto.UpdateBoardRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {

    Board save(AddBoardRequest request, String userName);

    Board findById(Long bno);

    Board update(Long bno, UpdateBoardRequest request);

    void delete(Long bno);

//	List<BoardVO> getList();

    Page<BoardListViewResponse> findAll(int pageNum, int size);

    List<Board> findAll();

//    int getTotal(Criteria cri);

    // 특정 bno를 파라미터로 첨부파일을 리스트로 가져온다.
    List<BoardAttach> getAttachList(Long bno);

    List<Board> getDataTablesList();

    void getApi();
}
