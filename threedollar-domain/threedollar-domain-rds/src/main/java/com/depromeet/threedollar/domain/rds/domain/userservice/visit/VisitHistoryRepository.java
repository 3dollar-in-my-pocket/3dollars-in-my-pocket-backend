package com.depromeet.threedollar.domain.rds.domain.userservice.visit;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.domain.userservice.visit.repository.VisitHistoryRepositoryCustom;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.repository.VisitHistoryStatisticsRepositoryCustom;

public interface VisitHistoryRepository extends JpaRepository<VisitHistory, Long>, VisitHistoryRepositoryCustom, VisitHistoryStatisticsRepositoryCustom {

}
