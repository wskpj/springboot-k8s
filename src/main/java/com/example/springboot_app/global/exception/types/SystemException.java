package com.example.springboot_app.global.exception.types;

import com.example.springboot_app.global.exception.enums.GlobalError;
import com.example.springboot_app.global.exception.types.base.SystemBaseException;

public class SystemException {


    public static class Server extends SystemBaseException {
        public Server() {
            // TODO: Server 에러 코드 정의 또는 구체화
            super(GlobalError.INTERNAL_SERVER_ERROR);
        }

        public Server(Object details) {
            super(GlobalError.INTERNAL_SERVER_ERROR, details);
        }
    }

    public static class Database extends SystemBaseException {
        public Database() {
            // TODO: Database 에러 코드 정의 또는 구체화
            super(GlobalError.INTERNAL_SERVER_ERROR);
        }

        public Database(Object details) {
            super(GlobalError.INTERNAL_SERVER_ERROR, details);
        }
    }

    public static class Redis extends SystemBaseException {
        public Redis() {
            // TODO: Redis 에러 코드 정의 또는 구체화
            super(GlobalError.INTERNAL_SERVER_ERROR);
        }

        public Redis(Object details) {
            super(GlobalError.INTERNAL_SERVER_ERROR, details);
        }
    }

    public static class Infrastructure extends SystemBaseException {
        public Infrastructure() {
            // TODO: Infrastructure 에러 코드 정의 또는 구체화
            super(GlobalError.INTERNAL_SERVER_ERROR);
        }

        public Infrastructure(Object details) {
            super(GlobalError.INTERNAL_SERVER_ERROR, details);
        }
    }
}
