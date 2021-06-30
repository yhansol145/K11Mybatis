package mybatis;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

/*
해당 인터페이스는 컨트롤러와 DAO사이에서 매개역할을 하는
서비스 객체로 사용된다.
 */
@Service
public interface MybatisDAOImpl {
	
	//1차 버전에서 사용
	//# 검색기능 추가 전 #
//	//게시물 수 카운트 하기
//	public int getTotalCount();
//	
//	//목록에 출력할 게시물 가져오기
//	public ArrayList<MyBoardDTO> listPage(int s, int e);
	
	//2차 버전(게시물 검색 추가)
	//# 검색기능 추가 후 # : 파라미터를 저장한 DTO를 매개변수로 받음
	public int getTotalCount(ParameterDTO parameterDTO);
	public ArrayList<MyBoardDTO> listPage(ParameterDTO parameterDTO);
	
	/*
	Mapper에서 파라미터를 처리할 수 있는 세번째 방법으로, @Param
	어노테이션을 사용한다. 이때는 변수명을 그대로 Mapper에서 사용할 수 있다.
	 */
	public int write(@Param("_name") String name,
			@Param("_contents") String contents,
			@Param("_id") String id);
	
	//기존 게시물 조회
	public MyBoardDTO view(ParameterDTO parameterDTO);
	//수정처리
	public int modify(MyBoardDTO myBoardDTO);
	//삭제처리
	public int delete(String idx, String id);

}
