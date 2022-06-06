package com.depromeet.threedollar.api.user.controller.visit;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.depromeet.threedollar.api.user.config.interceptor.Auth;
import com.depromeet.threedollar.api.user.config.resolver.UserId;
import com.depromeet.threedollar.api.user.service.visit.VisitHistoryService;
import com.depromeet.threedollar.api.user.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.api.user.service.visit.dto.request.RetrieveMyVisitHistoriesRequest;
import com.depromeet.threedollar.api.user.service.visit.dto.response.VisitHistoriesCursorResponse;
import com.depromeet.threedollar.domain.rds.event.userservice.visit.VisitHistoryAddedEvent;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class VisitHistoryController {

    private final VisitHistoryService visitHistoryService;
    private final ApplicationEventPublisher eventPublisher;

    @ApiOperation("[인증] 특정 가게에 방문 인증을 추가합니다")
    @Auth
    @PostMapping("/v2/store/visit")
    public ApiResponse<String> addVisitHistory(
        @Valid @RequestBody AddVisitHistoryRequest request,
        @UserId Long userId
    ) {
        Long visitHistoryId = visitHistoryService.addVisitHistory(request, userId, LocalDate.now());
        eventPublisher.publishEvent(VisitHistoryAddedEvent.of(visitHistoryId, userId));
        return ApiResponse.OK;
    }

    @ApiOperation("[인증] 내가 방문한 이력을 조회합니다. (스크롤 페이지네이션)")
    @Auth
    @GetMapping("/v2/store/visits/me")
    public ApiResponse<VisitHistoriesCursorResponse> retrieveMyVisitHistories(
        @Valid RetrieveMyVisitHistoriesRequest request,
        @UserId Long userId
    ) {
        return ApiResponse.success(visitHistoryService.retrieveMyVisitHistories(request, userId));
    }

}
