package hiddencore.ddasum.backend.web.dto;

import java.time.LocalDateTime;

import hiddencore.ddasum.backend.domain.Post;
import hiddencore.ddasum.backend.domain.Post.PostStatus;
import hiddencore.ddasum.backend.domain.Post.PostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PostDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest { // 글 작성할 때 받는 데이터

        @NotBlank(message = "제목은 필수입니다")
        @Size(max = 200, message = "제목은 200자 이하여야 합니다")
        private String title;

        @NotBlank(message = "내용은 필수입니다")
        private String content;

        @NotNull(message = "게시글 타입은 필수입니다")
        private PostType type; // NOTICE | BOARD | PROGRAM

        @NotNull(message = "게시글 상태는 필수입니다")
        private PostStatus status; // ACTIVE | INACTIVE | RESERVE

        private Integer capacity; // PROGRAM일 때만 사용
        private LocalDateTime startAt; // PROGRAM일 때만 사용
        private LocalDateTime endAt; // PROGRAM일 때만 사용
        private String attachmentUrls;
        private LocalDateTime reservationAt; // 예약 시간. null이면 바로 게시

    } //

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostResponse { // 단일 게시글 조회

        private Long id;
        private Long facilityId;
        private String authorName;
        private PostType type;
        private String title;
        private String content;
        private PostStatus status;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private Integer capacity;
        private Integer currentEnrolled;
        private String attachmentUrls;
        private LocalDateTime reservationAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static PostResponse from(Post post) { // 단일 게시글 조회

            return PostResponse.builder()
                    .id(post.getPostId())
                    .facilityId(post.getFacilityId().getFacilityId())
                    .authorName(post.getAuthorUserId().getName())
                    .type(post.getType())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .status(post.getStatus())
                    .startAt(post.getStartAt())
                    .endAt(post.getEndAt())
                    .capacity(post.getCapacity())
                    .currentEnrolled(post.getCurrentEnrolled())
                    .attachmentUrls(post.getAttachmentUrls())
                    .reservationAt(post.getReservationAt())
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .build();
        }//

        public String getRecruitStatus() {
            if (startAt == null || endAt == null)
                return null;
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(startAt))
                return "모집 예정";
            if (now.isAfter(endAt))
                return "마감";
            return "모집 중";
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostListResponse { // 전체 게시글 조회해서 보내는 데이터

        private Long id;
        private Long facilityId;
        private String authorName;
        private PostType type;
        private String title;
        private PostStatus status;
        private Integer capacity;
        private Integer currentEnrolled;
        private LocalDateTime updatedAt;
        private LocalDateTime startAt, endAt;

        // 전체 게시글 조회
        public static PostListResponse from(Post post) {
            return PostListResponse.builder()
                    .id(post.getPostId())
                    .facilityId(post.getFacilityId().getFacilityId())
                    .authorName(post.getAuthorUserId().getName())
                    .type(post.getType()) // 타입으로 필터링
                    .title(post.getTitle())
                    .status(post.getStatus())
                    .capacity(post.getCapacity())
                    .currentEnrolled(post.getCurrentEnrolled())
                    .updatedAt(post.getUpdatedAt())
                    .startAt(post.getStartAt())
                    .endAt(post.getEndAt())
                    .build();
        }

        public String getRecruitStatus() {
            if (startAt == null || endAt == null)
                return null;
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(startAt))
                return "모집 예정";
            if (now.isAfter(endAt))
                return "마감";
            return "모집 중";
        }

    }

    // CHECK!!
    // 게시글 수정
    // 공지사항, 프로그램, 일반 게시글 모두 다른 형태의 Update를 가짐.
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {

        @NotBlank(message = "제목은 필수입니다")
        @Size(max = 200, message = "제목은 200자 이하여야 합니다")
        private String title;

        @NotBlank(message = "내용은 필수입니다")
        private String content;

        // CHECK!! 공지/일반 게시글은 아래 필드 null로 전달 ? 공지 게시글 필수 필드
        // PROGRAM일 때만 사용
        private PostStatus status;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private Integer capacity;
        private String attachmentUrls;
        private LocalDateTime reservationAt;
    }

}
