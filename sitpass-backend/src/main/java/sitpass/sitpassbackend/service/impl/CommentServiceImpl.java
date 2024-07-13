package sitpass.sitpassbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import sitpass.sitpassbackend.dto.CommentDTO;
import sitpass.sitpassbackend.dto.ReviewDTO;
import sitpass.sitpassbackend.exception.BadRequestException;
import sitpass.sitpassbackend.exception.NotFoundException;
import sitpass.sitpassbackend.model.Comment;
import sitpass.sitpassbackend.model.Review;
import sitpass.sitpassbackend.model.User;
import sitpass.sitpassbackend.repository.CommentRepository;
import sitpass.sitpassbackend.service.CommentService;
import sitpass.sitpassbackend.service.ReviewService;
import sitpass.sitpassbackend.service.UserService;

import java.time.LocalDateTime;

import static sitpass.sitpassbackend.dto.CommentDTO.convertToDto;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ReviewService reviewService;
    private final UserService userService;

    @Override
    public Comment getModel(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with ID %s not found.", id)));
    }

    @Override
    public CommentDTO findByParentComment(Long parentCommentId) {
        Comment parentComment = getModel(parentCommentId);
        if (parentComment != null) {
            Comment comment = commentRepository.findByParentComment(parentComment);
            if (comment != null) {
                return convertToDto(comment);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public CommentDTO create(CommentDTO commentDTO, Long reviewId, String email) {
        Review review = reviewService.getModel(reviewId);
        User user = userService.getModel(email);

        commentDTO.setReview(review);
        commentDTO.setUser(user);
        commentDTO.setCreatedAt(LocalDateTime.now());

        CommentDTO savedComment = convertToDto(commentRepository.save(commentDTO.convertToModel()));

        review.setComment(savedComment.convertToModel());
        reviewService.update(ReviewDTO.convertToDto(review));

        return savedComment;
    }

    @Override
    public CommentDTO addReply(CommentDTO replyDTO, Long commentId, Long reviewId, String email) {
        Comment parentComment = getModel(commentId);
        if(parentComment.getUser().getEmail().equals(email)) {
            throw new BadRequestException("You can't reply to yourself.");
        }
        Review review = reviewService.getModel(reviewId);
        User user = userService.getModel(email);

        replyDTO.setReview(review);
        replyDTO.setUser(user);
        replyDTO.setParentComment(parentComment);
        replyDTO.setCreatedAt(LocalDateTime.now());

        Comment savedReply = commentRepository.save(replyDTO.convertToModel());

        parentComment.getReplies().add(savedReply);
        commentRepository.save(parentComment);

        return convertToDto(savedReply);
    }
}
