package sitpass.sitpassbackend.service;

import sitpass.sitpassbackend.dto.CommentDTO;
import sitpass.sitpassbackend.model.Comment;

public interface CommentService {

    Comment getModel(Long id);
    CommentDTO findByParentComment(Long parentCommentId);
    CommentDTO create(CommentDTO comment, Long reviewId, String email);
    CommentDTO addReply(CommentDTO replyDTO, Long commentId, Long reviewId, String email);

}
