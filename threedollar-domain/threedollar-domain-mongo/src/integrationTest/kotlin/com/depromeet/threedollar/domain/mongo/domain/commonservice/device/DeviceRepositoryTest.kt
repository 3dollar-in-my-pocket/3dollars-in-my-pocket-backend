package com.depromeet.threedollar.domain.mongo.domain.commonservice.device

import com.depromeet.threedollar.domain.mongo.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class DeviceRepositoryTest(
    private val deviceRepository: DeviceRepository,
) : IntegrationTest() {

    @AfterEach
    fun cleanUp() {
        deviceRepository.deleteAll()
    }

    @Nested
    inner class FindDeviceByAccountIdAndTypeTest {

        @Test
        fun `계정에 해당하는 디바이스를 조회한다`() {
            // given
            val accountType = AccountType.BOSS_ACCOUNT
            val accountId = "accountId"
            val pushToken = "pushToken"

            val device = DeviceFixture.create(accountId = accountId, accountType = accountType, pushToken = pushToken)
            deviceRepository.save(device)

            // when
            val sut = deviceRepository.findDeviceByAccountIdAndType(accountId = accountId, accountType = accountType)

            // then
            assertThat(sut).isNotNull
            assertThat(sut?.accountType).isEqualTo(accountType)
            assertThat(sut?.accountId).isEqualTo(accountId)
            assertThat(sut?.deviceInfo?.pushToken).isEqualTo(pushToken)
        }

        @Test
        fun `해당하는 계정에 등록된 디바이스가 없는경우 null을 반환한다`() {
            // when
            val sut = deviceRepository.findDeviceByAccountIdAndType(accountId = "notFoundAccountId", accountType = AccountType.BOSS_ACCOUNT)

            // then
            assertThat(sut).isNull()
        }

    }

    @Nested
    inner class ExistsDeviceByAccountIdAndTypeTest {

        @Test
        fun `계정에 등록된 디바이스가 존재하는지 여부를 확인한다 - 존재하는 경우`() {
            // given
            val accountType = AccountType.BOSS_ACCOUNT
            val accountId = "accountId"

            val device = DeviceFixture.create(accountId = accountId, accountType = accountType)
            deviceRepository.save(device)

            // when
            val sut = deviceRepository.existsDeviceByAccountIdAndType(accountId = accountId, accountType = accountType)

            // then
            assertThat(sut).isTrue
        }

        @Test
        fun `계정에 등록된 디바이스가 존재하는지 여부를 확인한다 - 존재하지 않는 경우`() {
            // when
            val sut = deviceRepository.existsDeviceByAccountIdAndType(accountId = "notRegistrationAccountId", accountType = AccountType.BOSS_ACCOUNT)

            // then
            assertThat(sut).isFalse
        }

    }

    @Nested
    inner class FindAllDevicesByAccountIdsAndTypeTest {

        @Test
        fun `계정 목록들에 등록된 디바이스가 존재하는지 여부를 확인한다 - 존재하는 경우`() {
            // given
            val accountType = AccountType.BOSS_ACCOUNT

            val device1 = DeviceFixture.create(accountId = "accountId1", accountType = accountType)
            val device2 = DeviceFixture.create(accountId = "accountId2", accountType = accountType)
            deviceRepository.saveAll(listOf(device1, device2))

            // when
            val sut = deviceRepository.findAllDevicesByAccountIdsAndType(accountIds = listOf(device1.accountId, device2.accountId), accountType = accountType)

            // then
            assertThat(sut).hasSize(2)
            assertDevice(sut[0], accountType = device1.accountType, accountId = device1.accountId, pushToken = device1.deviceInfo.pushToken)
            assertDevice(sut[1], accountType = device2.accountType, accountId = device2.accountId, pushToken = device2.deviceInfo.pushToken)
        }

        @Test
        fun `계정 목록들에 등록된 디바이스가 존재하는지 여부를 확인한다 - 존재하지 않는 경우 빈 리스트를 반환한다`() {
            // when
            val sut = deviceRepository.findAllDevicesByAccountIdsAndType(accountIds = listOf("accountId"), accountType = AccountType.BOSS_ACCOUNT)

            // then
            assertThat(sut).isEmpty()
        }

    }

    private fun assertDevice(device: Device, accountId: String, accountType: AccountType, pushToken: String) {
        assertThat(device.accountId).isEqualTo(accountId)
        assertThat(device.accountType).isEqualTo(accountType)
        assertThat(device.deviceInfo.pushToken).isEqualTo(pushToken)
    }

}
