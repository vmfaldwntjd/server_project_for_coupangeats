package com.example.demo.src.review;


import com.example.demo.config.BaseException;
import com.example.demo.src.review.model.PatchReviewReq;
import com.example.demo.src.review.model.PostReviewReq;
import com.example.demo.src.review.model.PostReviewRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ReviewService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReviewDao reviewDao;
    private final ReviewProvider reviewProvider;
    private final JwtService jwtService;


    @Autowired
    public ReviewService(ReviewDao reviewDao, ReviewProvider reviewProvider, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.reviewProvider = reviewProvider;
        this.jwtService = jwtService;
    }

    //리뷰 생성 메소드
    public PostReviewRes createReview(int userId, int restaurantId, PostReviewReq postReviewReq) throws BaseException {
        try {
            int reviewId = reviewDao.createReview(userId, restaurantId, postReviewReq);
            return new PostReviewRes(reviewId);

        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //리뷰 수정 메소드
    public void patchReview(int reviewId, int userId, PatchReviewReq patchReviewReq) throws BaseException {
        try {
            int result = reviewDao.patchReview(reviewId, userId, patchReviewReq); // 해당 과정이 무사히 수행되면 True(1), 그렇지 않으면 False(0)입니다.
            if (result == 0) { // result값이 0이면 과정이 실패한 것이므로 에러 메서지를 보냅니다.
                throw new BaseException(MODIFY_FAIL_REVIEW);
            }
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //리뷰 삭제 메소드
    public boolean deleteReview(int reviewId, int userId) throws BaseException {
        try {
            boolean deleteReviewRes = reviewDao.deleteReview(reviewId, userId);
            if (deleteReviewRes == false) {
                throw new BaseException(DELETE_FAIL_REVIEW);
            }
            return deleteReviewRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
