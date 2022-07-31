package com.depromeet.threedollar.domain.rds.domain.userservice.medal.collection;

import com.depromeet.threedollar.domain.rds.domain.userservice.medal.Medal;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalAcquisitionConditionType;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MedalObtainCollectionTest {

    @Test
    void 해당_조건에_대해_추가로_획득할_수있는_메달이_있는경우() {
        User user = UserFixture.create();

        Medal medal = MedalFixture.create("메달", MedalAcquisitionConditionType.NO_CONDITION, 0);

        MedalObtainCollection collection = MedalObtainCollection.of(List.of(medal), MedalAcquisitionConditionType.NO_CONDITION, user);

        // when
        boolean sut = collection.hasMoreMedalsCanBeObtained();

        // then
        assertThat(sut).isTrue();
    }

    @Test
    void 해당_조건에_대해_추가로_획득할_수있는_메달이_있는경우_역() {
        User user = UserFixture.create();

        Medal medal = MedalFixture.create("메달", MedalAcquisitionConditionType.NO_CONDITION, 0);

        MedalObtainCollection collection = MedalObtainCollection.of(List.of(medal), MedalAcquisitionConditionType.NO_CONDITION, user);

        // when
        boolean sut = collection.hasNoMoreMedalsCanBeObtained();

        // then
        assertThat(sut).isFalse();
    }

    @Test
    void 해당_조건에_대해_추가로_획득할_수있는_메달이_없는경우() {
        User user = UserFixture.create();

        Medal medal = MedalFixture.create("메달", MedalAcquisitionConditionType.NO_CONDITION, 0);
        user.addMedals(List.of(medal));

        MedalObtainCollection collection = MedalObtainCollection.of(List.of(medal), MedalAcquisitionConditionType.NO_CONDITION, user);

        // when
        boolean sut = collection.hasMoreMedalsCanBeObtained();

        // then
        assertThat(sut).isFalse();
    }

    @Test
    void 해당_조건에_대해_추가로_획득할_수있는_메달이_없는경우_2() {
        User user = UserFixture.create();

        Medal medal = MedalFixture.create("메달", MedalAcquisitionConditionType.NO_CONDITION, 0);
        user.addMedals(List.of(medal));

        MedalObtainCollection collection = MedalObtainCollection.of(List.of(medal), MedalAcquisitionConditionType.NO_CONDITION, user);

        // when
        boolean sut = collection.hasNoMoreMedalsCanBeObtained();

        // then
        assertThat(sut).isTrue();
    }

    @ValueSource(ints = {3, 4})
    @ParameterizedTest
    void 획득_조건에_만족하는_메달을_조회한다(int counts) {
        User user = UserFixture.create();

        Medal medal = MedalFixture.create("메달", MedalAcquisitionConditionType.NO_CONDITION, 3);

        MedalObtainCollection collection = MedalObtainCollection.of(List.of(medal), MedalAcquisitionConditionType.NO_CONDITION, user);

        // when
        List<Medal> sut = collection.getSatisfyMedalsCanBeObtained(counts);

        // then
        assertThat(sut).hasSize(1);
        assertThat(sut.get(0).getName()).isEqualTo(medal.getName());
        assertThat(sut.get(0).getAcquisitionCondition()).isEqualTo(medal.getAcquisitionCondition());
    }

    @ValueSource(ints = {0, 1, 2})
    @ParameterizedTest
    void 획득_조건에_만족하는_메달을_조회할떄_해당_조건_갯수_이하인경우_획득할_수없다(int counts) {
        User user = UserFixture.create();

        Medal medal = MedalFixture.create("메달", MedalAcquisitionConditionType.NO_CONDITION, 3);

        MedalObtainCollection collection = MedalObtainCollection.of(List.of(medal), MedalAcquisitionConditionType.NO_CONDITION, user);

        // when
        List<Medal> sut = collection.getSatisfyMedalsCanBeObtained(counts);

        // then
        assertThat(sut).isEmpty();
    }

    @ValueSource(ints = {3, 4})
    @ParameterizedTest
    void 획득_조건에_만족하는_메달을_조회할때_이미_보유중인_메달은_제외된다(int counts) {
        User user = UserFixture.create();

        Medal medal = MedalFixture.create("메달", MedalAcquisitionConditionType.NO_CONDITION, 3);
        user.addMedals(List.of(medal));

        MedalObtainCollection collection = MedalObtainCollection.of(List.of(medal), MedalAcquisitionConditionType.NO_CONDITION, user);

        // when
        List<Medal> sut = collection.getSatisfyMedalsCanBeObtained(counts);

        // then
        assertThat(sut).isEmpty();
    }

    @Test
    void 기본으로_획득할_수있는_메달을_조회한다() {
        User user = UserFixture.create();

        Medal medal1 = MedalFixture.create("기본적으로 제공하는 메달", MedalAcquisitionConditionType.NO_CONDITION, 0);
        Medal medal2 = MedalFixture.create("리뷰 작성 1회 이후 획득할 수 있는 메달 ", MedalAcquisitionConditionType.ADD_REVIEW, 1);
        Medal medal3 = MedalFixture.create("가게 작성 1회 이후 획득 가능", MedalAcquisitionConditionType.ADD_STORE, 1);

        MedalObtainCollection collection = MedalObtainCollection.of(List.of(medal1, medal2, medal3), MedalAcquisitionConditionType.NO_CONDITION, user);

        // when
        List<Medal> sut = collection.getSatisfyMedalsCanBeObtainedByDefault();

        // then
        assertThat(sut).hasSize(1);
        assertThat(sut.get(0).getName()).isEqualTo(medal1.getName());
        assertThat(sut.get(0).getAcquisitionCondition()).isEqualTo(medal1.getAcquisitionCondition());
    }

    @Test
    void 기본으로_획득할_수있는_메달을_조회할때_이미_보유중인_메달은_제외된다() {
        User user = UserFixture.create();

        Medal medal = MedalFixture.create("기본적으로 제공하는 메달", MedalAcquisitionConditionType.NO_CONDITION, 0);
        user.addMedals(List.of(medal));

        MedalObtainCollection collection = MedalObtainCollection.of(List.of(medal), MedalAcquisitionConditionType.NO_CONDITION, user);

        // when
        List<Medal> sut = collection.getSatisfyMedalsCanBeObtainedByDefault();

        // then
        assertThat(sut).isEmpty();
    }

}
