package com.tommy.bootrest.acount.exception;

import com.tommy.bootrest.common.exception.NotMatchResourceException;

public class NotMatchAccountException extends NotMatchResourceException {

    private static final String EXCEPTION_MESSAGE_NOT_MATCH_ACCOUNT = "자신이 관리하는 이벤트만 접근 권한이 있습니다.";

    public NotMatchAccountException() {
        super(EXCEPTION_MESSAGE_NOT_MATCH_ACCOUNT);
    }
}
