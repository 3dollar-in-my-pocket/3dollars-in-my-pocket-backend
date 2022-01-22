package com.depromeet.threedollar.domain.user.domain.admin;

import com.depromeet.threedollar.domain.common.domain.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Admin extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 30)
    private String name;

    @Builder(access = AccessLevel.PACKAGE)
    private Admin(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public static Admin newInstance(String email, String name) {
        return new Admin(email, name);
    }

}
