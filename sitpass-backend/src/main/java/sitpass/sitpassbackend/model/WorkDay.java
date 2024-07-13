package sitpass.sitpassbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import sitpass.sitpassbackend.model.enums.DayOfWeek;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="workDay")
public class WorkDay implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="valid_from")
    @Builder.Default
    private LocalDate validFrom = LocalDate.now();

    @Column(name="day", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

    @Column(name="`from`", nullable = false)
    private LocalTime from;

    @Column(name="until", nullable = false)
    private LocalTime until;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    @JsonIgnore
    private Facility facility;

    @Column(name="is_deleted")
    private Boolean isDeleted;

}
