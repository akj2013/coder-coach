
# Thymeleaf와 JSTL의 차이 및 Thymeleaf의 상대경로 해석 이유 (2025-07-04 08:20)

## ✅ 1. Thymeleaf vs JSTL (JSP)
둘 다 **Java 웹 어플리케이션에서 HTML을 동적으로 생성**하기 위한 기술입니다.

| 항목 | JSTL/JSP | Thymeleaf |
|------|----------|-----------|
| 방식 | 서버 사이드에서 HTML 생성 | HTML 기반 템플릿에 동적 데이터 주입 |
| 문법 | `<c:forEach>`, `${}` | `th:each`, `th:text`, `${}` |
| 특징 | 서버에서 처리 후 HTML로 변환되어 클라이언트로 전송됨 | HTML 파일을 직접 브라우저에서 열어도 깨지지 않음 (자연스러운 템플릿) |
| 기술스택 | JSTL, EL, Servlet/JSP 기반 | Spring MVC와 자연스럽게 통합됨 |

---

## ✅ 2. Thymeleaf의 "상대경로 해석"이란 무엇인가?
Spring MVC와 함께 사용할 때, Thymeleaf는 컨트롤러에서 전달된 모델을 받아 HTML 템플릿에서 표현합니다.

예시:
```java
@GetMapping("/hello")
public String hello(Model model) {
    model.addAttribute("name", "광준");
    return "greeting";  // greeting.html 렌더링
}
```

그리고 `greeting.html` 내에서:
```html
<a th:href="@{/user/profile}">프로필</a>
```

Thymeleaf는 `@{}` 문법을 통해 **URL 경로를 Spring MVC 상대경로로 해석**합니다.

즉, **Thymeleaf는 템플릿 안에서**:
- `@{...}`: URL을 생성
- `${...}`: 모델 속성 참조
- `th:each`, `th:if`: 반복과 조건문 등 제어

이런 기능을 제공함으로써 **동적인 HTML 생성을 돕습니다.**

---

## ✅ 3. 왜 상대경로를 해석할까?

Thymeleaf는 단순한 정적 HTML 템플릿 엔진이 아니라, **웹 어플리케이션의 라우팅과 데이터 바인딩까지 책임지는 뷰 레이어**입니다.

상대경로를 해석하는 이유는:

- 컨트롤러의 URL 매핑 구조와 **동기화된 링크 제공** (변경에 강함)
- `context path` 포함한 경로 자동 처리 (`@{/user}` → `/app/user`)
- 국제화(i18n), fragment 조립 등 다양한 Spring 기능과의 연동

Thymeleaf는 서버에서 .html을 렌더링하는 템플릿 엔진입니다.
즉, 브라우저에서 직접 Ajax로 header.html을 요청하면 정상적으로 응답되지 않습니다.
왜냐하면 /templates/*.html은 정적 자원이 아니기 때문입니다.

- Thymeleaf 템플릿은 반드시 컨트롤러에서 매핑되어야 합니다.
- Thymeleaf 공식 방식은 JS에서 로드하지 않고, 서버 측에서 조립하는 것입니다.

---

## ✅ 4. 정리

| 질문 | 설명 |
|------|------|
| 🔹 Thymeleaf는 뭐냐? | HTML 기반의 템플릿 엔진으로, 동적인 HTML 페이지 생성 용도 |
| 🔹 JSTL과 비교하면? | JSP에 비해 더 직관적이고 HTML 친화적 |
| 🔹 왜 상대경로를 해석하냐? | 뷰(View)에서 사용자가 접근해야 할 URL을 동적으로 생성하기 위해 |
| 🔹 어디서 쓰냐? | Spring MVC의 Controller와 연계되어 동적 웹 페이지 렌더링할 때 |

---

궁금한 포인트가 더 있거나 JSTL/Thymeleaf 비교를 실습 코드로 보고 싶다면 말씀해주세요!
