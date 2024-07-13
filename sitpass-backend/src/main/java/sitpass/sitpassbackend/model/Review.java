package sitpass.sitpassbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="review")
public class Review {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="facility_id", nullable=false)
    private Facility facility;

    @ManyToOne
    @JoinColumn(name="rate_id", nullable = false)
    @JsonIgnore
    private Rate rate;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="exercise_count")
    private Integer exerciseCount;

    @Column(name="hidden")
    private Boolean hidden;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Column(name="is_deleted")
    private Boolean isDeleted;

}
