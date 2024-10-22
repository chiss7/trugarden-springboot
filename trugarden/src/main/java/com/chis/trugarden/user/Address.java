package com.chis.trugarden.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Address {
    @Id
    @GeneratedValue
    private Long id;
    private String street;
    private String houseNumber;
    private String zipCode;

    @OneToOne(mappedBy = "address")
    private User user;
}
