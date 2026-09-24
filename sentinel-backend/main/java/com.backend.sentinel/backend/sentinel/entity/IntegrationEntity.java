package backend.sentinel.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "integrations")
public class IntegrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    /** Why this 3rd-party service is used. */
    @Column(nullable = false, length = 1000)
    private String purposeDescription;

    /** Link to the internal application that consumes this service. */
    @Column(nullable = false)
    private String appUrl;

    /** Brief description of what the 3rd-party API/service does. */
    @Column(nullable = false, length = 1000)
    private String apiDescription;

    /** Link to the 3rd-party provider's website. */
    @Column(nullable = false)
    private String apiWebsiteUrl;

    /** The user who registered this integration. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
