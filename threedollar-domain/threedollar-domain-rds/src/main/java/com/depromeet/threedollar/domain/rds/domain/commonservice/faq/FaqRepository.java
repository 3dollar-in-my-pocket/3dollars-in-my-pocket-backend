package com.depromeet.threedollar.domain.rds.domain.commonservice.faq;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.repository.FaqRepositoryCustom;

public interface FaqRepository extends JpaRepository<Faq, Long>, FaqRepositoryCustom {

}
