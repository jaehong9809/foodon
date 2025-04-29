package com.foodon.foodon.intakeLog.presentation;

import com.foodon.foodon.auth.annotation.AuthMember;
import com.foodon.foodon.common.dto.Response;
import com.foodon.foodon.common.util.ResponseUtil;
import com.foodon.foodon.intakeLog.application.IntakeLogService;
import com.foodon.foodon.intakeLog.dto.IntakeInfoResponse;
import com.foodon.foodon.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/intake")
public class IntakeLogController {

    private final IntakeLogService intakeLogService;

    @GetMapping
    @Operation(summary = "달력 섭취 기록 조회")
    public ResponseEntity<Response<List<IntakeInfoResponse>>> getIntakeLogByMonth(
            @RequestParam(name = "date") String date,
            @Parameter(hidden = true) @AuthMember Member member
    ) {
        List<IntakeInfoResponse> result = intakeLogService.getIntakeLogsByMonth(date, member);
        return ResponseUtil.success(result);
    }

}
