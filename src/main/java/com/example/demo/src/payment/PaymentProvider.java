package com.example.demo.src.payment;


import com.example.demo.config.BaseException;
import com.example.demo.src.payment.model.GetUserPaymentRes;
import com.example.demo.src.payment.model.GetUserSelectAccountRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class PaymentProvider {
    private final PaymentDao paymentDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PaymentProvider(PaymentDao paymentDao, JwtService jwtService) {
        this.paymentDao = paymentDao;
        this.jwtService = jwtService;
    }

    //결제 정보 관리 화면 메소드
    public GetUserPaymentRes getUserPayment(int userId) throws BaseException {
        try {
            GetUserPaymentRes getUserPaymentRes = paymentDao.getUserPayment(userId);
            return getUserPaymentRes;
        } catch (Exception exception) {
            System.out.println(exception); //오류 내용 확인용
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //카드 존재 여부 체크
    public int checkCardNum(String cardNum) throws BaseException{
        try{
            return paymentDao.checkCardNum(cardNum);
        } catch (Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //계좌 등록 과정 중 은행 선택 화면
    public List<GetUserSelectAccountRes> getUserSelectAccount() throws BaseException {
        try {
            List<GetUserSelectAccountRes> getUserSelectAccountRes = paymentDao.getUserSelectAccount();
            return getUserSelectAccountRes;
        } catch (Exception exception) {
            System.out.println(exception); //오류 내용 확인용
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //계좌 번호 존재 여부 체크
    public int checkAccountNum(String accountNum) throws BaseException{
        try{
            return paymentDao.checkAccountNum(accountNum);
        } catch (Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
