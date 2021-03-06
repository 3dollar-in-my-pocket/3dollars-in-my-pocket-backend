package com.depromeet.threedollar.domain.rds.domain.commonservice.admin;

import com.depromeet.threedollar.domain.rds.core.model.AuditingTimeEntity;
import com.depromeet.threedollar.domain.rds.core.model.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
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

    @Column(nullable = false, length = 50)
    private Email email;

    @Column(nullable = false, length = 30)
    private String name;

    private Long creatorAdminId;

    @Builder(access = AccessLevel.PACKAGE)
    private Admin(@NotNull String email, @NotNull String name, @Nullable Long creatorAdminId) {
        this.email = Email.of(email);
        this.name = name;
        this.creatorAdminId = creatorAdminId;
    }

    public static Admin newInstance(@NotNull String email, @NotNull String name, @Nullable Long creatorAdminId) {
        return new Admin(email, name, creatorAdminId);
    }

    public void updateName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getEmail() {
        return email.getEmail();
    }

}
