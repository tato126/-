# 게시판 테스트 결과 보고서

> 작성일: 2025-12-31
> 테스트 환경: Spring Boot 4.0.1, Java 21

## 테스트 개요

게시판 시스템(Post, Comment)에 대한 단위 테스트 및 통합 테스트를 수행했습니다.

### 테스트 구성

| 테스트 유형 | 테스트 클래스 | 어노테이션 |
|------------|--------------|-----------|
| Repository | PostRepositoryTest, CommentRepositoryTest | `@DataJpaTest` |
| Service | PostServiceTest, CommentServiceTest | `@ExtendWith(MockitoExtension.class)` |
| Controller | PostControllerTest, CommentControllerTest | `@WebMvcTest` |

---

## 테스트 결과 요약

**전체 테스트: 54개 통과**

### Repository 테스트 (12개)

#### PostRepositoryTest (6개)
- 게시글 저장 테스트
- 게시글 ID로 조회 테스트
- 존재하지 않는 게시글 조회 시 빈 Optional 반환
- 게시글 페이징 조회 테스트
- 게시글 수정 테스트
- 게시글 삭제 테스트

#### CommentRepositoryTest (6개)
- 댓글 저장 테스트
- 댓글 ID로 조회 테스트
- 게시글의 최상위 댓글만 조회 테스트
- 대댓글 저장 테스트
- 댓글 수정 테스트
- 댓글 삭제 테스트
- 게시글 삭제 시 댓글도 함께 삭제 (Cascade) 테스트

---

### Service 테스트 (16개)

#### PostServiceTest (8개)
- 게시글 생성 테스트
- 게시글 목록 페이징 조회 테스트
- 게시글 단건 조회 테스트
- 존재하지 않는 게시글 조회 시 예외 발생
- 게시글 수정 테스트
- 존재하지 않는 게시글 수정 시 예외 발생
- 게시글 삭제 테스트
- 존재하지 않는 게시글 삭제 시 예외 발생

#### CommentServiceTest (8개)
- 댓글 생성 테스트
- 존재하지 않는 게시글에 댓글 생성 시 예외 발생
- 대댓글 생성 테스트
- 존재하지 않는 부모 댓글에 대댓글 생성 시 예외 발생
- 게시글의 최상위 댓글 목록 조회 테스트
- 댓글 수정 테스트
- 존재하지 않는 댓글 수정 시 예외 발생
- 댓글 삭제 테스트
- 존재하지 않는 댓글 삭제 시 예외 발생

---

### Controller 테스트 (23개)

#### PostControllerTest (11개)
- 게시글 생성 API 테스트 (POST /api/posts)
- 게시글 생성 API - 유효성 검증 실패 테스트 (빈 제목)
- 게시글 생성 API - 유효성 검증 실패 테스트 (빈 내용)
- 게시글 생성 API - 유효성 검증 실패 테스트 (빈 작성자)
- 게시글 목록 조회 API 테스트 (GET /api/posts)
- 게시글 단건 조회 API 테스트 (GET /api/posts/{id})
- 존재하지 않는 게시글 조회 시 404 응답
- 게시글 수정 API 테스트 (PUT /api/posts/{id})
- 존재하지 않는 게시글 수정 시 404 응답
- 게시글 삭제 API 테스트 (DELETE /api/posts/{id})
- 존재하지 않는 게시글 삭제 시 404 응답

#### CommentControllerTest (12개)
- 댓글 생성 API 테스트 (POST /api/posts/{postId}/comments)
- 존재하지 않는 게시글에 댓글 생성 시 404 응답
- 댓글 생성 API - 유효성 검증 실패 테스트 (빈 내용)
- 댓글 생성 API - 유효성 검증 실패 테스트 (빈 작성자)
- 댓글 목록 조회 API 테스트 (GET /api/posts/{postId}/comments)
- 대댓글 생성 API 테스트 (POST /api/comments/{id}/replies)
- 존재하지 않는 부모 댓글에 대댓글 생성 시 404 응답
- 댓글 수정 API 테스트 (PUT /api/comments/{id})
- 존재하지 않는 댓글 수정 시 404 응답
- 댓글 수정 API - 유효성 검증 실패 테스트 (빈 내용)
- 댓글 삭제 API 테스트 (DELETE /api/comments/{id})
- 존재하지 않는 댓글 삭제 시 404 응답

---

## 수정 사항

### 의존성 수정 (build.gradle)

Spring Boot 4.0의 모듈화된 테스트 구조에 맞게 의존성을 수정했습니다:

```diff
- testImplementation 'org.springframework.boot:spring-boot-test-classic-modules'
+ testImplementation 'org.springframework.boot:spring-boot-webmvc-test'
```

### Controller 테스트 import 수정

Spring Boot 4.0에서 변경된 패키지 구조 적용:

```java
// Before
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

// After
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
```

---

## 테스트 파일 위치

```
board/src/test/java/io/github/tato126/board/
├── api/
│   ├── post/
│   │   └── PostControllerTest.java
│   └── comment/
│       └── CommentControllerTest.java
└── domain/
    ├── post/
    │   ├── PostRepositoryTest.java
    │   └── PostServiceTest.java
    └── comment/
        ├── CommentRepositoryTest.java
        └── CommentServiceTest.java
```

---

## 테스트 실행 방법

```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests "PostServiceTest"

# 테스트 리포트 확인
open build/reports/tests/test/index.html
```

---

*이 문서는 자동화 태스크로 생성되었습니다.*
