
// 비밀번호 변경 유효성 검사

// 비밀번호 변경 form 요소
const changePwForm = document.getElementById("changePwForm");

if(changePwForm != null){ // changePwForm 요소가 존재할 때
    changePwForm.addEventListener("submit", function(event){

        // ** 이벤트 핸들러 매개변수 event || e **
        // -> 현재 발생한 이벤트 정보를 가지고 있는 event 객체가 전달됨.

        console.log(event);
    
        const currentPw = document.getElementById("currentPw");
        const newPw = document.getElementById("newPw");
        const newPwConfirm = document.getElementById("newPwConfirm");

        // 현재 비밀번호가 작성되지 않았을 때
        if(currentPw.value.trim().length == 0){
            // alert("현재 비밀번호를 입력해주세요.");
            // currentPw.focus();
            // currentPw.value = "";

            alertAndFocus(currentPw, "현재 비밀번호를 입력해주세요.")

            // return false; 
            // --> 인라인 이벤트 모델 onsubmit = "return 함수명();" 에서만 가능

            event.preventDefault();
            // -> form 태그가 가지고 있는 이벤트를 수행하지 못하게 하는 함수
            // --> 기본 이벤트 삭제
            // ---> a태그가 가지고 있는 이벤트도 막을 수 있음.
            
            return; // 붙여주면 유효성 검사하고 밑에껀 확인 안해봐도 된다 약간 그런거래. 그럼 된거래
        }
        
        
        // 새 비밀번호가 작성되지 않았을 때
        if(newPw.value.trim().length == 0){
            // alert("새 비밀번호를 입력해주세요.");
            // newPw.focus();
            // newPw.value="";
            alertAndFocus(newPw, "새 비밀번호를 입력해주세요.")
            event.preventDefault();
            return;
        }
        
        
        // 새 비밀번호 확인이 작성되지 않았을 때
        if(newPwConfirm.value.trim().length == 0){
            // alert("새 비밀번호 확인을 작성해주세요.");
            // newPwConfirm.focus();
            // newPwConfirm.value = "";
            alertAndFocus(newPwConfirm, "새 비밀번호 확인을 입력해주세요.")
            event.preventDefault();
            return;
        }


        // 비밀번호 정규식 검사(특수문자 넣어서 확인하는 그런건가봐)




        // 새 비밀번호, 새 비밀번호 확인이 같은지 검사
        if(newPw.value != newPwConfirm.value){
            alert("새 비밀번호가 일치하지 않습니다.");
            newPwConfirm.focus();
            event.preventDefault(); // 기본 이벤트 제거
            return; // 함수 종료
        }
    })

}

// 경고창 출력 + 포커스 이동 + 값 삭제
function alertAndFocus(input, str){
    alert(str);
    input.focus();
    input.value="";
    // 이벤트랑 return은 작성하지 않았는데 이벤트는 이동시키는걸 권장하지 않고,
    // return도 호출 함수가 종료되는거라서 안된다는데...?
}



// 회원 탈퇴 유효성 검사
// - 인라인 이벤트 모델 또는 표준 이벤트 모델 사용

//표준 이벤트 모델
const memberDeleteForm = document.getElementById("memberDeleteForm");

if(memberDeleteForm != null){ // 탈퇴 폼이 있을 경우

    // 제출 되었을 때의 동작
    memberDeleteForm.addEventListener("submit", function(event){
        // 1) 비밀번호 미작성 -> "비밀번호를 입력해주세요" alert 출력 후 
        //                       포커스 이동(내용도 같이 삭제)
        const memberPw = document.getElementById("memberPw");
        if(memberPw.value.trim().length == 0){ // 비밀번호 미작성
            alert("비밀번호를 입력해주세요");
            memberPw.value="";
            memberPw.focus();
            event.preventDefault();
            return; // 함수를 이 순간 종료
        }
    
        // 2) 동의 체크가 되지 않은 경우 -> "탈퇴를 동의하시면 체크를 눌러주세요."
        //                                   포커스 이동
        // 단순히 탈퇴를 누르면 체크박스가 체크되었는지 확인만 하는거라서 eventlistner가 필요가 없데
        const agree = document.getElementById("agree");
        if(!agree.checked){
            alert("탈퇴를 동의하시면 체크를 눌러주세요.");
            agree.focus();
            event.preventDefault();
            return;
        }

        // 정말 탈퇴할 것인지 검사
        if(!confirm("정말 탈퇴하시겠습니까?")){ // 취소를 누른 경우
            alert("탈퇴 취소");
            event.preventDefault();
            memberPw.value="";
            return;
        }
    });
}


// 인라인 이벤트 모델로 탈퇴처리
// function memberDeleteValidate(){
//     const memberPw = document.getElementById("memberPw");

//     if(memberPw.value.trim().length == 0){
//         alert("비밀번호를 입력해주세요");
//         memberPw.focus();
//         memberPw.value = "";
//         return false;
//     }

//     const agree = document.getElementById("agree");
//     if(!agree.checked){
//         alert("탈퇴를 동의하시면 체크를 눌러주세요.");
//         agree.focus();
//         return false;
//     }

//     // 탈퇴 여부 확인
//     if(!confirm("정말로 탈퇴하시겠습니까?")){
//         alert("탈퇴취소");
//         return false;
//     }
//     return true;
// }



// --------------------------------------------------
// 프로필 수정

const profileImage = document.getElementById("profile-image");
const deleteImage = document.getElementById("delete-image");
const imageInput = document.getElementById("image-input");

// 초기 프로필 이미지 상태를 저장하는 변수
// (true : 업로드 된 이미지 있음, false : 기본이미지)
let initCheck;

// 이미지가 업로드 되었거나 삭제되었음을 나타내는 변수
// (-1 : 초기값(취소), 0 : 프로필 삭제(x버튼 클릭), 1 : 새 이미지 업로드)
let deleteCheck=-1;

// 프로필 수정 페이지에 처음 들어왔을 때의 이미지 경로
const originalImage = profileImage.getAttribute("src");

// 프로필 수정 화면일 경우
if(imageInput != null){

    // 해당 화면 진입 시 프로필 이미지 상태를 저장(initCheck)
    if(profileImage.getAttribute("src") == "/resources/images/user.png"){
        // 기본 이미지인 경우
        initCheck = false;
    }else{
        initCheck = true;
    }
    
    // 이미지가 선택되었을 때 미리보기

    // * input type="file" 요소는 값이 없을 때 ''(빈칸)
    //      이미지를 넣으면 fakepath로 뜨는데
    // * input type="file" 요소는 이전에 선택한 파일이 있어도 취소를 누르면 다시 ''(빈칸)
    // * input type="file" 요소로 파일을 선택하면 change 이벤트가 발생

    imageInput.addEventListener("change", e=>{

        // e.target : 이벤트가 발생한 요소(==imageInput)
        // * 화살표 함수에서 this는 window 객체를 의미힌다!!

        // 선택된 파일의 목록 (여러개를 선택하면 여러개가 나올거래)
        console.log(e.target.files); 
        console.log(e.target.files[0]);

        // 선택된 파일이 있을 경우
        if(e.target.files[0] != undefined){

            const reader = new FileReader();
            // FileReader (파일 읽는 객체)
            // - 웹 애플리케이션이
            //   비동기적으로 데이터를 읽기 위하여 
            //   읽을 파일을 가르키는 File 객체
            // - 읽어들인 파일을 사용자 컴퓨터에 저장할 수 있다.
    
            reader.readAsDataURL(e.target.files[0]);
            // FileReader.readAsDataURL("파일정보")
            // -> 지정된 파일을 읽기 시작함.
    
            // FileReader.onload : 파일 읽기가 완료되었을 때의 동작을 지정
            reader.onload = event =>{
                // console.log(event.target);
                // event.target.result : 읽어진 파일 결과(실제 이미지 파일)의 경로
    
                // img 태그의 src 속성으로 읽은 이미지 파일 경로 추가
                // == 이미지 미리보기
                profileImage.setAttribute("src", event.target.result);

                deleteCheck = 1;
            }
        }else{ // 취소가 눌러진 경우

            // 초기 이미지로 다시 변경
            profileImage.setAttribute("src", originalImage);
            deleteCheck = -1;
        }
    });


    // x버튼이 클릭됐을 경우 -> 기본이미지로 변경
    deleteImage.addEventListener("click", ()=>{
        profileImage.setAttribute("src", "/resources/images/user.png");
        imageInput.value="";
        deleteCheck=0;
    });

}


function profileValidate(){

    // 이미지가 없음 -> 있음
    if(!initCheck && deleteCheck==1){
        return true;
    }

    // 이미지가 있음 -> 없음(x버튼)
    if(initCheck && deleteCheck==0){
        return true;
    }

    // 이미지가 있음 -> 있음(새로운 이미지 업로드)
    if(initCheck && deleteCheck==1){
        return true;
    }

    alert("이미지 변경 후 클릭하세요")
    return false;
}