package nextpos.app.nextpos.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 100, nullable = false)
    private String firstname;

    @Column(length = 100, nullable = false)
    private String lastname;

    @Column(length = 255)
    private String profileImageUrl;

    @Column(length = 255)
    private String addressLine1;

    @Column(length = 255)
    private String addressLine2;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(length = 20)
    private String postalCode;

    @Column(length = 50)
    private String timezone;

    @Column(length = 10)
    private String language;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Column(length = 20)
    private String gender;

    @Column(length = 100)
    private String department;

    @Column(length = 100)
    private String positionTitle;
}