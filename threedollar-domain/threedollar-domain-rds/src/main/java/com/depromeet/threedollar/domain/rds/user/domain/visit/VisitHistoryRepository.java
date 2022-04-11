package com.depromeet.threedollar.domain.rds.user.domain.visit;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.user.domain.visit.repository.VisitHistoryRepositoryCustom;
import com.depromeet.threedollar.domain.rds.user.domain.visit.repository.VisitHistoryStatisticsRepositoryCustom;

public interface VisitHistoryRepository extends JpaRepository<VisitHistory, Long>, VisitHistoryRepositoryCustom, VisitHistoryStatisticsRepositoryCustom {

}
