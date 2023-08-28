package com.soroosh.auth.project.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="clients")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String accessToken;

    @Column
    private String secret;

    @JoinColumn
    @ManyToOne
    private Application application;

}
