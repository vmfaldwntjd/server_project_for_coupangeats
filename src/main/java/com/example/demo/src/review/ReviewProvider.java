package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.review.model.GetAllReviewRes;
import com.example.demo.src.review.model.GetResReviewListRes;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.src.user.model.GetUserAddressInformationRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ReviewProvider {
    private final ReviewDao reviewDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ReviewProvider(ReviewDao reviewDao, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.jwtService = jwtService;
    }

    //유저가 작성한 리뷰 하나 조회 메소드
    public GetReviewRes getUserReview(int reviewId, int userId) throws BaseException {
        try {
            GetReviewRes getReviewRes = reviewDao.getUserReview(reviewId, userId);
            return getReviewRes;
        } catch (Exception exception) {
            System.out.println(exception); //오류 내용 확인용
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //리뷰 목록 조회
    public List<GetAllReviewRes> getReview(int restaurantId) throws BaseException {
        try {
            List<GetAllReviewRes> getAllReviewRes = reviewDao.getReview(restaurantId);
            return getAllReviewRes;
        } catch (Exception exception) {
            System.out.println(exception); //오류 내용 확인용
            throw new BaseException(DATABASE_ERROR);
        }
    }

//    public GetResReviewListRes getResReview(int restaurantId) throws BaseException {
//        try {
//            GetResReviewListRes getResReviewListRes = reviewDao.getResReview(restaurantId);
//            return getResReviewListRes;
//        } catch (Exception exception) {
//            System.out.println(exception); //오류 내용 확인용
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
}
