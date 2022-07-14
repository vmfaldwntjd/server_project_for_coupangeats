package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users/signUp
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),

    USERS_EMPTY_PHONE(false, 2018, "핸드폰 번호를 입력해주세요."),
    USERS_INVALID_PHONE(false, 2019, "핸드폰 번호 형식을 확인해주세요."),
    POST_USERS_EXISTS_PHONE(false, 2020, "중복된 핸드폰 번호입니다."),

    POST_USERS_EMPTY_PASSWORD(false, 2021, "비밀번호를 입력해주세요."),
    POST_USERS_INVALID_PASSWORD(false, 2022, "비밀번호 형식을 확인해주세요."),

    RESTAURANTS_EMPTY_CATEGORY_ID(false, 2023, "카테고리 번호를 입력해주세요."),
    RESTAURANTS_INVALID_SORT_BY(false, 2024, "유효하지 않은 정렬 기준입니다."),

    //[POST] /carts
    POST_CARTS_ANOTHER_RESTAURANT_ID_EXISTS(false, 2025, "다른 가게에 대한 카트 정보가 이미 존재합니다."),
    POST_CARTS_EMPTY_ESSENTIAL_OPTION(false, 2026, "필수 옵션을 설정하지 않았습니다."),
    GET_CARTS_TOO_MUCH_LONG_DISTANCE(false, 2027, "유효한 배달 거리가 아닙니다."),

    //[POST] /coupons
    POST_COUPONS_EXISTS_COUPON_NUM(false, 2028, "유효하지 않은 쿠폰 번호입니다."),
    POST_COUPONS_CORRECT_COUPON_NUM(false, 2029, "8자리 또는 16자리 쿠폰 번호를 입력해주세요."),

    NO_CART_FOR_USER_ID(false, 2030, "해당 유저에대한 카트정보가 없습니다."),
    INVALID_CART_ID(false, 2031, "존재하지 않는 cartId입니다."),

    //[POST] /cards
    POST_CARDS_EXISTS_CARD_NUM(false, 2032, "이미 등록한 카드 번호입니다."),
    POST_USER_PAYMENT_EMPTY_CARD_NUM(false, 2033, "카드 번호를 입력해주세요."),
    POST_USER_PAYMENT_INVALID_CARD_NUM(false, 2034, "유효하지 않은 카드번호입니다."),
    POST_USER_PAYMENT_EMPTY_PASSWORD(false, 2035, "비밀번호 앞 두 자리를 입력해주세요."),
    POST_USER_PAYMENT_EMPTY_CVC(false, 2036, "cvc번호를 입력해주세요."),
    POST_USER_PAYMENT_INVALID_CVC(false, 2037, "유효하지 않은 cvc입니다."),
    POST_USER_PAYMENT_EMPTY_VALID_YEAR(false, 2038, "유효기간(YY)을 입력해주세요."),
    POST_USER_PAYMENT_INVALID_VALID_YEAR(false, 2039, "올바른 형식의 유효기간(YY)을 입력해주세요."),
    POST_USER_PAYMENT_EMPTY_VALID_MONTH(false, 2040, "유효기간(MM)을 입력해주세요."),
    POST_USER_PAYMENT_INVALID_VALID_MONTH(false, 2041, "올바른 형식의 유효기간(YY)을 입력해주세요."),
    POST_USER_PAYMENT_EMPTY_CARD_NAME(false, 2042, "카드 종류를 입력해주세요."),
    POST_ACCOUNTS_EXISTS_ACCOUNT_NUM(false, 2043, "이미 등록된 계좌 번호입니다."),
    POST_USER_ACCOUNT_NOT_NUMBER(false, 2044, "숫자만 입력해 주세요."),
    POST_USER_ACCOUNT_EMPTY_NUMBER(false, 2045, "계좌 번호를 입력해주세요."),
    POST_USER_ACCOUNT_INVALID_NUMBER(false, 2046, "유효하지 않은 계좌 번호입니다."),
    //[POST] /users/addresses
    EMPTY_DETAIL_ADDRESS(false, 2047, "상세 주소를 입력해주세요."),

    //[PATCH] /reviews
    PATCH_REVIEW_EMPTY_REVIEW(false, 2048, "별점 평가를 진행해주세요."),
    PATCH_REVIEW_EMPTY_CONTENT(false, 2049, "평가 내용을 입력해주세요."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),

    // [POST] /users/sign-in/jwt
    NOT_EXIST_USER_ID_BY_JWT(false, 3015, "존재하지 않는 user id의 jwt입니다."),
    NOT_EXIST_SELECTED_USER_ADDRESS(false, 3016, "현재 유저의 선택된 주소가 존재하지 않습니다."),

    // [DELETE] /cart/{cartId}
    // [PATCH] /cart/{cartId}/menus
    NO_UPDATED_CART_INFO(false, 3017, "변경된 카트 정보가 없습니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userId}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    //[PATCH] /reviews/{reviewId}/{userId}
    MODIFY_FAIL_REVIEW(false, 4015, "리뷰 수정 실패"),
    DELETE_FAIL_REVIEW(false, 4016, "리뷰 삭제 실패"),
    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");



    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
