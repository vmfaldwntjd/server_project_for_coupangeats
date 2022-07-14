package com.example.demo.src.coupon;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.coupon.model.*;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.src.review.model.PatchReviewReq;
import com.example.demo.src.review.model.Review;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app/coupons")
public class CouponController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final CouponProvider couponProvider;

    @Autowired
    private final CouponService couponService;

    public CouponController(JwtService jwtService, CouponProvider couponProvider, CouponService couponService) {
        this.jwtService = jwtService;
        this.couponProvider = couponProvider;
        this.couponService = couponService;
    }

    //쿠폰 생성 메소드
    @ResponseBody
    @PostMapping("/{userId}")
    public BaseResponse<PostCouponRes> createCoupon(@PathVariable("userId") int userId, @RequestBody PostCouponReq postCouponReq) {
        try {
            int userIdByJwt = jwtService.getUserId();
            if (userId != userIdByJwt) {
                return new BaseResponse(INVALID_USER_JWT);
            }
            PostCouponRes postCouponRes = couponService.createCoupon(userId, postCouponReq);
            return new BaseResponse<>(postCouponRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //쿠폰 조회 메소드
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<List<GetCouponRes>> getUserCoupon(@PathVariable("userId") int userId) {
        try {
            int userIdByJwt = jwtService.getUserId();
            if (userId != userIdByJwt) {
                return new BaseResponse(INVALID_USER_JWT);
            }
            List<GetCouponRes> getCouponRes = couponProvider.getUserCoupon(userId);
            return new BaseResponse<>(getCouponRes);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //쿠폰 사용 메소드
    @ResponseBody
    @PatchMapping("/{couponId}/{userId}")
    public BaseResponse<String> useCoupon(@PathVariable("couponId") int couponId, @PathVariable("userId") int userId) {
        try {
            //jwt에서 idx 추출.
            int userIdByJwt = jwtService.getUserId();
            //userIdx와 접근한 유저가 같은지 확인
            if (userId != userIdByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PatchCouponReq patchCouponReq = new PatchCouponReq(couponId, userId);
            couponService.useCoupon(couponId, userId);

            String result = "쿠폰이 적용되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
