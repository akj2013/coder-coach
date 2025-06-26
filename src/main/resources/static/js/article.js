/**
article.html
newArticle.html
화면에서 사용한다.

각 버튼의 클릭 이벤트를 구현한다.
*/

/*
✅ 템플릿 리터럴 (Template Literals)
    템플릿 리터럴은 문자열을 동적으로 생성하거나 여러 줄로 작성할 수 있게 해주는 JavaScript 문법입니다.
    백틱(`) 기호로 문자열을 감싸고, ${변수}를 이용해 값을 삽입할 수 있습니다.
    // 템플릿 리터럴 사용
    let message2 = `이름: ${name}, 나이: ${age}`;

✅ 1. const vs let의 차이
📌 공통점
    둘 다 **ES6(ES2015)**에서 도입된 변수 선언 방식
    **블록 스코프(block scope)**를 가짐 ({} 중괄호 내에서만 유효)
    var보다 안전하고 예측 가능한 코드 작성 가능

📌 차이점
    🔹 const (constant)
        재할당 불가능한 변수 선언
        반드시 선언과 동시에 초기화해야 함
        주의: 객체나 배열은 내부 값을 변경할 수 있음
        const name = "홍길동";
        name = "이순신"; // ❌ 에러! const는 재할당 불가
        const user = { name: "홍길동" };
        user.name = "이순신"; // ✅ 가능

    🔹 let
        재할당 가능한 변수
        블록 내에서만 유효
        let count = 1;
        count = 2; // ✅ 가능

✅ 2. if (modifyButton) 조건문은 어떻게 참/거짓을 판단하나?
📌 document.getElementById('modify-btn')의 결과는?
    해당 ID를 가진 DOM 요소가 존재하면 → HTMLElement 객체 반환
    없으면 → null 반환
📌 JavaScript의 if (modifyButton)의 판별 로직
    JavaScript는 다음 값을 Falsy (거짓)로 간주합니다:
        null
        undefined
        0
        NaN
        "" (빈 문자열)
        false
    그 외의 값은 Truthy (참)로 간주합니다.
    따라서 if (modifyButton)은
    👉 버튼이 존재하면 (HTMLElement 객체) → 참
    👉 버튼이 없으면 (null) → 거짓

✅ 왜 이렇게 조건을 거는가?
    JS는 DOM을 찾을 때 해당 요소가 페이지에 없으면 null을 반환하므로,
    addEventListener()를 붙이기 전에 존재 여부를 확인해서 에러 방지하는 것.
*/

// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', (event) => {
        let id = document.getElementById('article-id').value;
// httpRequest() 함수를 사용하기 위해 코멘트 처리
//        fetch(`/api/article/${id}`, {                          // API 삭제 요청 호출(백틱으로 URL 형성)
//            method: 'DELETE'
//        })
//        .then(() => {                                           // 완료 시 연이어 실행
//            alert('삭제가 완료되었습니다.');
//            location.replace('/articles');                      // 현재 주소를 기반으로 화면 전이
//        });
        function success() {
            alert("삭제가 완료되었습니다.");
            location.replace('/articles');
        }
        function fail() {
            alert("삭제가 실패했습니다.");
            location.replace('/articles');
        }

        httpRequest("DELETE", "/api/articles/" + id, null, success, fail);
    });
}


// 수정 기능
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    // 클릭 이벤트가 감지되면 수정 API 요청
    modifyButton.addEventListener('click', (event) => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        body = JSON.stringify({
            title: document.getElementById("title").value,
            content: document.getElementById("content").value,
        });

        function success() {
            alert("수정이 성공했습니다.");
            location.replace("/articles/" + id);
        }

        function fail() {
            alert("수정이 실패했습니다.");
            location.replace("/articles/" + id);
        }
// httpRequest() 함수를 사용하기 위해 코멘트 처리
//        fetch(`/api/article/${id}`, {
//            method: 'PUT',
//            headers: {
//                "Content-Type": "application/json",
//            },
//            body: JSON.stringify({
//                title: document.getElementById('title').value,
//                content: document.getElementById('content').value
//            })
//        })
//        .then(() => {
//            alert('수정이 완료되었습니다.');
//            location.replace(`/articles/${id}`);
//        });
        httpRequest(`PUT`, `/api/article/${id}`, body, success, fail);
    });
}

// 생성 기능
const createButton = document.getElementById("create-btn");

if (createButton) {
    createButton.addEventListener("click", (event) => {
        body = JSON.stringify({
            title: document.getElementById("title").value,
            content: document.getElementById("content").value,
        });
        function success() {
            alert("등록 완료되었습니다.");
            location.replace("/articles");
        }
        function fail() {
            alert("등록 실패하였습니다..");
            location.replace("/articles");
        }

        httpRequest("POST", "/api/articles", body, success, fail);
    });
}

// 쿠키를 가져오는 함수
function getCookie(key) {
    var result = null;
    var cookie = document.cookie.split(";");
    cookie.some(function (item) {
        item = item.replace(" ", "");

        var dic = item.split("=");

        if (key === dic[0]) {
            result = dic[1];
            return true;
        }
    });

    return result;
}

// HTTP 요청을 보내는 함수
// POST 요청을 보낼 때 액세스 토큰도 함께 보냅니다. 만약 응답에 권한이 없다는 에러코드가 발생하면 리프레시 토큰과 함께 새로운
// 액세스 토큰을 요청하고, 전달받은 액세스 토큰으로 다시 API를 요청합니다.
function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: {
            // 로컬 스토리지에서 엑세스 토큰 값을 가져와 헤더에 추가
            Authorization: "Bearer " + localStorage.getItem("access_token"),
            "Content-Type": "application/json",
        },
        body: body,
    }).then((response) => {
        if (response.status == 200 || response.status === 201) {
            return success();
        }
        const refresh_token = getCookie("refresh_token");
        if (response.status === 401 && refresh_token) {
            fetch("/api/token", {
                method: "POST",
                headers: {
                    Authorization: "Bearer " + localStorage.getItem("access_token"),
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    refreshToken: getCookie("refresh_token"),
                }),
            })
                .then((res) => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then((result) => {
                    // 재발급이 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem("access_token", result.accessToken);
                    httpRequest(method, url, body, success, fail);
                })
                .catch((error) => fail());
        } else {
            return fail();
        }
    });
}

// 로그아웃 버튼 클릭 이벤트
const logoutButton = document.getElementById("logout-btn");

if (logoutButton) {
    logoutButton.addEventListener("click", (event) => {
        function success() {
            // 로컬 스토리지에 저장된 액세스 토큰 삭제
            localStorage.removeItem("access_token");
            // 쿠키에 저장된 리프레시 토큰 삭제
            deleteCookie("refresh_token");
            // 로그인 페이지로 이동
            alert("로그아웃하였습니다");
            location.replace("/login");
        }
        function fail() {
            alert("로그아웃에 실패하였습니다.");
        }

        httpRequest('DELETE', '/api/refresh-token', null, success, fail);
    });
}

// 쿠키를 삭제하는 함수
function deleteCookie(key) {
    document.cookie = key + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}