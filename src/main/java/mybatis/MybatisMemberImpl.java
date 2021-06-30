package mybatis;

import org.springframework.stereotype.Service;

/*
@Service어노테이션이 있으면 스프링이 시작될 때 자동으로 빈이
생성된다. 따라서 해당 프로그램에서는 @Autowired하는 부분이 없으므로
어노테이션은 생략 가능하다.
 */
@Service
public interface MybatisMemberImpl {
	
	public MemberVO login(String id, String pass);
 
}
