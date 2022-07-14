package com.depromeet.threedollar.domain.redis.core

import com.depromeet.threedollar.domain.redis.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.data.redis.core.RedisTemplate
import java.time.Duration

internal class StringRedisRepositoryTest(
    private val stringRedisRepository: StringRedisRepository<MockRedisKey, String>,
    private val redisTemplate: RedisTemplate<String, String>,
) : IntegrationTest() {

    private val operations = redisTemplate.opsForValue()

    @AfterEach
    fun cleanUp() {
        redisTemplate.delete(listOf(KEY1.getKey(), KEY2.getKey()))

        val keys = (startKey..endKey)
            .map { keyId -> MockRedisKey(keyId.toString()).getKey() }
        redisTemplate.delete(keys)
    }

    @Nested
    inner class GetOperationTest {

        @Test
        fun `단건 키를 조회한다`() {
            // given
            val value = "Value1"
            operations.set(KEY1.getKey(), value)

            // when
            val sut = stringRedisRepository.get(KEY1)

            // then
            assertThat(sut).isEqualTo(value)
        }

        @Test
        fun `단건 키 조회시 값이 없는 경우 NULL`() {
            // when
            val sut = stringRedisRepository.get(KEY1)

            // then
            assertThat(sut).isNull()
        }

    }

    @Nested
    inner class BulkGetOperationTest {

        @Test
        fun `다건 키를 조회한다`() {
            // given
            val value1 = "Value1"
            val value2 = "Value2"

            operations.multiSet(mapOf(
                KEY1.getKey() to value1,
                KEY2.getKey() to value2
            ))

            // when
            val sut: List<String> = stringRedisRepository.getBulk(listOf(KEY1, KEY2))

            // then
            assertThat(sut).hasSize(2)
            assertThat(sut[0]).isEqualTo(value1)
            assertThat(sut[1]).isEqualTo(value2)
        }

        @Test
        fun `다건 키 조회시 값이 없는 경우 NULL`() {
            // when
            val sut: List<String> = stringRedisRepository.getBulk(listOf(KEY1, KEY2))

            // then
            assertThat(sut).hasSize(2)
            assertThat(sut[0]).isNull()
            assertThat(sut[1]).isNull()
        }

    }

    @Nested
    inner class SetOperationTest {

        @Test
        fun `단건 키를 저장한다`() {
            // given
            val value = "Value1"

            // when
            stringRedisRepository.set(KEY1, value)

            // then
            val sut = operations.get(KEY1.getKey())
            assertThat(sut).isEqualTo(value)
        }

    }

    @Nested
    inner class SetBulkOperationTest {

        @Test
        fun `다건 키를 저장한다`() {
            // given
            val value = "value"

            val keys: Map<MockRedisKey, String> = (startKey..endKey)
                .associate { MockRedisKey(it.toString()) to value }

            // when
            stringRedisRepository.setBulk(keys)

            // then
            val sut1 = operations.get(MockRedisKey(startKey.toString()).getKey())
            assertThat(sut1).isEqualTo(value)

            val sut2 = operations.get(MockRedisKey(endKey.toString()).getKey())
            assertThat(sut2).isEqualTo(value)
        }

    }

    @Nested
    inner class IncrOperationTest {

        @Test
        fun `단건 키를 1증가한다`() {
            // given
            operations.increment(KEY1.getKey())

            // when
            stringRedisRepository.incr(KEY1)

            // then
            val sut = operations.get(KEY1.getKey())
            assertThat(sut).isEqualTo("2")
        }

        @Test
        fun `단건 키에 대해 1 증가시, 값이 없는 경우 1이 된다`() {
            // when
            stringRedisRepository.incr(KEY1)

            // then
            val sut = operations.get(KEY1.getKey())
            assertThat(sut).isEqualTo("1")
        }

    }

    @Nested
    inner class IncrBulkOperationTest {

        @Test
        fun `다건 키에 대해 1 증가한다`() {
            // given
            val keys = (startKey..endKey)
                .map { MockRedisKey(it.toString()) }
                .toList()

            // when
            stringRedisRepository.incrBulk(keys)

            // then
            val sut1 = operations.get(MockRedisKey(startKey.toString()).getKey())
            assertThat(sut1).isEqualTo("1")

            val sut2 = operations.get(MockRedisKey(endKey.toString()).getKey())
            assertThat(sut2).isEqualTo("1")
        }

    }

    @Nested
    inner class IncrByOperationTest {

        @Test
        fun `단건 키에 대해 특정 값만큼 증가한다`() {
            // when
            stringRedisRepository.incrBy(KEY1, 2)

            // then
            val sut = operations.get(KEY1.getKey())
            assertThat(sut).isEqualTo("2")
        }

    }

    @Nested
    inner class DecrOperationTest {

        @Test
        fun `단건 키를 1 감소한다`() {
            // given
            operations.increment(KEY1.getKey())

            // when
            stringRedisRepository.decr(KEY1)

            // then
            val sut = operations.get(KEY1.getKey())
            assertThat(sut).isEqualTo("0")
        }

        @Test
        fun `단건 키에 대해 1감소시, 해당 키에 대한 값이 없는 경우 -1이 된다`() {
            // when
            stringRedisRepository.decr(KEY1)

            // then
            val sut = operations.get(KEY1.getKey())
            assertThat(sut).isEqualTo("-1")
        }

    }

    @Nested
    inner class DecrBulkOperationTest {

        @Test
        fun `다건 키를 1 감소한다`() {
            // given
            val keys = (startKey..endKey)
                .map { MockRedisKey(it.toString()) }
                .toList()

            // when
            stringRedisRepository.decrBulk(keys)

            // then
            val sut1 = operations.get(MockRedisKey(startKey.toString()).getKey())
            assertThat(sut1).isEqualTo("-1")

            val sut2 = operations.get(MockRedisKey(endKey.toString()).getKey())
            assertThat(sut2).isEqualTo("-1")
        }

    }

    @Nested
    inner class DecrByOperationTest {

        @Test
        fun `단건 키애 대해 특정 값 만큼 감소한다`() {
            // when
            stringRedisRepository.decrBy(KEY1, 2)

            // then
            val sut = operations.get(KEY1.getKey())
            assertThat(sut).isEqualTo("-2")
        }

    }

    @Nested
    inner class DelOperationTest {

        @Test
        fun `단건 키를 삭제한다`() {
            // given
            operations.set(KEY1.getKey(), "1")

            // when
            stringRedisRepository.del(KEY1)

            // then
            val sut = operations.get(KEY1.getKey())
            assertThat(sut).isNull()
        }

    }

    @Nested
    inner class DelBulkTest {

        @Test
        fun `다건 키를 삭제한다`() {
            // given
            val keys = (startKey..endKey)
                .map { MockRedisKey(it.toString()) }
                .toList()

            for (key in keys) {
                operations.set(key.getKey(), "VALUE")
            }

            // when
            stringRedisRepository.delBulk(keys)

            // then
            val sut1 = operations.get(MockRedisKey(startKey.toString()).getKey())
            assertThat(sut1).isNull()

            val sut2 = operations.get(MockRedisKey(endKey.toString()).getKey())
            assertThat(sut2).isNull()
        }

    }

    companion object {
        private val KEY1 = MockRedisKey("KEY1")
        private val KEY2 = MockRedisKey("KEY2")

        private const val startKey = 0
        private const val endKey = 2000
    }

}


class MockRedisKey(
    private val id: String,
) : StringRedisKey<String> {

    override fun getKey(): String {
        return "key:${id}"
    }

    override fun deserializeValue(value: String?): String? {
        return value
    }

    override fun serializeValue(value: String): String {
        return value
    }

    override fun getTtl(): Duration? {
        return null
    }

}
