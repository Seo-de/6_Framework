package edu.kh.project.board.model.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Board {

	// 목록조회, 상세조회에서 쓸거래
    private int boardNo;
    private String boardTitle;
    private String boardContent;
    private String boardCreateDate;
    private String boardUpdateDate;
    private int readCount;
    private int commentCount;
    private int likeCount;
    private String memberNickname;
    private String thumbnail;
    private int memberNo;
    private String profileImage;
	
    // 이미지 목록을 나타내는 리스트
    private List<BoardImage> imageList;
    
    // 댓글 목록을 나타내는 리스트
    private List<Comment> commentList;
    
}
