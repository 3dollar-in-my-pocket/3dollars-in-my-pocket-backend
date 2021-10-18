package com.depromeet.threedollar.api.controller.visit;

import com.depromeet.threedollar.api.config.interceptor.Auth;
import com.depromeet.threedollar.api.config.resolver.UserId;
import com.depromeet.threedollar.api.service.visit.VisitHistoryService;
import com.depromeet.threedollar.api.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.api.service.visit.dto.request.RetrieveVisitHistoryRequest;
import com.depromeet.threedollar.api.service.visit.dto.response.VisitHistoryResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class VisitHistoryController {

    private final VisitHistoryService visitHistoryService;

    @ApiOperation("[인증] 해당 가게에 오늘날짜로 방문 인증을 등록합니다.")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @Auth
    @PostMapping("/api/v1/store/visit")
    public ApiResponse<String> addStoreVisitHistory(@Valid @RequestBody AddVisitHistoryRequest request, @UserId Long userId) {
        visitHistoryService.addStoreVisitHistory(request, userId);
        return ApiResponse.SUCCESS;
    }

    @ApiOperation("[인증] 해당 가게에 등록된 방문기록들을 조회합니다.")
    @GetMapping("/api/v1/store/visits")
    public ApiResponse<Map<LocalDate, List<VisitHistoryResponse>>> retrieveStoreVisitHistories(@Valid RetrieveVisitHistoryRequest request) {
        return ApiResponse.success(visitHistoryService.retrieveStoreVisitHistory(request));
    }

}
