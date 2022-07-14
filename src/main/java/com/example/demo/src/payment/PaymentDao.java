package com.example.demo.src.payment;

import com.example.demo.src.payment.model.GetUserPaymentRes;
import com.example.demo.src.payment.model.GetUserSelectAccountRes;
import com.example.demo.src.payment.model.PostUserAccountReq;
import com.example.demo.src.payment.model.PostUserPaymentReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PaymentDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //결제 관리 화면 출력
    public GetUserPaymentRes getUserPayment(int userId) {
        String getUserPaymentQuery = "Select payment.coopay_id,\n" +
                "       ifnull(balance,'계좌 등록하기') as balance,\n" +
                "       payment.card_id,\n" +
                "       card_image_url,\n" +
                "       card_name,\n" +
                "       card_num,\n" +
                "       payment.account_id,\n" +
                "       account_image_url,\n" +
                "       account_name,\n" +
                "       account_number,\n" +
                "       case\n" +
                "           when cash_receipt.status = 0 then '미신청'\n" +
                "           else '신청'\n" +
                "       end as cash_receipt_status\n" +
                "       from payment\n" +
                "left join coupangeats_dev.account on payment.account_id = account.account_id\n" +
                "left join coupangeats_dev.card on payment.card_id = card.card_id\n" +
                "left join coopay c on account.account_id = c.account_id\n" +
                "left join card_image ci on card.card_image_id = ci.card_image_id\n" +
                "left join account_image ai on account.account_image_id = ai.account_image_id\n" +
                "left join cash_receipt on cash_receipt.user_id = payment.user_id\n" +
                "where payment.user_id = ?;";
        int getUserPaymentParams = userId;
        return this.jdbcTemplate.queryForObject(getUserPaymentQuery,
                (rs, rowNum) -> new GetUserPaymentRes(
                        rs.getInt("coopay_id"),
                        rs.getString("balance"),
                        rs.getInt("card_id"),
                        rs.getString("card_image_url"),
                        rs.getString("card_name"),
                        rs.getString("card_num"),
                        rs.getInt("account_id"),
                        rs.getString("account_image_url"),
                        rs.getString("account_name"),
                        rs.getString("account_number"),
                        rs.getString("cash_receipt_status")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getUserPaymentParams); // 해당 닉네임을 갖는 모든 User 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }


    //결제 관리에서 카드 추가 메소드
    public int createUserPayment(int userId, PostUserPaymentReq postUserPaymentReq){
        String createUserPaymentQuery = "INSERT INTO coupangeats_dev.card (card_num, user_id, valid_thru_month, valid_thru_year, cvc, password, card_name) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] createUserPaymentParams = new Object[]{postUserPaymentReq.getCardNum(), userId, postUserPaymentReq.getValidThruMonth(), postUserPaymentReq.getValidThruYear(),
                postUserPaymentReq.getCvc(),postUserPaymentReq.getPassword(), postUserPaymentReq.getCardName()};
        this.jdbcTemplate.update(createUserPaymentQuery, createUserPaymentParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    //결제 관리에서 카드 삭제 메소드
    public boolean deleteUserPayment(int userId, int cardId) {
        String deleteUserPaymentQuery = "delete from card where user_id = ? and card_id = ?;";
        Object[] args = new Object[] {userId, cardId};

        return jdbcTemplate.update(deleteUserPaymentQuery, args) == 1;
    }

    //카드 번호 중복 여부 체크
    public int checkCardNum(String cardNum){
        String checkCardNumQuery = "select exists(select card_num from card where card_num = ?)";
        String checkCardNumParams = cardNum;
        return this.jdbcTemplate.queryForObject(checkCardNumQuery,
                int.class,
                checkCardNumParams);
    }

    //계좌 등록 과정 중 은행 선택 화면 관련 메소드
    public List<GetUserSelectAccountRes> getUserSelectAccount() {
        String getUserSelectAccountQuery = "select account_image_url, account_image_name from account_image;";
        return this.jdbcTemplate.query(getUserSelectAccountQuery,
                (rs, rowNum) -> new GetUserSelectAccountRes(
                        rs.getString("account_image_url"),
                        rs.getString("account_image_name")) // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                ); // 해당 닉네임을 갖는 모든 User 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    //계좌 번호 입력 화면
    public int createUserAccount(int userId, PostUserAccountReq postUserAccountReq){
        String createUserAccountQuery = "INSERT INTO coupangeats_dev.account (account_number, user_id) VALUES (?, ?)";
        Object[] createUserPaymentParams = new Object[]{postUserAccountReq.getAccountNumber(), userId};
        this.jdbcTemplate.update(createUserAccountQuery, createUserPaymentParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    //계좌 번호 중복 여부 체크
    public int checkAccountNum(String accountNum){
        String checkCardNumQuery = "select exists(select account_number from account where account_number = ?)";
        String checkCardNumParams = accountNum;
        return this.jdbcTemplate.queryForObject(checkCardNumQuery,
                int.class,
                checkCardNumParams);
    }
}
