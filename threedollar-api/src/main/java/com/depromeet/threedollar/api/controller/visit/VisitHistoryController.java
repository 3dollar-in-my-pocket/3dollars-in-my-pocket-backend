package com.depromeet.threedollar.api.controller.visit;

import com.depromeet.threedollar.api.config.interceptor.Auth;
import com.depromeet.threedollar.api.config.resolver.UserId;
import com.depromeet.threedollar.api.service.visit.VisitHistoryService;
import com.depromeet.threedollar.api.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.api.service.visit.dto.request.RetrieveMyVisitHistoryRequest;
import com.depromeet.threedollar.api.service.visit.dto.response.MyVisitHistoriesScrollResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class VisitHistoryController {

    private final VisitHistoryService visitHistoryService;

    @ApiOperation("[인증] 가게 방문 인증 페이지 - 특정 가게에 대한 방문 인증을 추가합니다")
    @Auth
    @PostMapping("/api/v2/store/visit")
    public ApiResponse<String> addVisitHistory(@Valid @RequestBody AddVisitHistoryRequest request, @UserId Long userId) {
        visitHistoryService.addVisitHistory(request, userId);
        return ApiResponse.SUCCESS;
    }

    @ApiOperation("[인증] 마이페이지 - 내가 추가한 방문 인증 목록을 스크롤 페이지네이션으로 조회합니다.")
    @Auth
    @GetMapping("/api/v2/store/visits/me")
    public ApiResponse<MyVisitHistoriesScrollResponse> retrieveMyVisitHistories(@Valid RetrieveMyVisitHistoryRequest request, @UserId Long userId) {
        return ApiResponse.success(visitHistoryService.retrieveMyVisitHistories(request, userId));
    }

}
