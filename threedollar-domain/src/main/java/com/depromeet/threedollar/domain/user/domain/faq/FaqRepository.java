package com.depromeet.threedollar.domain.user.domain.faq;

import com.depromeet.threedollar.domain.user.domain.faq.repository.FaqRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Long>, FaqRepositoryCustom {

}
