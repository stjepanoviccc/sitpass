package sitpass.sitpassbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="comment")
public class Comment {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @JsonIgnore
    private User user;

    @Column(name="text", nullable = false)
    private String text;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "comment")
    @JsonIgnore
    private Review review;

    @ManyToOne
    @JoinColumn(name="parent_comment_id")
    @JsonIgnore
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> replies;

    @Column(name="is_deleted")
    private Boolean isDeleted;

}
