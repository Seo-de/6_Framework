<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>

<c:set var="boardName" value="${boardTypeList[boardCode-1].BOARD_NAME}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${boardName} 수정</title>

    <link rel="stylesheet" href="/resources/css/main-style.css">

    <link rel="stylesheet" href="/resources/css/board/boardWrite-style.css">


    <script src="https://kit.fontawesome.com/a2e8ca0ae3.js" crossorigin="anonymous"></script>
</head>
<body>
    <main>
        <jsp:include page="/WEB-INF/views/common/header.jsp"/>

        <form action="update" method="POST" enctype="multipart/form-data" class="board-write" id="boardWriteForm" onsubmit="return updateValidate()">  


            <!-- 제목 -->
            <h1 class="board-title">
                <input type="text" name="boardTitle" placeholder="제목" value="${board.boardTitle}">
            </h1>

            <%-- imageList에 존재하는 이미지 순서에 따라 변수 선언 --%>
            <c:forEach items="${board.imageList}" var="img">
                <c:choose>
                    <c:when test="${img.imageOrder == 0}"> <%-- 썸네일 --%>
                        <c:set var="thumbnail" value="${img.imagePath}${img.imageReName}"/>
                    </c:when>

                    <c:when test="${img.imageOrder == 1}"> <%-- 썸네일 --%>
                        <c:set var="img1" value="${img.imagePath}${img.imageReName}"/>
                    </c:when>

                    <c:when test="${img.imageOrder == 2}"> <%-- 썸네일 --%>
                        <c:set var="img2" value="${img.imagePath}${img.imageReName}"/>
                    </c:when>

                    <c:when test="${img.imageOrder == 3}"> <%-- 썸네일 --%>
                        <c:set var="img3" value="${img.imagePath}${img.imageReName}"/>
                    </c:when>

                    <c:when test="${img.imageOrder == 4}"> <%-- 썸네일 --%>
                        <c:set var="img4" value="${img.imagePath}${img.imageReName}"/>
                    </c:when>
                </c:choose>
            </c:forEach>

            <!-- 썸네일 영역 -->
            <h5>썸네일</h5>
            <div class="img-box">
                <div class="boardImg thumbnail">
                    <label for="img0">
                        <img class="preview" src="${thumbnail}">
                    </label>
                    <input type="file" name="images" class="inputImage" id="img0" accept="image/*">
                    <span class="delete-image">&times;</span>
                </div>
            </div>


            <!-- 업로드 이미지 영역 -->
            <h5>업로드 이미지</h5>
            <div class="img-box">

                <div class="boardImg">
                    <label for="img1">
                        <img class="preview" src="${img1}">
                    </label>
                    <input type="file" name="images" class="inputImage" id="img1" accept="image/*">
                    <span class="delete-image">&times;</span>
                </div>

                <div class="boardImg">
                    <label for="img2">
                        <img class="preview" src="${img2}">
                    </label>
                    <input type="file" name="images" class="inputImage" id="img2" accept="image/*">
                    <span class="delete-image">&times;</span>
                </div>

                <div class="boardImg">
                    <label for="img3">
                        <img class="preview" src="${img3}">
                    </label>
                    <input type="file" name="images" class="inputImage" id="img3" accept="image/*">
                    <span class="delete-image">&times;</span>
                </div>

                <div class="boardImg">
                    <label for="img4">
                        <img class="preview" src="${img4}">
                    </label>
                    <input type="file" name="images" class="inputImage" id="img4" accept="image/*">
                    <span class="delete-image">&times;</span>
                </div>
            </div>

            <!-- 이미지 수정
                1. 기존 o -> 삭제한 경우
                    -> 어떤 이미지가 삭제되었는지 확인하고 DB에서 삭제
                2. 기존 o -> 새로운 이미지 -> 수정
                3. 기존 x -> 새로운 이미지 -> 삽입

                input 태그에 기존 경로를 넣어서 대입할 수 없대.
                input 태그에 값을 대입 가능한 건 "" 빈칸 밖에 없대.
            -->

            <!-- 내용 -->
            <!-- 우리가 게시글 쓸 때 엔터 인식을 안해서 개행문자 처리 해준게 그대로 인식되서 태그가 보이는거래
                    그래서 이걸 풀어주는걸 util에서 또 작성함 -->
            <div class="board-content">
                <textarea name="boardContent">${board.boardContent}</textarea>
            </div>


            <!-- 버튼 영역 -->
            <div class="board-btn-area">
                <button type="submit" id="updatebtn">수정</button>
            </div>

            <%-- 삭제될 이미지 순서를 저장한 input태그 --%>
            <input type="hidden" name="deleteList" id="deleteList" value="">

            <%-- 수정 완료 후 리다이렉트 시 사용 예정 - 안해놓으면 사라진데 --%>
            <input type="hidden" name="cp" value="${param.cp}">

        </form>

    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>

    <script src="/resources/js/board/boardUpdate.js"></script>

</body>
</html>