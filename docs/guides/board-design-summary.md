# 게시판 시스템 설계 문서

> 작성일: 2025-12-30
> 태스크 ID: task-1767099338593-975788

## 개요

게시글 등록, 상세 조회, 댓글, 대댓글 기능을 포함한 게시판 시스템 설계를 완료했습니다.

## 설계 산출물

### 1. API 설계 (`board/docs/design/api-design.md`)

REST API 엔드포인트 설계:

| 구분 | Method | Endpoint | 설명 |
|------|--------|----------|------|
| 게시글 | POST | `/api/posts` | 게시글 등록 |
| 게시글 | GET | `/api/posts` | 목록 조회 (페이징) |
| 게시글 | GET | `/api/posts/{id}` | 상세 조회 |
| 게시글 | PUT | `/api/posts/{id}` | 수정 |
| 게시글 | DELETE | `/api/posts/{id}` | 삭제 |
| 댓글 | POST | `/api/posts/{postId}/comments` | 댓글 등록 |
| 댓글 | GET | `/api/posts/{postId}/comments` | 댓글 목록 |
| 댓글 | PUT | `/api/comments/{id}` | 수정 |
| 댓글 | DELETE | `/api/comments/{id}` | 삭제 |
| 대댓글 | POST | `/api/comments/{commentId}/replies` | 대댓글 등록 |

### 2. 데이터베이스 스키마 (`board/docs/design/database-schema.md`)

#### 테이블 구조

```
┌─────────────────────┐
│       posts         │
├─────────────────────┤
│ id (PK)             │
│ title               │
│ content             │
│ author              │
│ created_at          │
│ updated_at          │
└─────────┬───────────┘
          │ 1:N
┌─────────▼───────────┐
│      comments       │
├─────────────────────┤
│ id (PK)             │
│ post_id (FK)        │
│ parent_id (FK, self)│ ← 대댓글 (self-reference)
│ content             │
│ author              │
│ created_at          │
│ updated_at          │
└─────────────────────┘
```

**설계 결정사항:**
- 댓글/대댓글은 Self-referencing 방식 채택
- `parent_id`가 NULL이면 댓글, 값이 있으면 대댓글
- 대댓글은 1단계만 허용 (depth 제한)
- CASCADE 삭제 정책 적용

### 3. 도메인 모델 (`board/docs/design/domain-model.md`)

#### 패키지 구조

```
io.github.tato126.board
├── domain
│   ├── post      # Post 엔티티, Repository, Service
│   └── comment   # Comment 엔티티, Repository, Service
├── api
│   ├── post      # Controller, Request/Response DTO
│   └── comment   # Controller, Request/Response DTO
├── common
│   ├── exception # GlobalExceptionHandler, NotFoundException
│   └── response  # ErrorResponse
└── BoardApplication.java
```

**설계 원칙:**
- 계층 분리: domain, api, common 패키지로 분리
- DTO 분리: 요청/응답 DTO를 엔티티와 분리
- 지연 로딩: 연관관계는 LAZY 로딩
- Cascade: 부모 엔티티 삭제 시 자식도 함께 삭제

## 기술 스택

- **Framework**: Spring Boot
- **ORM**: JPA/Hibernate
- **Database**: MySQL (DDL 제공)

## 다음 단계

1. 도메인 엔티티 구현
2. Repository 인터페이스 작성
3. Service 계층 구현
4. Controller 및 DTO 구현
5. 예외 처리 구현
6. 테스트 코드 작성

---

*이 문서는 자동화 태스크로 생성되었습니다.*
