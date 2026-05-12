package com.chis.trugarden.persistence.user.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "address")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String principalStreet;
    private String secondaryStreet;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String houseNumber;
    private String zipCode;
    private String sector;
    private String city;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String sessionId;
}
