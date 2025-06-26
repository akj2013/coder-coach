// OAuth2 로그인 성공 후 리디렉션된 URL에 포함된 액세스 토큰을 localStorage에 저장하는 역할

const token = searchParam('token')

if (token) {
    localStorage.setItem("access_token", token)
}

function searchParam(key) {
    return new URLSearchParams(location.search).get(key);
}