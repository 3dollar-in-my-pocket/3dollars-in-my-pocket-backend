package com.depromeet.threedollar.common.utils;

import com.depromeet.threedollar.common.model.UserMetaValue;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMetaSessionUtils {

    private static final ThreadLocal<UserMetaValue> USER_META_THREAD_LOCAL = new ThreadLocal<>();

    public static UserMetaValue get() {
        return USER_META_THREAD_LOCAL.get();
    }

    public static void set(UserMetaValue userMetaValue) {
        USER_META_THREAD_LOCAL.set(userMetaValue);
    }

    public static void remove() {
        USER_META_THREAD_LOCAL.remove();
    }

}
