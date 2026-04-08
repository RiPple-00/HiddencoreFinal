# 📋 게시판 (Board) 프론트엔드 구조 문서

> - Nav 바는 외부 컴포넌트 사용 예정으로 제외
> - 탭 전환 / 검색 / 페이지네이션 모두 프론트엔드에서 처리
> - 백엔드 추가 호출 없음 (마운트 시 1회 전체 로드)
> - 환경: Java(Spring Boot) + JavaScript + React + Tailwind + Express + Axios + Swagger

---

## 📁 디렉토리 구조

```
src/
├── assets/
│   └── icons/
│       ├── filter.svg
│       ├── search.svg
│       └── attachment.svg
│
├── constants/
│   └── boardConstants.js             # 상수 모음
│
├── contexts/
│   └── BoardContext.jsx              # 게시판 상태/로직 중앙 관리
│
├── utils/
│   ├── boardUtils.js
│   └── dateUtils.js
│
├── api/
│   └── postApi.js                    # Axios 기반 API 호출
│
├── components/
│   ├── common/
│   │   ├── StatusBadge.jsx           # 공통 배지 (게시판 외 전체 사용)
│   │   ├── FilterTab.jsx             # 공통 탭 (탭 목록 props 주입)
│   │   ├── SearchBar.jsx             # 공통 검색바
│   │   └── Pagination.jsx            # 공통 페이지네이션
│   │
│   └── board/
│       └── PostList.jsx              # 게시글 목록 (table / widget 모드 공용)
│
└── pages/
    └── board/
        ├── BoardListPage.jsx
        ├── BoardDetailPage.jsx
        ├── BoardCreatePage.jsx
        └── BoardEditPage.jsx
```

---

## 📐 상수 (boardConstants.js)

```js
export const PAGE_SIZE = 20;          // 페이지당 항목 수 (변경 가능)
export const WIDGET_SIZE = 2;         // 메인 페이지 위젯 노출 개수 (변경 가능)
// export const HOT_THRESHOLD = 500;  // HOT 배지 기준 조회수 (추후 추가)

export const BOARD_TABS = [
  { label: '전체 게시판', type: null },
  { label: '자유 게시판', type: 'FREE' },
  { label: '공지 게시판', type: 'NOTICE' },
  { label: '프로그램 게시판', type: 'PROGRAM' },
];

// 게시판 종류에 따라 탭 목록이 달라지는 경우 별도 상수 추가
```

---

## 🏷️ StatusBadge (공통 컴포넌트)

게시판 외 전체에서 공용으로 사용해요.

| type 값 | 라벨 | 사용 예시 |
|---------|------|-----------|
| `URGENT` | 긴급 | 게시판, 공지 위젯 |
| `NOTICE` | 공지 | 게시판 |
| `CLINICAL` | 임상 | 게시판 |
| `ADMIN` | 행정 | 게시판 |
| `GENERAL` | 일반 | 게시판 |
| `HOT` | HOT | 주석 처리 (추후 추가) |
| 추후 추가 가능 | - | 환자 상태, 일정 등 |

```jsx
<StatusBadge type="URGENT" />
```

---

## 🗂️ FilterTab (공통 컴포넌트)

탭 목록을 외부에서 주입받아 어디서든 재사용해요.

```jsx
<FilterTab tabs={BOARD_TABS} onChange={changeTab} />
```

탭 종류는 게시판마다 `BOARD_TABS` 상수만 교체해서 구성해요.

---

## 🧩 PostList (board 전용)

```
[ table 모드 - 게시판 목록 페이지 ]
NO | TITLE (배지 + 제목 + 댓글수 + 첨부) | AUTHOR | DATE | VIEWS

[ widget 모드 - 메인 페이지 위젯 ]
배지 + 제목
작성자                               N시간 전
```

### mode별 렌더링 차이

| 항목 | table | widget |
|------|-------|--------|
| NO | ✅ | ❌ |
| TITLE | ✅ | ✅ |
| 배지 | ✅ | ✅ |
| AUTHOR | ✅ | ✅ (하단 소자) |
| DATE | ✅ 날짜 | ✅ N시간 전 |
| VIEWS | ✅ | ❌ |
| ~~HOT 배지~~ | 주석 처리 | 주석 처리 |
| 첨부파일 아이콘 | ✅ | ❌ |
| 클릭 → 상세 이동 | ✅ | ✅ |

```jsx
// 게시판 목록 페이지
<PostList posts={paginatedPosts} mode="table" />

// 메인 페이지 위젯
<PostList posts={widgetPosts} mode="widget" />
```

---

## 📄 BoardListPage 레이아웃

```
┌──────────────────────────────────────────────┐
│  FilterTab (전체 / 자유 / 공지 / 프로그램)      │
├──────────────────────────────────────────────┤
│  최근 게시물                    🔽 🔍 + 새 게시물│
│  ──────────────────────────────────────────  │
│  NO │ TITLE                  │AUTHOR│ DATE   │
│ 124 │ 공지 제목               │관리자 │ 05.20  │
│ 123 │ 행정 제목               │이정우 │ 05.18  │
├──────────────────────────────────────────────┤
│           |< < 1 2 3 4 5 > >|               │
│  [제목+내용 ▼]  [검색어 입력      ]  [검색]    │
└──────────────────────────────────────────────┘
```

---

## 🔄 데이터 흐름

```
마운트 시 1회
getPostList(facilityId, type=null, size=9999)
        ↓
   allPosts 저장 (원본, 변경 안 함)
        ↓
탭 변경 → type 기준 프론트 필터링
검색어 입력 → title / content / all 기준 프론트 필터링
        ↓
   filteredPosts (useMemo)
        ↓
페이지네이션 → slice(page * PAGE_SIZE, (page+1) * PAGE_SIZE)
        ↓
   paginatedPosts (useMemo)
        ↓
   PostList 렌더링

CRUD 완료 후 → fetchAllPosts() 재호출 → allPosts 갱신
```

> 삭제는 hard delete (DB 완전 제거)이므로
> 재호출 시 삭제된 게시글이 자연스럽게 제외됨

---

## 🗂️ BoardContext

```
상태:
- allPosts          // 원본 전체 데이터 (변경 안 함)
- currentTab        // 현재 탭 type (null = 전체)
- currentPage       // 현재 페이지 (0-based)
- searchKeyword     // 검색 키워드
- searchType        // 'title' | 'content' | 'all'
- isLoading
- error

파생 상태 (useMemo):
- filteredPosts     // 탭 + 검색 필터 적용
- totalPages        // filteredPosts.length / PAGE_SIZE
- paginatedPosts    // filteredPosts.slice 결과

액션:
- fetchAllPosts()         // 마운트 + CRUD 완료 후 재호출
- changeTab(type)         // currentPage 0으로 초기화
- changePage(page)
- search(type, keyword)   // currentPage 0으로 초기화
- resetSearch()
```

> 메인 페이지 위젯은 BoardContext 사용 안 함
> 로컬 상태로 독립 처리, allPosts.slice(0, WIDGET_SIZE)

---

## 🛠️ Utils

### boardUtils.js
```
- getBadgeStyle(type)          // type → { label, color }
- getSearchTypeLabel(type)     // 검색타입 → 한글 라벨
// - isHotPost(views)          // 추후 추가
```

### dateUtils.js
```
- formatDate(dateStr)          // "2024.05.20" (table 모드)
- formatRelativeTime(dateStr)  // "2시간 전 / 어제" (widget 모드)
```

---

## 🔗 API 연결 요약 (postApi.js / Axios)

| 기능 | 서비스 메서드 | 프론트 호출 | 호출 시점 |
|------|-------------|------------|-----------|
| 전체 목록 | `getPostList` | `getPostList(facilityId, null, size=9999)` | 마운트 1회 + CRUD 후 재호출 |
| 단건 상세 | `getPost` | `getPost(facilityId, postId)` | 상세 페이지 진입 |
| 게시글 생성 | `createPost` | `createPost(facilityId, userId, data)` | 작성 완료 |
| 게시글 수정 | `updatePost` | `updatePost(facilityId, postId, userId, data)` | 수정 완료 |
| 게시글 삭제 | `deletePost` | `deletePost(facilityId, postId, userId)` | 삭제 확인 |
| 내 게시글 | `getUserActivePosts` | `getMyPosts(facilityId, userId)` | 마이페이지 |
| 임시저장 | `getUserDrafts` | `getMyDrafts(facilityId, userId)` | 마이페이지 |
| ~~검색~~ | ~~`searchPost`~~ | 프론트 필터링으로 대체 | 사용 안 함 |

---

## ✅ 최종 확인 사항

| 항목 | 상태 |
|------|------|
| post.type 필드명 | ✅ 일치 확인 |
| 삭제 방식 | ✅ hard delete |
| HOT 배지 | ⏸ 주석 처리 (추후 추가) |
| PAGE_SIZE | ✅ 상수로 분리 (기본 20) |
| WIDGET_SIZE | ✅ 상수로 분리 (기본 2) |
| Express 역할 | ❓ 미확인 |
| Axios 인스턴스 기설정 여부 | ❓ 미확인 |
| Swagger 위치 | ❓ 미확인 |
