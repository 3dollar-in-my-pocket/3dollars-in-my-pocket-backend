package com.depromeet.threedollar.common.utils;

import com.depromeet.threedollar.common.exception.model.InvalidException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileUtilsTest {

    @CsvSource({
        "image.jpeg, .jpeg",
        "test.png, .png"
    })
    @ParameterizedTest
    void 파일의_확장자를_가져온다(String fileName, String extension) {
        // when
        String sut = FileUtils.getFileExtension(fileName);

        // then
        assertThat(sut).isEqualTo(extension);
    }

    @ValueSource(strings = {"image", "image."})
    @ParameterizedTest
    void 잘못된_파일의_확장자인경우_Validation_Exception_에러가_발생한다(String fileName) {
        // when & then
        assertThatThrownBy(() -> FileUtils.getFileExtension(fileName)).isInstanceOf(InvalidException.class);
    }

}
