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
    @GeneratedValue
    private Long id;
    private String street;
    private String houseNumber;
    private String zipCode;

    @OneToOne(mappedBy = "address")
    private UserEntity users;
}
