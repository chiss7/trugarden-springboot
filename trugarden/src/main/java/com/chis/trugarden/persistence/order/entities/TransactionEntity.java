package com.chis.trugarden.persistence.order.entities;

import com.chis.trugarden.persistence.user.entities.UserEntity;
import com.chis.trugarden.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction")
public class TransactionEntity extends BaseEntity {
    @ManyToOne
    private UserEntity customer;

    @OneToOne
    private OrderEntity order;
}
