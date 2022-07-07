package com.depromeet.threedollar.domain.rds.domain.commonservice.faq;

import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.repository.FaqRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Long>, FaqRepositoryCustom {

}
