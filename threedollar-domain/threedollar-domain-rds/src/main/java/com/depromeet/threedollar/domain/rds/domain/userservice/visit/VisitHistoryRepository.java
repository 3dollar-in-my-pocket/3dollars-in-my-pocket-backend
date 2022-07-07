package com.depromeet.threedollar.domain.rds.domain.userservice.visit;

import com.depromeet.threedollar.domain.rds.domain.userservice.visit.repository.VisitHistoryRepositoryCustom;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.repository.statistics.VisitHistoryStatisticsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitHistoryRepository extends JpaRepository<VisitHistory, Long>, VisitHistoryRepositoryCustom, VisitHistoryStatisticsRepositoryCustom {

}
