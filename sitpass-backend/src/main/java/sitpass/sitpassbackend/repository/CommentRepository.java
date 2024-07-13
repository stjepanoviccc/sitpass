package sitpass.sitpassbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sitpass.sitpassbackend.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.parentComment = :parentComment")
    Comment findByParentComment(Comment parentComment);
}
