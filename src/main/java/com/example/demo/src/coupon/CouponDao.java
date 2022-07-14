package com.example.demo.src.coupon;

import com.example.demo.src.coupon.model.GetCouponRes;
import com.example.demo.src.coupon.model.PostCouponReq;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.src.review.model.PatchReviewReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CouponDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    //쿠폰 생성 관련 메소드
    public int createCoupon(int userId, PostCouponReq postCouponReq){
        String createCouponQuery = "INSERT INTO coupon (coupon_num, user_id) VALUES (?, ?);";
        Object[] createCouponParams = new Object[]{postCouponReq.getCouponNum(), userId};
        this.jdbcTemplate.update(createCouponQuery, createCouponParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    //쿠폰 조회 관련 메소드
    public List<GetCouponRes> getUserCoupon(int userId) {
        String getUserCouponQuery = "select user_id, coupon.coupon_id, coupon_image_url from coupon\n" +
                "inner join coupon_image on coupon.coupon_id = coupon_image.coupon_id\n" +
                "where user_id = ?;";
        int userParams = userId;
        return this.jdbcTemplate.query(getUserCouponQuery,
                (rs, rowNum) -> new GetCouponRes(
                        rs.getInt("user_id"),
                        rs.getInt("coupon_id"),
                        rs.getString("coupon_image_url")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                userParams); // 해당 닉네임을 갖는 모든 User 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    //쿠폰 사용 관련 메소드
    public int useCoupon(int couponId, int userId) {
        String useCouponQuery = "UPDATE coupon t SET t.status = 0 WHERE t.coupon_id = ? AND t.user_id = ?;"; // 해당 userIdx를 만족하는 User를 해당 nickname으로 변경한다.
        Object[] useCouponParams = new Object[]{couponId, userId}; // 주입될 값들(nickname, userIdx) 순

        return this.jdbcTemplate.update(useCouponQuery, useCouponParams); // 대응시켜 매핑시켜 쿼리 요청(생성했으면 1, 실패했으면 0)
    }

    //쿠폰 중복 여부 체크
    public int checkCouponNum(String couponNum){
        String checkCouponNumQuery = "select exists(select coupon_num from coupon where coupon_num = ?)";
        String checkCouponNumParams = couponNum;
        return this.jdbcTemplate.queryForObject(checkCouponNumQuery,
                int.class,
                checkCouponNumParams);
    }

}
