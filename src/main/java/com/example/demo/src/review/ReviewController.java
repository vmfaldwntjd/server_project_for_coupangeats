package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/reviews")
public class ReviewController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final ReviewProvider reviewProvider;

    @Autowired
    private final ReviewService reviewService;

    public ReviewController(JwtService jwtService, ReviewProvider reviewProvider, ReviewService reviewService) {
        this.jwtService = jwtService;
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
    }

    //리뷰 생성 관련 메소드
    @ResponseBody
    @PostMapping("/{userId}/{restaurantId}")
    public BaseResponse<PostReviewRes> createReview(@PathVariable("userId") int userId, @PathVariable("restaurantId") int restaurantId, @RequestBody PostReviewReq postReviewReq) {
        try {
            int userIdByJwt = jwtService.getUserId();
            if (userId != userIdByJwt) {
                return new BaseResponse(INVALID_USER_JWT);
            }
            PostReviewRes postReviewRes = reviewService.createReview(userId, restaurantId, postReviewReq);
            return new BaseResponse<>(postReviewRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //리뷰 수정 관련 메소드
    @ResponseBody
    @PatchMapping("/{reviewId}/{userId}")
    public BaseResponse<String> patchReview(@PathVariable("reviewId") int reviewId, @PathVariable("userId") int userId, @RequestBody Review review) {
        if (review.getStarPoint() == null) {
            return new BaseResponse<>(PATCH_REVIEW_EMPTY_REVIEW);
        }
        if (review.getContent() == null) {
            return new BaseResponse<>(PATCH_REVIEW_EMPTY_CONTENT);
        }
        try {
            //jwt에서 idx 추출.
            int userIdByJwt = jwtService.getUserId();
            //userIdx와 접근한 유저가 같은지 확인
            if (userId != userIdByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PatchReviewReq patchReviewReq = new PatchReviewReq(review.getStarPoint(), review.getContent());
            reviewService.patchReview(reviewId, userId, patchReviewReq);

            String result = "리뷰정보가 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //리뷰 관리에서 내가 작성한 리뷰 삭제 메소드
    @ResponseBody
    @DeleteMapping("/{reviewId}/{userId}")
    public BaseResponse<Boolean> deleteReview(@PathVariable("reviewId") int reviewId, @PathVariable("userId") int userId) {
        try {
            boolean deleteReviewRes = reviewService.deleteReview(reviewId, userId);
            return new BaseResponse<>(deleteReviewRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //내가 작성한 리뷰 하나 조회 메소드
    @ResponseBody
    @GetMapping("/{reviewId}/{userId}")
    public BaseResponse<GetReviewRes> getUserReview(@PathVariable("reviewId") int reviewId, @PathVariable("userId") int userId) {
        try {
            int userIdByJwt = jwtService.getUserId();
            if (userId != userIdByJwt) {
                return new BaseResponse(INVALID_USER_JWT);
            }
            GetReviewRes getReviewRes = reviewProvider.getUserReview(reviewId, userId);
            return new BaseResponse<>(getReviewRes);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //특정 레스토랑에 대한 리뷰 목록 조회 메소드
    @ResponseBody
    @GetMapping("/{restaurantId}")
    public BaseResponse<List<GetAllReviewRes>> getReview(@PathVariable("restaurantId") int restaurantId) {
        try {
            List<GetAllReviewRes> getAllReviewRes = reviewProvider.getReview(restaurantId);
            return new BaseResponse<>(getAllReviewRes);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //특정 레스토랑 리뷰 목록 조회 고친 작업
//    @ResponseBody
//    @GetMapping("/{restaurantId}")
//    public BaseResponse<GetResReviewListRes> getResReview(@PathVariable("restaurantId") int restaurantId) {
//        try {
//            GetResReviewListRes getResReviewListRes = reviewProvider.getResReview(restaurantId);
//            return new BaseResponse<>(getResReviewListRes);
//
//        } catch (BaseException exception) {
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }
}
