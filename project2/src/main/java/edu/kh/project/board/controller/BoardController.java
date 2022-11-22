package edu.kh.project.board.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.board.model.vo.Board;
import edu.kh.project.member.model.vo.Member;

@Controller
public class BoardController {

	@Autowired
	private BoardService service;
	
	// 특정 게시판 목록 조회
	// 주소 형식이
	// /board/1?cp=1
	// /board/2?cp=10
	// /board/3
	// /board/4 이런 형태래
	
	// -> @PathVariable 사용(변수의 경로)
	//	 URL 경로에 있는 값을 파라미터(변수)로 사용할 수 있게하는 어노테이션
	//	 + 자동으로 request scope로 등록되어 EL 구문으로 jsp에서 출력도 가능
	
	// 요청주소?K=V&K=V&K=V.... (queryString, 쿼리 스트링)
	// -> 요청주소에 값을 담아서 전달할 때 사용하는 문자열
	
	@GetMapping("/board/{boardCode}") // 이걸 여러개 작성이 가능해서 어떤 pathvariable을 꺼낼 지 적어주는 거래
	public String selectBoardList(@PathVariable("boardCode") int boardCode,
			Model model,
			@RequestParam(value="cp", required=false, defaultValue="1") int cp) {
		
		// Model : 값 전달용 객체
		// model.addAttribute("K":V) : request scope에 세팅
		//								-> forward 시 유지됨(session에 안올려도 돼)
		
		Map<String, Object> map = service.selectBoardList(boardCode, cp);
		
		model.addAttribute("map", map); // request scope 세팅
		
		return "board/boardList"; // forward
	}
	
	
	// 게시글 상세 조회
	@GetMapping("/board/{boardCode}/{boardNo}")
	public String boardDetail(
			@PathVariable("boardNo") int boardNo,
			@PathVariable("boardCode") int boardCode,
			Model model,
			HttpServletRequest req, HttpServletResponse resp,
			@SessionAttribute(value = "loginMember", required = false) Member loginMember) throws ParseException {
							// Session에 loginMember가 없으면 null
		
		// 게시글 상세조회 서비스 호출
		Board board = service.selectBoardDetail(boardNo);
		
		// + 좋아요 수, 좋아요 여부
		if(board != null) {
			
			// 좋아요 여부 확인
			// BOARD_LIKE 테이블에
			// 게시글번호, 로그인한 회원 번호가 일치하는 행이 있는지 확인
			
			if(loginMember != null) { // 로그인 상태인 경우
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("boardNo", boardNo);
				map.put("memberNo", loginMember.getMemberNo());
				
				int result = service.boardLikeCheck(map);
				
				if(result>0) { // 좋아요가 되어있는 경우
					model.addAttribute("likeCheck", "like");
				}
			}
			
		}
		
		// + 조회 수 증가(쿠키를 이용해서 해당 IP당 하루 한 번)
		// 게시글 상세 조회 성공 시 조회 수 증가
		if(board != null) {
			
			// 컴퓨터 1대당 게시글마다 1일 1번씩만 조회수 증가
			// -> 쿠키 이용
			
			// Cookie
			// - 사용되는 경로, 수명(만료일자) 
			//	-> 경로 지정 시 
			//	   해당 경로 또는 이하 요청을 보낼 때
			//	   쿠키 파일을 서버로 같이 보냄
			
			// 쿠키에 "readBoardNo"를 얻어와
			// 현재 조회하려는 게시글 번호가 없으면
			// 조회수 1 증가 후 쿠키에 게시글 번호 추가
			
			// 만약에 있으면 
			// 조회수 증가 X
			
			
			// 쿠키 얻오기 - 원하는 하나만 빼오는게 안돼서 쿠키 배열로 얻어와야 한데
			Cookie[] cookies = req.getCookies();
			
			// 쿠키 중 "readBoardNo"가 있는지 확인
			Cookie c = null;
			
			if(cookies != null) { // 쿠키가 있을 때
				for(Cookie temp : cookies) {
					// 얻어온 쿠키의 이름이 readBoardNo인 경우
					if(temp.getName().equals("readBoardNo")) {
						c = temp;
					}
				}
			}
			
			int result = 0; // 조회 수 증가 service 호출 결과 저장
			
			if(c == null) { // "readBoardNo" 쿠키가 없을 경우 == 오늘 상세 조회를 한 번도 안했다.
				
				result = service.updateReadCount(boardNo);
				
				// boardNo 게시글을 상세조회 했을 때 쿠키에 기록
				
				c = new Cookie("readBoardNo", "|" + boardNo + "|"); // 두번째 값에 Stirng이 들어와야된데.
											// |1500|
				
				// 톰캣 8.5 이상 부터 쿠키의 값으로 
				// 세미콜론; , = (공백) 사용 불가
				
			}else { // "readBoardNo" 쿠키가 있을 경우 - 한 번 상세조회를 눌러서 null이 아닌거지
				
				// c.getValue() : "readBoardNo" 쿠키에 저장된 값 (|1990|)
				
				// 쿠키에 저장된 값 중 "|게시글번호|"가 존재하는지 확인
				if(c.getValue().indexOf("|" + boardNo + "|") == -1) {
					// 존재하지 않는 경우
					// == 오늘 처음 조회하는 게시글 번호
					result = service.updateReadCount(boardNo);
					
					// 현재 조회한 게시글 번호를 쿠키에 값으로 추가
					c.setValue(c.getValue()+"|"+boardNo+"|");
					// |1900||2000||20|... 이런식으로 번호가 계속 누적이 될거래. 
					
				}
				
			}

			
			if(result > 0) { // 조회 수 증가 성공 시 DB와 조회된 Board 조회수 동기화

				board.setReadCount(board.getReadCount()+1);
				
				// 쿠키 적용 경로, 구명 설정 후 클라이언트에게 전달
				c.setPath("/"); // localhost/(최상위경로 /) 이하로 적용
				
				// 오늘 23시 59분 59초 까지 남은 시간을 초단위로 구하기
				// Date : 날짜용 객체
				// Calendar : Date가 오래된 기능이라 지원이 안되는게 많아서 만들어진 Date 업그레이드 객체
				// SimpleDateFormat : 날짜를 원하는 형태의 문자열로 변환 / toChar 같은 거래.
				
				Date a = new Date(); // 현재 시간
				// 기준시간 : 1970년 1월 1일 09시 0분 0초
				// new Date(ms) : 기준 시간 + ms 만큼 지난 시간

				Calendar cal = Calendar.getInstance();
				cal.add(cal.DATE, 1); // 날짜에 1 추가
				//	단위(년/월/일) , 추가할 값
				// 여기서 년월일만 남기고 다 지우면 년월일 0:0:0이래
				
				// SimpleDateFormat을 이용해서 cal 날짜 중 시,분,초를 0:0:0 바꿈
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date temp = new Date( cal.getTimeInMillis() );
				// 하루 증가한 내일 날짜의 ms 값을 이용해서 Date 객체 생성
				
//				System.out.println(sdf.format(temp));
				Date b = sdf.parse(sdf.format(temp));
					// 날짜 형식 String -> Date로 변환

				// 날짜 끼리는 빼기가 안된데
				// 내일 자정 ms - 현재 시간 ms
				
				long diff = b.getTime() - a.getTime();
//				System.out.println(diff/1000); // 23:59:59까지 남은 시간(s)
				
				c.setMaxAge( (int)(diff/1000) ); // 10분(임시) 600초(초단위)
				resp.addCookie(c); // 쿠키를 클라이언트에게 전달
			}
		}
		
		model.addAttribute("board",board);
		
		
		return "board/boardDetail";
	}
	
	
	// 좋아요 수 증가(INSERT)
	@GetMapping("/boardLikeUp")
	@ResponseBody
	public int boardLikeUp(@RequestParam Map<String, Object> paramMap) {
		// vo로 받아올 수 있는데 그럼 너무 커서 map으로 받아올거래.
		// @RequestParam Map<String, Object>
		//	-> 요청 시 전달된 파라미터를 하나의 Map으로 반환
		return service.boardLikeUp(paramMap);
	}
	
   // 좋아요 수 감소(DELETE)
   @GetMapping("/boardLikeDown")
   @ResponseBody
   public int boardLikeDown(@RequestParam Map<String, Object> paramMap) {
      return service.boardLikeDown(paramMap);
   }
	
	
   // 트랜잭셔널을 사용하는 경우는 
   // dao를 여러개를 사용할 때 쓴데.
   // 하나만 DML 할때는 성능저하가 될 수도 있으니까 생략한데.
   
   
   
   // 게시글 삭제
   @GetMapping("board/{boardCode}/{boardNo}/delete")
   public String boardDelete(@RequestHeader("referer") String referer,
		   @PathVariable("boardCode") int boardCode, @PathVariable("boardNo")int boardNo,
		   RedirectAttributes ra) {
	   
	   // 게시글 번호를 이용해서 게시글을 삭제(BOARD_DEL_FL = 'Y' 수정)
	   int result = service.boardDelete(boardNo);
	   
	   String message = null;
	   String path = null;
	   
	   if(result>0) {
		   // 성공 시 : "삭제되었습니다" 메세지 전달
		   // 해당 게시판 목록 1페이지로 리다이렉트
		   
		   message = "삭제되었습니다.";
		   path = "/board/"+boardCode;
	   }else {
		   // 실패 시 : "게시글 삭제 실패" 메세지 전달
		   // 요청 이전 주소(referer)로 리다이렉트
		   
		   message = "게시글 삭제 실패";
		   path = referer;
	   }
	   
	  ra.addFlashAttribute("message", message);
	   
	   return "redirect:"+path;
   }
   
   
   
   // 게시글 작성 페이지 이동
   @GetMapping("/write/{boardCode}")
   public String boardWrite(@PathVariable("boardCode")int boardCode) {
	   
	   return "board/boardWrite";
   }
	   
	   
	
   // 게시글 작성
   @PostMapping("/write/{boardCode}") // multipart는 무조건 post만 가능
   public String boardWrite(
		   Board board,
		   @RequestParam(value="images", required=false) List<MultipartFile> imageList,
		   @SessionAttribute("loginMember") Member loginMember,
		   @PathVariable("boardCode") int boardCode,
		   RedirectAttributes ra, 
		   HttpSession session,// 어느 파일에 저장할지의 경로
		   @RequestHeader("referer") String referer) throws IOException {
	   
	   // 1. boardCode를 board객체에 세팅(Board VO에 boardCode 필드 추가)
	   board.setBoardCode(boardCode);
	   
	   // 2. 로그인한 회원의 번호를 board 객체에 세팅
	   board.setMemberNo(loginMember.getMemberNo());
	   
	   // 3. 업로드 된 파일의 웹 접근경로 / 서버 내부 경로를 변수에 저장
	   String webPath = "/resources/images/board/";
	   String folderPath = session.getServletContext().getRealPath(webPath); // 어플리케이션 스코프래
	   // -> /resources/images/board/ 까지의 실제 컴퓨터 저장 경로 반환
	   
	   // 4. 게시글 삽입 서비스 호출 
	   // -> 내가 쓴 게시글 화면이 보여야 되는거니까 boardNo를 받아올거래.
	   int boardNo = service.boardWrite(board, imageList, webPath, folderPath); 
	   
	   String message = null;
	   String path = null;
	   
	   if(boardNo > 0) {
		   message = "게시글이 등록되었습니다.";
		   path  = "/board/"+boardCode+"/"+boardNo;
		   		//  /board/1/2004 (상세조회 요청 주소)
	   }else {
		   message = "게시글 작성 실패";
		   path = referer;
	   }
	   
	   ra.addFlashAttribute("message", message);
	   
	   return "redirect:"+path;
   }
	   
	   
	   
}




