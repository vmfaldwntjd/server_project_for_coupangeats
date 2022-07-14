package com.example.demo.src.payment;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.payment.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import static com.example.demo.utils.ValidationRegex.*;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/payments")
public class PaymentController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final PaymentProvider paymentProvider;

    @Autowired
    private final PaymentService paymentService;

    public PaymentController(JwtService jwtService, PaymentProvider paymentProvider, PaymentService paymentService) {
        this.jwtService = jwtService;
        this.paymentProvider = paymentProvider;
        this.paymentService = paymentService;
    }

    //결제 정보 관리 화면 메소드
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<GetUserPaymentRes> getUserPayment(@PathVariable("userId") int userId) {
        try {
            int userIdByJwt = jwtService.getUserId();
            if (userId != userIdByJwt) {
                return new BaseResponse(INVALID_USER_JWT);
            }
            GetUserPaymentRes getUserPaymentRes = paymentProvider.getUserPayment(userId);
            return new BaseResponse<>(getUserPaymentRes);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //결제 관리에서 카드 추가하기 메소드
    @ResponseBody
    @PostMapping("/{userId}")
    public BaseResponse<PostUserPaymentRes> createUserPayment(@PathVariable("userId") int userId, @RequestBody PostUserPaymentReq postUserPaymentReq) {
        //카드 번호 입력이 되어 있지 않은 경우
        if (postUserPaymentReq.getCardNum() == null) {
            return new BaseResponse<>(POST_USER_PAYMENT_EMPTY_CARD_NUM);
        }
        //올바른 카드 번호 입력 방식인지 확인하기
        if (!validCardNumber(postUserPaymentReq.getCardNum())) {
            return new BaseResponse<>(POST_USER_PAYMENT_INVALID_CARD_NUM);
        }
        //패스워드가 입력이 안되어 있는 경우
        if (postUserPaymentReq.getPassword() == null) {
            return new BaseResponse<>(POST_USER_PAYMENT_EMPTY_PASSWORD);
        }
        //cvc 번호가 입력이 안되어 있는 경우
        if (postUserPaymentReq.getCvc() == null) {
            return new BaseResponse<>(POST_USER_PAYMENT_EMPTY_CVC);
        }
        //cvc의 입력이 유요한지 체크하기
        if (!isValidCVCNumber(postUserPaymentReq.getCvc())) {
            return new BaseResponse<>(POST_USER_PAYMENT_INVALID_CVC);
        }
        //유효기간(YY)이 입력이 되어 있지 않은 경우
        if (postUserPaymentReq.getValidThruYear() == null) {
            return new BaseResponse<>(POST_USER_PAYMENT_EMPTY_VALID_YEAR);
        }
        //유효기간(YY)의 입력 형식이 올바르게 진행이 되어 있는 경우
        if (!isValidPeriod(postUserPaymentReq.getValidThruYear())){
            return new BaseResponse<>(POST_USER_PAYMENT_INVALID_VALID_YEAR);
        }
        //유효기간(MM)이 입력이 되어 있지 않은 경우
        if (postUserPaymentReq.getValidThruMonth() == null) {
            return new BaseResponse<>(POST_USER_PAYMENT_EMPTY_VALID_MONTH);
        }
        if (!isValidPeriod(postUserPaymentReq.getValidThruMonth())){
            return new BaseResponse<>(POST_USER_PAYMENT_INVALID_VALID_MONTH);
        }
        //카드 이름이 입력이 되어 있지 않은 경우
        if (postUserPaymentReq.getCardName() == null) {
            return new BaseResponse<>(POST_USER_PAYMENT_EMPTY_CARD_NAME);
        }

        try {
            int userIdByJwt = jwtService.getUserId();
            if (userId != userIdByJwt) {
                return new BaseResponse(INVALID_USER_JWT);
            }
            PostUserPaymentRes postUserPaymentRes = paymentService.createUserPayment(userId, postUserPaymentReq);
            return new BaseResponse<>(postUserPaymentRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //결제 관리에서 카드 삭제하기 메소드
    @ResponseBody
    @DeleteMapping("/{userId}/{cardId}")
    public BaseResponse<Boolean> deleteUserPayment(@PathVariable("userId") int userId, @PathVariable("cardId") int cardId) {
        try {
            int userIdByJwt = jwtService.getUserId();
            if (userId != userIdByJwt) {
                return new BaseResponse(INVALID_USER_JWT);
            }
            boolean deleteUserPaymentRes = paymentService.deleteUserPayment(userId, cardId);
            return new BaseResponse<>(deleteUserPaymentRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //계좌 등록 과정 중 은행 선택 화면
    @ResponseBody
    @GetMapping("/accounts")
    public BaseResponse<List<GetUserSelectAccountRes>> getUserSelectAccount() {
        try {
            List<GetUserSelectAccountRes> getUserSelectAccountRes = paymentProvider.getUserSelectAccount();
            return new BaseResponse<>(getUserSelectAccountRes);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //계좌 등록 과정 중 계좌 번호 입력
    @ResponseBody
    @PostMapping("/{userId}/accounts")
    public BaseResponse<PostUserAccountRes> createUserAccount(@PathVariable("userId") int userId, @RequestBody PostUserAccountReq postUserAccountReq) {
        //계좌 번호 입력이 안 되어 있는 경우
        if (postUserAccountReq.getAccountNumber() == null) {
            return new BaseResponse<>(POST_USER_ACCOUNT_EMPTY_NUMBER);
        }

        //숫자로만 구성이 되어 있지 않은 경우
        if (!isOnlyNumber(postUserAccountReq.getAccountNumber())) {
            return new BaseResponse<>(POST_USER_ACCOUNT_NOT_NUMBER);
        }

        //계좌 번호가 자릿수가 11자리가 아닌 경우
        if (postUserAccountReq.getAccountNumber().length() != 11) {
            return new BaseResponse<>(POST_USER_ACCOUNT_INVALID_NUMBER);
        }
        try {
            int userIdByJwt = jwtService.getUserId();
            if (userId != userIdByJwt) {
                return new BaseResponse(INVALID_USER_JWT);
            }
            PostUserAccountRes postUserAccountRes = paymentService.createUserAccount(userId, postUserAccountReq);
            return new BaseResponse<>(postUserAccountRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}