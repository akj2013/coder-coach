# Git 명령어 요약

---

## 1. 기본 설정
```bash
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```
### 1-1. 로컬 저장소 사용자 정보 확인
```bash
git config user.name
git config user.email
```
### 1-2. 로컬 저장소 사용자 정보 설정 ( 글로벌 X )
```bash
git config user.name "Other User"
git config user.email "otheruser@example.com"
```

***

## 2. 📁 로컬 저장소 초기화
```bash
git init
```

***

## 3. 📄 파일 상태 확인
```bash
git status
```
```bash
PS C:\Users\akjak\IdeaProjects\CoderCoach2\src\main\resources\static\md> git status
On branch dev
Changes to be committed:
  (use "git restore --staged <file>..." to unstage)
        renamed:    ../../../../../.github/workflows/ci.yml -> ../../../../../.github/workflows/ci_cd-main.yml

Untracked files:
  (use "git add <file>..." to include in what will be committed)
        ./
```

***

## 4. ➕ 스테이징 & 커밋
```bash
git add .
git commit -m "커밋 메시지"
```

***

## 5. 🌐 원격 저장소 연결
```bash
git remote add origin https://github.com/username/repo.git
```

***

## 6. 🚀 푸시 & 풀
```bash
git push -u origin main
git pull origin main
```

***

## 7. 🌱 브랜치 관리
```bash
git remote update # 지역 저장소에 원격 저장소의 상태를 갱신
```

### 7-1. checkout 명령어
```bash
git checkout -b new-branch  # 새 브랜치 생성 후 이동
git checkout -t new-branch  # 원격 저장소에서 생성한 브랜치를 지역 저장소에서 사용할 브런치로 지정
git checkout main           # 기존 브랜치로 이동
```
### 7-2. branch 명령어
```bash
git branch                 # 브랜치 목록 확인
git branch -l              # 지역 저장소의 브랜치 정보를 보여준다.(생략가능)
git branch -a              # 지역 저장소와 원격 저장소의 브랜치 정보를 함께 보여준다
git branch <브랜치명>        # 새로운 브랜치를 생성한다.
git branch -d <브랜치명>     # 브랜치 삭제
git branch -r              # 원격 저장소의 브랜치 정보를 보여준다.
git branch -v              # 지역 저장소의 브랜치 정보를 최신 커밋 내역과 함께 보여준다.
```
### 7-3. merge 명령어
```bash
git merge new-branch        # 브랜치 병합
```

***

## 8. 🧹 기타 명령어
```bash
git log           # 커밋 로그 확인
git diff          # 변경사항 확인
git stash         # 작업 저장
git tag v1.0      # 태그 생성
```
### 8-1. log 명령어
```bash
git log --pretty=oneline --graph
```
```bash
* 265b194b7f2396016f1e429a42b2ecceac4c76e1 (HEAD -> dev, origin/dev) PR의 대상 브런치를 dev가 아닌 main 브런치로 수정
* 5a765b45522aba932ca36303360e2d5613f7ba40 Article 생성 때 날짜값 들어가도록 수정, dev 브런치PR용 ci-dev.yml파일 추가
* 473f702b66c3ef7e0baf7f5c6257be01c3c72073 (main) application.yml에 jpa와 server port 관련 코멘트 추가
* a6748b5a39abdeb2835b9e9c114ca87b5410bfcc (origin/main) CICD TEST 07
* b976352a19a46e267c371764eb0136dafeb348d2 CICD TEST 06
* 125fe4a7a6f2557af496dd5495b1a497a60fe3c0 CICD TEST 05
```

***

## 9. 📝 커밋 메시지 예시
```bash
feat: 로그인 기능 추가
fix: 사용자 등록 버그 수정
docs: README 파일 수정
refactor: 중복 코드 제거
```
