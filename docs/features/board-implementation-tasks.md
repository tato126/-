# 게시판 시스템 구현 태스크

> 작성일: 2025-12-31
> 기반 문서: docs/guides/board-design-summary.md

## 개요

게시판 시스템 설계 문서를 기반으로 실제 구현을 위한 세부 태스크를 정의합니다.

## 구현 상태

- [x] 프로젝트 초기 설정 (Spring Boot)
- [ ] 도메인 엔티티 구현
- [ ] Repository 인터페이스 작성
- [ ] Service 계층 구현
- [ ] Controller 및 DTO 구현
- [ ] 예외 처리 구현
- [ ] 테스트 코드 작성

---

## Task 1: 도메인 엔티티 구현

### 목표
Post, Comment 엔티티 클래스 및 JPA 매핑 구현

### 세부 작업

#### 1.1 Post 엔티티
- 파일 위치: `board/src/main/java/io/github/tato126/board/domain/post/Post.java`
- 필드: id, title, content, author, createdAt, updatedAt
- 연관관계: `@OneToMany` → Comment (cascade = ALL, orphanRemoval = true)

#### 1.2 Comment 엔티티
- 파일 위치: `board/src/main/java/io/github/tato126/board/domain/comment/Comment.java`
- 필드: id, content, author, createdAt, updatedAt
- 연관관계:
  - `@ManyToOne` → Post (LAZY)
  - `@ManyToOne` → Comment (parent, self-reference, LAZY)
  - `@OneToMany` → Comment (replies, cascade = ALL)

#### 1.3 BaseEntity (선택)
- 파일 위치: `board/src/main/java/io/github/tato126/board/domain/common/BaseEntity.java`
- 공통 필드: createdAt, updatedAt
- `@MappedSuperclass`, `@EntityListeners(AuditingEntityListener.class)`

### 완료 조건
- 엔티티 컴파일 성공
- JPA 어노테이션 적용 완료

---

## Task 2: Repository 인터페이스 작성

### 목표
JPA Repository 인터페이스 정의

### 세부 작업

#### 2.1 PostRepository
- 파일 위치: `board/src/main/java/io/github/tato126/board/domain/post/PostRepository.java`
- `JpaRepository<Post, Long>` 상속
- 페이징 지원 (기본 제공)

#### 2.2 CommentRepository
- 파일 위치: `board/src/main/java/io/github/tato126/board/domain/comment/CommentRepository.java`
- `JpaRepository<Comment, Long>` 상속
- 커스텀 메서드:
  - `List<Comment> findByPostIdAndParentIsNull(Long postId)` - 게시글의 최상위 댓글만 조회

### 완료 조건
- Repository 인터페이스 정의 완료
- 컴파일 성공

---

## Task 3: Service 계층 구현

### 목표
비즈니스 로직을 담당하는 Service 클래스 구현

### 세부 작업

#### 3.1 PostService
- 파일 위치: `board/src/main/java/io/github/tato126/board/domain/post/PostService.java`
- 메서드:
  - `Post createPost(String title, String content, String author)`
  - `Page<Post> getPosts(Pageable pageable)`
  - `Post getPost(Long id)`
  - `Post updatePost(Long id, String title, String content)`
  - `void deletePost(Long id)`

#### 3.2 CommentService
- 파일 위치: `board/src/main/java/io/github/tato126/board/domain/comment/CommentService.java`
- 메서드:
  - `Comment createComment(Long postId, String content, String author)`
  - `Comment createReply(Long commentId, String content, String author)`
  - `List<Comment> getComments(Long postId)`
  - `Comment updateComment(Long id, String content)`
  - `void deleteComment(Long id)`

### 완료 조건
- Service 클래스 구현 완료
- 트랜잭션 처리 (`@Transactional`)
- 예외 처리 기본 구현

---

## Task 4: Controller 및 DTO 구현

### 목표
REST API 엔드포인트 및 요청/응답 DTO 구현

### 세부 작업

#### 4.1 Post DTO
- 파일 위치: `board/src/main/java/io/github/tato126/board/api/post/dto/`
- 클래스:
  - `CreatePostRequest` (title, content, author)
  - `UpdatePostRequest` (title, content)
  - `PostResponse` (id, title, content, author, createdAt, updatedAt)
  - `PostListResponse` (id, title, author, createdAt)

#### 4.2 Comment DTO
- 파일 위치: `board/src/main/java/io/github/tato126/board/api/comment/dto/`
- 클래스:
  - `CreateCommentRequest` (content, author)
  - `UpdateCommentRequest` (content)
  - `CommentResponse` (id, content, author, createdAt, replies)

#### 4.3 PostController
- 파일 위치: `board/src/main/java/io/github/tato126/board/api/post/PostController.java`
- 엔드포인트: `/api/posts`

#### 4.4 CommentController
- 파일 위치: `board/src/main/java/io/github/tato126/board/api/comment/CommentController.java`
- 엔드포인트: `/api/posts/{postId}/comments`, `/api/comments/{id}`

### 완료 조건
- 모든 API 엔드포인트 구현
- 요청/응답 DTO 매핑 완료
- API 문서와 일치 여부 확인

---

## Task 5: 예외 처리 구현

### 목표
전역 예외 처리 및 커스텀 예외 정의

### 세부 작업

#### 5.1 커스텀 예외
- 파일 위치: `board/src/main/java/io/github/tato126/board/common/exception/`
- 클래스:
  - `NotFoundException` - 리소스를 찾을 수 없을 때
  - `InvalidRequestException` - 잘못된 요청일 때

#### 5.2 GlobalExceptionHandler
- 파일 위치: `board/src/main/java/io/github/tato126/board/common/exception/GlobalExceptionHandler.java`
- `@RestControllerAdvice`
- 예외별 HTTP 상태 코드 매핑

#### 5.3 ErrorResponse
- 파일 위치: `board/src/main/java/io/github/tato126/board/common/response/ErrorResponse.java`
- 필드: code, message, timestamp

### 완료 조건
- 예외 발생 시 일관된 응답 형식 반환
- HTTP 상태 코드 적절히 매핑

---

## Task 6: 테스트 코드 작성

### 목표
단위 테스트 및 통합 테스트 작성

### 세부 작업

#### 6.1 Repository 테스트
- `PostRepositoryTest`
- `CommentRepositoryTest`
- `@DataJpaTest` 활용

#### 6.2 Service 테스트
- `PostServiceTest`
- `CommentServiceTest`
- Mockito를 활용한 단위 테스트

#### 6.3 Controller 테스트
- `PostControllerTest`
- `CommentControllerTest`
- `@WebMvcTest` 또는 `@SpringBootTest`

### 완료 조건
- 주요 기능에 대한 테스트 커버리지 확보
- 모든 테스트 통과

---

## 우선순위 및 의존성

```
Task 1 (엔티티)
    ↓
Task 2 (Repository)
    ↓
Task 3 (Service)
    ↓
Task 4 (Controller/DTO)
    ↓
Task 5 (예외 처리) ← 병렬 진행 가능
    ↓
Task 6 (테스트)
```

## 참고 사항

- Task 5(예외 처리)는 Task 3, 4와 병렬로 진행 가능
- 각 Task 완료 후 컴파일 및 기본 동작 확인 필수
- 테스트 코드는 각 Task 진행 중에도 작성 가능

---

*이 문서는 자동화 태스크로 생성되었습니다.*
