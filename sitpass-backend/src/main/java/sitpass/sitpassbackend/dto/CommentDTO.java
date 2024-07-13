package sitpass.sitpassbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sitpass.sitpassbackend.model.Comment;
import sitpass.sitpassbackend.model.Review;
import sitpass.sitpassbackend.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {

    private Long id;
    private User user;
    @NotBlank
    private String text;
    private LocalDateTime createdAt;
    private Review review;
    private Comment parentComment;
    private List<Comment> replies;
    private Boolean isDeleted;

    public static CommentDTO convertToDto(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .user(comment.getUser())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .review(comment.getReview())
                .parentComment(comment.getParentComment())
                .replies(comment.getReplies())
                .isDeleted(comment.getIsDeleted())
                .build();
    }

    public Comment convertToModel() {
        return Comment.builder()
                .id(getId())
                .user(getUser())
                .text((getText()))
                .createdAt(getCreatedAt())
                .review(getReview())
                .parentComment(getParentComment())
                .replies(getReplies())
                .isDeleted(getIsDeleted())
                .build();
    }

}
