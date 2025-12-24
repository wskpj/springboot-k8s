package com.example.springboot_app.domain.coupon.exception;

import com.example.lib.common.core.exception.BaseDomainException;
import com.example.lib.web.core.error.GlobalError;

public class CouponException {

    public static class NotFound extends BaseDomainException {
        public NotFound() {
            super(GlobalError.NOT_FOUND, "Coupon not found.");
        }

        public NotFound(Long id) {
            super(GlobalError.NOT_FOUND, "Coupon ID [" + id + "] not found.");
        }
    }

    public static class OutOfStock extends BaseDomainException {
        public OutOfStock() {
            super(GlobalError.BAD_REQUEST, "Coupon stock is exhausted.");
        }

        public OutOfStock(Long id) {
            super(GlobalError.BAD_REQUEST, "Coupon ID [" + id + "] stock is exhausted.");
        }
    }

    public static class AlreadyIssued extends BaseDomainException {
        public AlreadyIssued() {
            super(GlobalError.CONFLICT, "Coupon already issued to this user.");
        }
    }
}
