# Task 1: 도메인 엔티티 구현

> 작성일: 2025-12-31
> 기반 문서: docs/features/board-implementation-tasks.md

## 목표

Post, Comment 엔티티 클래스 및 JPA 매핑 구현

## 세부 작업

### 1.1 Post 엔티티
- 파일 위치: `board/src/main/java/io/github/tato126/board/domain/post/Post.java`
- 필드: id, title, content, author, createdAt, updatedAt
- 연관관계: `@OneToMany` → Comment (cascade = ALL, orphanRemoval = true)

### 1.2 Comment 엔티티
- 파일 위치: `board/src/main/java/io/github/tato126/board/domain/comment/Comment.java`
- 필드: id, content, author, createdAt, updatedAt
- 연관관계:
  - `@ManyToOne` → Post (LAZY)
  - `@ManyToOne` → Comment (parent, self-reference, LAZY)
  - `@OneToMany` → Comment (replies, cascade = ALL)

### 1.3 BaseEntity (선택)
- 파일 위치: `board/src/main/java/io/github/tato126/board/domain/common/BaseEntity.java`
- 공통 필드: createdAt, updatedAt
- `@MappedSuperclass`, `@EntityListeners(AuditingEntityListener.class)`

## 완료 조건
- 엔티티 컴파일 성공
- JPA 어노테이션 적용 완료

## 기술 참고
- Lombok 사용: `@Getter`, `@NoArgsConstructor(access = AccessLevel.PROTECTED)`
- JPA Auditing 활성화 필요: `@EnableJpaAuditing`

---

*이 문서는 자동화 태스크로 생성되었습니다.*
