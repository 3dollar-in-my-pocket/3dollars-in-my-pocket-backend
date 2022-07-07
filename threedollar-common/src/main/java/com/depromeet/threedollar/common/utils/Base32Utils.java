package com.depromeet.threedollar.common.utils;

import com.depromeet.threedollar.common.exception.model.InvalidException;
import com.depromeet.threedollar.common.exception.type.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Base32;

import java.util.zip.CRC32;
import java.util.zip.Checksum;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Base32Utils {

    private static final Base32 BASE_32 = new Base32();
    private static final String PADDING = "#!@";

    public static String encode(Long originValue) {
        String base32 = BASE_32.encodeAsString((PADDING + originValue).getBytes());
        return checksum(base32) + base32;
    }

    public static Long decode(String encodingValue) {
        String base32 = encodingValue.substring(1);
        if (checksum(base32) != encodingValue.charAt(0)) {
            throw new InvalidException(String.format("encodingKey: (%s) 체크섬이 일치하지 않습니다", encodingValue), ErrorCode.INVALID_ENCODING_ID);
        }
        return Long.valueOf(new String(BASE_32.decode(base32)).split(PADDING)[1]);
    }

    private static char checksum(String base36) {
        Checksum crc = new CRC32();
        crc.update(base36.getBytes(), 0, base36.length());
        String crcBase36 = Long.toString(crc.getValue());
        return Long.toString(crc.getValue()).charAt(crcBase36.length() - 1);
    }

}
