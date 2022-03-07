package com.depromeet.threedollar.domain.rds.user.domain.visit;

import com.depromeet.threedollar.domain.rds.user.domain.visit.repository.VisitHistoryRepositoryCustom;
import com.depromeet.threedollar.domain.rds.user.domain.visit.repository.VisitHistoryStatisticsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitHistoryRepository extends JpaRepository<VisitHistory, Long>, VisitHistoryRepositoryCustom, VisitHistoryStatisticsRepositoryCustom {

}
