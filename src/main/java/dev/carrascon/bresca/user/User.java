package dev.carrascon.bresca.user;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String googleId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    /* TODO: Add the model videos
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Video> videos = new HashSet<>();
    */
}