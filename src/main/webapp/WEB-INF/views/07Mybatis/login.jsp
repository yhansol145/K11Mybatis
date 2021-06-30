<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="../resources/css/bootstrap.css" />
<script src="../resources/jquery/jquery-3.6.0.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
	function loginValidate(f) {
		if (f.id.value == "") {
			alert("아이디를 입력하세요");
			f.id.focus();
			return false;
		}
		if (f.pass.value == "") {
			alert("패스워드를 입력하세요");
			f.pass.focus();
			return false;
		}
	}
</script>
</head>
<body>
	<div class="container">
		<h3>방명록(로그인)</h3>
		<c:choose>
			<c:when test="${not empty sessionScope.siteUserInfo }">
				<!-- 로그인이 된 경우에는 회원의 이름과 로그아웃 버튼을 출력 -->
				<div class="row" style="border: 2px solid #cccccc; padding: 10px;">
					<h4>아이디:${sessionScope.siteUserInfo.id }</h4>
					<h4>이름:${sessionScope.siteUserInfo.name }</h4>
					<br />
					<br />
					<button class="btn btn-danger" onclick="location.href='logout.do';">
						로그아웃</button>
					&nbsp;&nbsp;
					<button class="btn btn-primary" onclick="location.href='list.do';">
						방명록리스트</button>
				</div>
			</c:when>
			<c:otherwise>
				<!-- 
				로그아웃 상태에[서는 폼을 출력한다.
				로그인 실패시 출력할 메세지 
				-->
				<span style="font-size: 1.5em; color: red;">${LoginNG }</span>
				<form name="loginForm" method="post" action="./loginAction.do"
					onsubmit="return loginValidate(this);">
					<!-- 로그인에 성공할 경우 이동할 페이지의 경로를 폼값으로 전송 -->
					<input type="hidden" name="backUrl" value="${param.backUrl }" />
					<table class="table-bordered" style="width: 50%;">
						<tr>
							<td><input type="text" class="form-control" name="id"
								placeholder="아이디" tabindex="1"></td>
							<td rowspan="2" style="width: 80px;"><button type="submit"
									class="btn btn-primary" style="height: 77px; width: 77px;"
									tabindex="3">로그인</button></td>
						</tr>
						<tr>
							<td><input type="password" class="form-control" name="pass"
								placeholder="패스워드" tabindex="2"></td>
						</tr>
					</table>
				</form>
			</c:otherwise>
		</c:choose>
	</div>

</body>
</html>