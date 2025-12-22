package com.example.springboot_app.api.admin.dto;







import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AdminCouponRequest {

    @Schema(description = "쿠폰 생성 요청")
    public record Create(
            @Schema(description = "쿠폰명", example = "신규 가입 감사 쿠폰")
            @NotBlank String title,

            @Schema(description = "총 발급 수량", example = "5000")
            @NotNull @Positive Integer totalQuantity
    ) {}
}
