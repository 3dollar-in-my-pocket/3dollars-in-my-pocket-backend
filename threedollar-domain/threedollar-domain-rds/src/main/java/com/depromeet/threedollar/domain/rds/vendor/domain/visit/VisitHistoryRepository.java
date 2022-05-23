package com.depromeet.threedollar.domain.rds.vendor.domain.visit;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.vendor.domain.visit.repository.VisitHistoryRepositoryCustom;
import com.depromeet.threedollar.domain.rds.vendor.domain.visit.repository.VisitHistoryStatisticsRepositoryCustom;

public interface VisitHistoryRepository extends JpaRepository<VisitHistory, Long>, VisitHistoryRepositoryCustom, VisitHistoryStatisticsRepositoryCustom {

}
