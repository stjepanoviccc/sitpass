package sitpass.sitpassbackend.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="rate")
public class Rate {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="equipment")
    private Integer equipment;

    @Column(name="staff")
    private Integer staff;

    @Column(name="hygene")
    private Integer hygene;

    @Column(name="space")
    private Integer space;

    @Column(name="is_deleted")
    private Boolean isDeleted;

}
