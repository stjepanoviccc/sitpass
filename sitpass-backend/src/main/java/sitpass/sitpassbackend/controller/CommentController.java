package sitpass.sitpassbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sitpass.sitpassbackend.dto.CommentDTO;
import sitpass.sitpassbackend.service.CommentService;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{parentCommentId}")
    public ResponseEntity<CommentDTO> findByParentComment(@PathVariable Long parentCommentId) {
        return ResponseEntity.ok(commentService.findByParentComment(parentCommentId));
    }

    @PostMapping("/reviews/{reviewId}")
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CommentDTO commentDTO, @PathVariable Long reviewId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(CREATED).body(commentService.create(commentDTO, reviewId, email));
    }

    @PostMapping("{parentCommentId}/reviews/{reviewId}")
    public ResponseEntity<CommentDTO> addReplyToComment(@Valid @RequestBody CommentDTO replyDTO, @PathVariable Long parentCommentId, @PathVariable Long reviewId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(CREATED).body(commentService.addReply(replyDTO, parentCommentId, reviewId, email));
    }

}
