package com.depromeet.threedollar.domain.rds.domain.userservice.store;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 테스트 용도의 Repository
 */
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

}
