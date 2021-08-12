package com.depromeet.threedollar.common.exception.notfound;

import com.depromeet.threedollar.common.exception.ErrorCode;
import com.depromeet.threedollar.common.exception.NotFoundException;

public class NotFoundStoreImageException extends NotFoundException {

    public NotFoundStoreImageException(String message) {
        super(message, ErrorCode.NOT_FOUND_STORE_IMAGE_EXCEPTION);
    }

}
