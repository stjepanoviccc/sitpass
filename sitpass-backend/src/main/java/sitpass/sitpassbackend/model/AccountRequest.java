package sitpass.sitpassbackend.model;

import jakarta.persistence.*;
import lombok.*;
import sitpass.sitpassbackend.model.enums.RequestStatus;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="accountRequest")
public class AccountRequest {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="email", nullable = false)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="rejection_reason")
    private String rejectionReason;

    @Column(name="created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name="address", nullable = false)
    private String address;

    @Column(name="request_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

}
