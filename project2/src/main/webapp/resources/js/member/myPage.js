
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
