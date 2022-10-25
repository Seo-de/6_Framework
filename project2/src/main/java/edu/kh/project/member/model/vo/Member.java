package edu.kh.project.member.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// lombok 라이브러리

@NoArgsConstructor
@Getter // EL 구문 쓸 때 필요
@Setter
@ToString // 보통 toString도 잘 안쓴데
public class Member {

	private int memberNo;
	private String memberEmail;
	private String memberPw;
	private String memberNickname;
	private String memberTel;
	private String memberAddress;
	private String profileImage;
	private String enrollDate;
	private String memberDeleteFlag;
	private int authority;
	
}
