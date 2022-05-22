package com.depromeet.threedollar.domain.rds.user.domain.faq;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.user.domain.faq.repository.FaqRepositoryCustom;

public interface FaqRepository extends JpaRepository<Faq, Long>, FaqRepositoryCustom {

}
