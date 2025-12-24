package com.example.springboot_app.global.exception.types;

import com.example.lib.common.core.exception.BaseSystemException;
import com.example.lib.web.core.error.GlobalError;

public class SystemException {

    public static class Server extends BaseSystemException {
        public Server() {
            // TODO: Server 에러 코드 정의 또는 구체화
            super(GlobalError.INTERNAL_SERVER_ERROR);
        }

        public Server(Object details) {
            super(GlobalError.INTERNAL_SERVER_ERROR, details);
        }
    }

    public static class Database extends BaseSystemException {
        public Database() {
            // TODO: Database 에러 코드 정의 또는 구체화
            super(GlobalError.INTERNAL_SERVER_ERROR);
        }

        public Database(Object details) {
            super(GlobalError.INTERNAL_SERVER_ERROR, details);
        }
    }

    public static class Redis extends BaseSystemException {
        public Redis() {
            // TODO: Redis 에러 코드 정의 또는 구체화
            super(GlobalError.INTERNAL_SERVER_ERROR);
        }

        public Redis(Object details) {
            super(GlobalError.INTERNAL_SERVER_ERROR, details);
        }
    }

    public static class Infrastructure extends BaseSystemException {
        public Infrastructure() {
            // TODO: Infrastructure 에러 코드 정의 또는 구체화
            super(GlobalError.INTERNAL_SERVER_ERROR);
        }

        public Infrastructure(Object details) {
            super(GlobalError.INTERNAL_SERVER_ERROR, details);
        }
    }
}
