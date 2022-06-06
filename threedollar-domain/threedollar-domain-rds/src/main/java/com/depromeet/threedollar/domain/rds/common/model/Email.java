package com.depromeet.threedollar.domain.rds.common.model;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.common.exception.model.InvalidException;
import com.depromeet.threedollar.common.exception.type.ErrorCode;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Email {

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z\\d._%+-]+@[A-Z\\d.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Column(nullable = false, length = 50)
    private String email;

    private Email(@NotNull String email) {
        verifyEmailFormat(email);
        this.email = email;
    }

    public static Email of(@NotNull String email) {
        return new Email(email);
    }

    private void verifyEmailFormat(@NotNull String email) {
        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new InvalidException(String.format("(%s)은 이메일 형식에 어긋납니다", email), ErrorCode.INVALID_EMAIL_FORMAT);
        }
    }

}
