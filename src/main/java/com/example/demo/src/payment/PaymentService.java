package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.src.payment.model.PostUserAccountReq;
import com.example.demo.src.payment.model.PostUserAccountRes;
import com.example.demo.src.payment.model.PostUserPaymentReq;
import com.example.demo.src.payment.model.PostUserPaymentRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class PaymentService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PaymentDao paymentDao;
    private final PaymentProvider paymentProvider;
    private final JwtService jwtService;


    @Autowired
    public PaymentService(PaymentDao paymentDao, PaymentProvider paymentProvider, JwtService jwtService) {
        this.paymentDao = paymentDao;
        this.paymentProvider = paymentProvider;
        this.jwtService = jwtService;
    }

    //유저 결제 관리에서 카드 추가 하기
    public PostUserPaymentRes createUserPayment(int userId, PostUserPaymentReq postUserPaymentReq) throws BaseException {

        //중복 체크
        if (paymentProvider.checkCardNum(postUserPaymentReq.getCardNum()) == 1) {
            throw new BaseException(POST_CARDS_EXISTS_CARD_NUM);
        }
        try {
            int cardId = paymentDao.createUserPayment(userId, postUserPaymentReq);
            return new PostUserPaymentRes(cardId);

        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //유저 결제 관리에서 카드 삭제 하기
    public boolean deleteUserPayment(int userId, int cardId) throws BaseException {
        try {
            boolean deleteUserPaymentRes = paymentDao.deleteUserPayment(userId, cardId);
            return deleteUserPaymentRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //계좌 등록하기
    public PostUserAccountRes createUserAccount(int userId, PostUserAccountReq postUserAccountReq) throws BaseException {

        //중복 체크
        if (paymentProvider.checkAccountNum(postUserAccountReq.getAccountNumber()) == 1) {
            throw new BaseException(POST_ACCOUNTS_EXISTS_ACCOUNT_NUM);
        }

        try {
            int accountId = paymentDao.createUserAccount(userId, postUserAccountReq);
            return new PostUserAccountRes(accountId);

        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
