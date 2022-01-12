package com.depromeet.threedollar.domain.user.domain.admin;

import com.depromeet.threedollar.domain.common.domain.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Admin extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private Admin(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public static Admin newInstance(String email, String name) {
        return new Admin(email, name);
    }

}
