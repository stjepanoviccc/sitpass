package sitpass.sitpassbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="exercise")
public class Exercise implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name="facility_id", nullable=false)
    @JsonIgnore
    private Facility facility;

    @Column(name="`from`", nullable = false)
    private LocalDateTime from;

    @Column(name="until", nullable = false)
    private LocalDateTime until;

    @Column(name="is_deleted")
    private Boolean isDeleted;


}
