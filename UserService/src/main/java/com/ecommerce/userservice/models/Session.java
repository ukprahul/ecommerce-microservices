package com.ecommerce.userservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Getter
@Setter
public class Session extends BaseModel {

    @Column(length = 500)
    private String token;

    private Date expiringAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.ORDINAL)
    private SessionStatus sessionStatus;
}
