<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

     <footer>
        <p>Copyright &copy; KH Information Educational Institute A-Class</p>
        <article>
            <a href="#">프로젝트 소개</a>
            <span> | </span>
            <a href="#">이용약관</a>
            <span> | </span>
            <a href="#">개인정보처리방침</a>
            <span> | </span>
            <a href="#">고객센터</a>
        </article>
     </footer>


     <%-- sessionScope에 message 속성이 존재하는 경우
        alert창을 이용해서 내용을 출력
        - header 보다 사용도도 낮고, 어느 페이지에서나 header/footer을 사용,
          alert 을 header에서 사용하면 뒤쪽이 안된다는데?
     --%>

     <c:if test="${!empty sessionScope.message}">
        <script>
            alert("${sessionScope.message}")
        </script>

        <%-- message 1회 출력 후 session scope에서 삭제 --%>
        <c:remove var="message" scope="session"/>
     </c:if>


     