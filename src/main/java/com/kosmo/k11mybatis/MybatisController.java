package com.kosmo.k11mybatis;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import mybatis.MemberVO;
import mybatis.MyBoardDTO;
import mybatis.MybatisDAOImpl;
import mybatis.MybatisMemberImpl;
import mybatis.ParameterDTO;
import util.PagingUtil;

@Controller
public class MybatisController {

	/*
	 * Mybatis를 사용하기 위해 빈을 자동주입 받음. servlet-context.xml에서 생성함.
	 */
	@Autowired
	private SqlSession sqlSession;
//   public void setSqlSession(SqlSession sqlSession) {
//      this.sqlSession = sqlSession;
//      System.out.println("Mybatis setter 호출");
//   }

	// 방명록리스트1
	/*
		#####
		검색처리 없는 버전으로 작성함
	 */
//   @RequestMapping("/mybatis/list.do")
//   public String list(Model model, HttpServletRequest req) {
//      
//	   //방명록 테이블의 게시물 갯수 카운트
//	   /*
//	   컨트롤러에서 서비스 역할을 하는 인터페이스에 정의된
//	   추상메소드를 호출한다. 그러면 mapper에 정의된 쿼리문이
//	   실행되는 형식으로 동작한다.
//	    */
//      int totalRecordCount = sqlSession.getMapper(MybatisDAOImpl.class)
//            .getTotalCount();
//      
//      //페이지 처리를 위한 설정값
//      int pageSize = 4;
//      int blockPage = 2;
//      //전체페이지 수 계산
//      int totalPage = (int)Math.ceil((double)totalRecordCount/pageSize);
//      //현재페이지 번호
//      int nowPage = req.getParameter("nowPage")==null ? 
//            1:Integer.parseInt(req.getParameter("nowPage"));
//      //select할 게시물의 구간을 계산
//      int start = (nowPage-1)*pageSize +1;
//      int end = nowPage * pageSize;
//      
//      //목록에 출력할 게시물을 얻어오기 위한 쿼리실행
//      ArrayList<MyBoardDTO> lists = sqlSession.getMapper(MybatisDAOImpl.class)
//            .listPage(start, end);
//      
//      
//
//      //페이지번호 출력 및 게시물 갯수 저장
//      String pagingImg = PagingUtil.pagingImg(
//            totalRecordCount, 
//            pageSize, 
//            blockPage, 
//            nowPage, 
//            req.getContextPath()+"/mybatis/list.do?");
//      model.addAttribute("pagingImg",pagingImg);
//      
//      //내용에 대한 줄바꿈 처리
//      for(MyBoardDTO dto : lists) {
//         String temp =
//               dto.getContents().replace("\r\n", "<br/>");
//         dto.setContents(temp);
//      }
//      
//      model.addAttribute("lists",lists);
//      return "07Mybatis/list";
//   }

	// 방명록리스트2
	/*
		#####
		검색어 처리 추가 부분으로 작성됨
	 */
	@RequestMapping("/mybatis/list.do")
	public String list(Model model, HttpServletRequest req) {

		// 방명록 테이블의 게시물 갯수 카운트
		/*
		 * 컨트롤러에서 서비스 역할을 하는 인터페이스에 정의된 추상메소드를 호출한다. 그러면 mapper에 정의된 쿼리문이 실행되는 형식으로
		 * 동작한다.
		 */
		
		//각종 파라미터를 저장하기 위한 DTO객체 생성
		ParameterDTO parameterDTO = new ParameterDTO();
		//검색 파라미터 저장
		parameterDTO.setSearchField(req.getParameter("searchField"));
		//parameterDTO.setSearchTxt(req.getParameter("searchTxt")); //2차버전
		
		//3차버전
		//검색어를 스페이스로 구분하는 경우
		ArrayList<String> searchLists = null;
		if(req.getParameter("searchTxt")!=null) {
			/*
			스페이스로 구분된 검색어를 받은 후 split하여 List컬렉션에
			추가한다. 검색어의 갯수만큼 추가된다.
			 */
			searchLists = new ArrayList<String>();
			String[] sTxtArray = req.getParameter("searchTxt").split(" ");
			for(String str : sTxtArray) { 
				searchLists.add(str);
			}
		}
		parameterDTO.setSearchTxt(searchLists);
		System.out.println("검색어:"+ searchLists);
		
		System.out.println("검색어:"+parameterDTO.getSearchTxt());
		
		//검색 파라미터를 기반으로 게시물 수 카운트 하기
		int totalRecordCount = sqlSession.getMapper(MybatisDAOImpl.class).getTotalCount(parameterDTO);
		System.out.println("totalRecordCount="+ totalRecordCount);

		// 페이지 처리를 위한 설정값
		int pageSize = 4;
		int blockPage = 2;
		// 전체페이지 수 계산
		int totalPage = (int) Math.ceil((double) totalRecordCount / pageSize);
		// 현재페이지 번호
		int nowPage = req.getParameter("nowPage") == null ? 1 : Integer.parseInt(req.getParameter("nowPage"));
		// select할 게시물의 구간을 계산
		int start = (nowPage - 1) * pageSize + 1;
		int end = nowPage * pageSize;
		
		//기존과는 다르게 시작, 끝을 DTO에 저장한다.
		parameterDTO.setStart(start);
		parameterDTO.setEnd(end);
		
		//DTO를 기반으로 Mapper 호출
		ArrayList<MyBoardDTO> lists =
				sqlSession.getMapper(MybatisDAOImpl.class).listPage(parameterDTO);
		
		//Mybatis 기본 쿼리문 출력하기(동적쿼리문 확인용)
		String sql = sqlSession.getConfiguration().getMappedStatement("listPage")
				.getBoundSql(parameterDTO).getSql();
		System.out.println("sql="+ sql);

//		// 목록에 출력할 게시물을 얻어오기 위한 쿼리실행
//		ArrayList<MyBoardDTO> lists = sqlSession.getMapper(MybatisDAOImpl.class).listPage(start, end);

		// 페이지번호 출력 및 게시물 갯수 저장
		String pagingImg = PagingUtil.pagingImg(totalRecordCount, pageSize, blockPage, nowPage,
				req.getContextPath() + "/mybatis/list.do?");
		model.addAttribute("pagingImg", pagingImg);

		// 내용에 대한 줄바꿈 처리
		for (MyBoardDTO dto : lists) {
			String temp = dto.getContents().replace("\r\n", "<br/>");
			dto.setContents(temp);
		}

		model.addAttribute("lists", lists);
		return "07Mybatis/list";
	}

	// 글쓰기 페이지 매핑
	@RequestMapping("/mybatis/write.do")
	public String write(Model model, HttpSession session, HttpServletRequest req) {
		/*
		 * 매핑된 메소드 내에서 session 내장객체를 사용하기 위해 매개변수로 선언해준다.
		 */
		// 세션영역에 회원인증 관련 속성이 있는지 확인
		if (session.getAttribute("siteUserInfo") == null) {
			/*
			 * 글쓰기 페이지 진입 시 세션이 없이 로그인 페이지로 이동하면 로그인 후에는 다시 글쓰기 페이지로 돌아가기 위해 backUrl을 지정해
			 * 준다.
			 */
			model.addAttribute("backUrl", "07Mybatis/write");
			return "redirect:login.do";
		}

		// 로그인 된 상태라면 쓰기 페이지로 즉시 진입한다.
		return "07Mybatis/write";
	}

	// 로그인 페이지 매핑
	@RequestMapping("/mybatis/login.do")
	public String login(Model model) {
		return "07Mybatis/login";
	}

	// 로그인 처리
	@RequestMapping("/mybatis/loginAction.do")
	public ModelAndView loginAction(HttpServletRequest req, HttpSession session) {
		// 파라미터로 전달된 id, pass를 받아 login() 메소드 호출
		MemberVO vo = sqlSession.getMapper(MybatisMemberImpl.class).login(req.getParameter("id"),
				req.getParameter("pass"));

		ModelAndView mv = new ModelAndView();
		if (vo == null) {
			// 로그인에 실패한 경우 실패메세지를 model에 저장한 후
			mv.addObject("LoginNG", "아이디/패스워드가 틀렸습니다.");
			// 다시 로그인 페이지를 호출한다.
			mv.setViewName("07Mybatis/login");
			return mv;
		} else {
			// 로그인에 성공한 경우 세션영역에 속성을 저장한다.
			session.setAttribute("siteUserInfo", vo);
		}

		// 로그인 후 돌아갈 페이지가 없는 경우에는 로그인 페이지로 이동한다.
		String backUrl = req.getParameter("backUrl");
		if (backUrl == null || backUrl.equals("")) {
			mv.setViewName("07Mybatis/login");
		} else {
			mv.setViewName(backUrl);
		}
		return mv;
	}

	// 글쓰기 처리
	@RequestMapping(value = "/mybatis/writeAction.do", method = RequestMethod.POST)
	public String writeAction(Model model, HttpServletRequest req, HttpSession session) {
		// 글쓰기 처리 시 로그인 체크 후 진행해야 함
		if (session.getAttribute("siteUserInfo") == null) {
			return "redirect:login.do";
		}

		// 글쓰기 처리를 위한 메소드 호출
		int result = sqlSession.getMapper(MybatisDAOImpl.class).write(req.getParameter("name"),
				req.getParameter("contents"), ((MemberVO) session.getAttribute("siteUserInfo")).getId());// 세션 영역에 저장된
																											// VO객체로부터
																											// 아이디를 얻어와
																											// 파라미터로 사용

		// 쓰기처리 완료 후 리스트로 이동
		return "redirect:list.do";

	}

	// 수정 페이지
	@RequestMapping("/mybatis/modify.do")
	public String modify(Model model, HttpServletRequest req, HttpSession session) {
		// 수정 페이지 진입 전 로그인 확인
		if (session.getAttribute("siteUserInfo") == null) {
			return "redirect:login.do";
		}

		// Mapper쪽으로 전달할 파라미터를 저장할 용도의 DTO객체 생성
		ParameterDTO parameterDTO = new ParameterDTO();
		parameterDTO.setBoard_idx(req.getParameter("idx")); // 일련번호
		parameterDTO.setUser_id(((MemberVO) session.getAttribute("siteUserInfo")).getId()); // 사용자아이디

		// Mapper호출 시 DTO객체를 파라미터로 전달
		MyBoardDTO dto = sqlSession.getMapper(MybatisDAOImpl.class).view(parameterDTO);

		model.addAttribute("dto", dto);
		return "07Mybatis/modify";
	}

	// 수정처리
	@RequestMapping("/mybatis/modifyAction.do")
	public String modifyAction(HttpSession session, MyBoardDTO myBoardDTO) {

		// 수정 페이지에서 전송된 폼값은 커맨드 객체를 통해 한꺼번에 받음

		// 수정 처리 전 로그인 체크
		if (session.getAttribute("siteUserInfo") == null) {

			return "redirect:login.do";
		}
		// 수정처리를 위해 modify 메소드 호출
		sqlSession.getMapper(MybatisDAOImpl.class).modify(myBoardDTO);

		return "redirect:list.do";
	}

	// 삭제처리
	@RequestMapping("/mybatis/delete.do")
	public String delete(HttpServletRequest req, HttpSession session) {
		// 본인확인을 위해서 로그인 체크
		if (session.getAttribute("siteUserInfo") == null) {

			return "redirect:login.do";
		}

		// 삭제처리 위해 delete() 메소드 호출. 삭제처리의 경우에도 삭제된
		// 레코드 갯수가 정수형으로 반환된다. 필요없는경우 생략할 수 있다.
		sqlSession.getMapper(MybatisDAOImpl.class).delete(req.getParameter("idx"),
				((MemberVO) session.getAttribute("siteUserInfo")).getId());

		return "redirect:list.do";
	}

}