package com.example.demo.src.coupon;

import com.example.demo.config.BaseException;
import com.example.demo.src.coupon.model.GetCouponRes;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class CouponProvider {
    private final CouponDao couponDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CouponProvider(CouponDao couponDao, JwtService jwtService) {
        this.couponDao = couponDao;
        this.jwtService = jwtService;
    }

    //쿠폰 조회 메소드
    public List<GetCouponRes> getUserCoupon(int userId) throws BaseException {
        try {
            List<GetCouponRes> getCouponRes = couponDao.getUserCoupon(userId);
            return getCouponRes;
        } catch (Exception exception) {
            System.out.println(exception); //오류 내용 확인용
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //쿠폰 번호 중복 여부 체크
    public int checkCouponNum(String couponNum) throws BaseException{
        try{
            return couponDao.checkCouponNum(couponNum);
        } catch (Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
