package com.depromeet.threedollar.domain.rds.user.domain.admin;

import com.depromeet.threedollar.domain.rds.common.domain.AuditingTimeEntity;
import com.depromeet.threedollar.domain.rds.common.domain.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Admin extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private Email email;

    @Column(nullable = false, length = 30)
    private String name;

    private Long creatorAdminId;

    @Builder(access = AccessLevel.PACKAGE)
    private Admin(String email, String name, @Nullable Long creatorAdminId) {
        this.email = Email.of(email);
        this.name = name;
        this.creatorAdminId = creatorAdminId;
    }

    public static Admin newInstance(String email, String name, @Nullable Long creatorAdminId) {
        return new Admin(email, name, creatorAdminId);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email.getEmail();
    }

}
