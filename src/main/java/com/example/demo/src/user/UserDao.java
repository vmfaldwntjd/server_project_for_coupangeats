package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
//import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

//    public List<GetUserRes> getUsers(){
//        String getUsersQuery = "select * from UserInfo";
//        return this.jdbcTemplate.query(getUsersQuery,
//                (rs,rowNum) -> new GetUserRes(
//                        rs.getInt("userIdx"),
//                        rs.getString("userName"),
//                        rs.getString("ID"),
//                        rs.getString("Email"),
//                        rs.getString("password"))
//                );
//    }
//
//    public List<GetUserRes> getUsersByEmail(String email){
//        String getUsersByEmailQuery = "select * from UserInfo where email =?";
//        String getUsersByEmailParams = email;
//        return this.jdbcTemplate.query(getUsersByEmailQuery,
//                (rs, rowNum) -> new GetUserRes(
//                        rs.getInt("userIdx"),
//                        rs.getString("userName"),
//                        rs.getString("ID"),
//                        rs.getString("Email"),
//                        rs.getString("password")),
//                getUsersByEmailParams);
//    }
//
//    public GetUserRes getUser(int userIdx){
//        String getUserQuery = "select * from UserInfo where userIdx = ?";
//        int getUserParams = userIdx;
//        return this.jdbcTemplate.queryForObject(getUserQuery,
//                (rs, rowNum) -> new GetUserRes(
//                        rs.getInt("userIdx"),
//                        rs.getString("userName"),
//                        rs.getString("ID"),
//                        rs.getString("Email"),
//                        rs.getString("password")),
//                getUserParams);
//    }

    public GetPhoneUserRes getUserEmailByPhone(String phone){
        String getUserEmailByPhoneQuery = "select email from user where phone = ?";
        return this.jdbcTemplate.queryForObject(getUserEmailByPhoneQuery,
                (rs, rowNum) -> new GetPhoneUserRes(
                        true,
                        rs.getString("email")),
                phone);
    }

    

    public int createUser(PostSignUpReq postSignUpReq){
        String createUserQuery = "insert into user (email, name, phone, password) VALUES (?,?,?,?)";
        Object[] createUserParams = new Object[]{postSignUpReq.getEmail(), postSignUpReq.getName(), postSignUpReq.getPhone(), postSignUpReq.getPassword()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int setTokens(int userId, String jwt, String refreshJwt){
        String setUserTokensQuery = "update user set access_token = ?, refresh_token = ? where user_id = ?";
        Object[] setUserTokensParams = new Object[]{jwt, refreshJwt, userId};
        return this.jdbcTemplate.update(setUserTokensQuery, setUserTokensParams);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from user where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }

    public int checkPhone(String phone){
        String checkPhoneQuery = "select exists(select phone from user where phone = ?)";
        String checkPhoneParams = phone;
        return this.jdbcTemplate.queryForObject(checkPhoneQuery,
                int.class,
                checkPhoneParams);
    }

    public String getUserAccessToken(int userId){
        String getUserAccessTokenQuery = "select access_token from user where user_id = ?";
        int getUserAccessTokenParam = userId;
        return this.jdbcTemplate.queryForObject(getUserAccessTokenQuery,
                String.class,
                getUserAccessTokenParam);
    }
    public String getUserRefreshToken(int userId){
        String getUserRefreshTokenQuery = "select refresh_token from user where user_id = ?";
        int getUserRefreshTokenParam = userId;
        return this.jdbcTemplate.queryForObject(getUserRefreshTokenQuery,
                String.class,
                getUserRefreshTokenParam);
    }


    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update UserInfo set userName = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public User getUser(PostSignInReq postSignInReq){
        String getUserQuery = "select user_id, name, email, password, status, phone from user where email = ?";
        String getUserParam = postSignInReq.getEmail();

        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("status"),
                        rs.getString("phone")
                ),
                getUserParam
                );
    }

    public int checkId(int userId){
        String checkIdQuery = "select exists(select user_id from user where user_id = ?)";
        int checkIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkIdQuery,
                int.class,
                checkIdParams);
    }

    public GetUserAddressRes getUserAddress(int userId, Boolean isSelected){

            String getUserAddressQuery = "select user_id, address_name from user_address where user_id = ? AND is_selected = ?";

            Object[] getUserAddressParams = new Object[]{userId, isSelected};
            return this.jdbcTemplate.queryForObject(getUserAddressQuery,
                    (rs, rowNum) -> new GetUserAddressRes(
                            rs.getInt("user_id"),
                            rs.getString("address_name")
                    ),
                    getUserAddressParams);

    }

    public int getUserAddressId(int userId){
        try {
            String getUserAddressIdQuery = "select user_address_id from user_address where user_id = ? AND is_selected = 1";
            return this.jdbcTemplate.queryForObject(getUserAddressIdQuery,
                    int.class,
                    userId);
        } catch (IncorrectResultSetColumnCountException exception){
            return -1;
        } catch (EmptyResultDataAccessException exception){ // userId에 대해 결과 없음
            return -1;
        }
    }

    //core 추가
    public List<GetOrderRes> getOrdersByDeliveryStatus(int user_id, int delivery_status) {
        String getOrderQuery = "select `order`.order_id,\n" +
                "       `order`.restaurant_id,\n" +
                "       `order`.user_id,\n" +
                "       restaurant_name,\n" +
                "       case\n" +
                "           when delivery_status = 1 then '주문 수락됨'\n" +
                "           when delivery_status = 2 then '메뉴 준비중'\n" +
                "           when delivery_status = 3 then '배달중'\n" +
                "           when delivery_status = 4 then '배달 완료'\n" +
                "        end as delivery_status,\n" +
                "       order_total_price,\n" +
                "       url from `order`\n" +
                "left join res_image on res_image.restaurant_id = `order`.restaurant_id\n" +
                "left join restaurant on restaurant.restaurant_id = `order`.restaurant_id\n" +
                "where `order`.user_id = ? and delivery_status = ? and res_image.image_id = 1;\n"; // 해당 이메일을 만족하는 유저를 조회하는 쿼리문
        Object[] getOrderParams = new Object[]{user_id, delivery_status};
        List<GetOrderRes> getOrderResList = this.jdbcTemplate.query(getOrderQuery,
                (rs, rowNum) -> new GetOrderRes(
                        rs.getString("order_id"),
                        rs.getInt("restaurant_id"),
                        rs.getInt("user_id"),
                        rs.getString("restaurant_name"),
                        null,
                        rs.getString("delivery_status"),
                        rs.getInt("order_total_price"),
                        rs.getString("url")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getOrderParams); // 해당 닉네임을 갖는 모든 User 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
        for (GetOrderRes getOrderRes : getOrderResList) {
            String getMenuQuery = "select name as menu_name from res_menu\n" +
                    "where restaurant_id = ?;\n";
            int restaurantId = getOrderRes.getRestaurantId();
            List<String> menuNameList = this.jdbcTemplate.query(getMenuQuery,
                    (rs, rowNum) -> new String(
                            rs.getString("menu_name")
                    ), restaurantId);
            getOrderRes.setName(menuNameList);
        }
        return getOrderResList;
    }

    //core 추가
    public List<GetReceiptRes> getReceipts(int userId, String orderId) {
        String getReceiptsQuery = "select receipt_id, receipt.order_id, res_menu.name, price, pay_info, restaurant_name, discount_fee, pay_by, delivery_fee, order_price, delivery_address, order.order_total_price, user.user_id from receipt\n" +
                "inner join `order` on `order`.order_id = receipt.order_id\n" +
                "inner join user on receipt.user_id = user.user_id\n" +
                "inner join restaurant on `order`.restaurant_id = restaurant.restaurant_id\n" +
                "inner join res_menu on restaurant.restaurant_id = res_menu.restaurant_id\n" +
                "where (user.user_id = ? and `order`.order_id = ?);";
        Object[] getReceiptParams = new Object[]{userId, orderId};
        return this.jdbcTemplate.query(getReceiptsQuery,
                (rs, rowNum) -> new GetReceiptRes(
                        rs.getInt("receipt_id"),
                        rs.getString("order_id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("pay_info"),
                        rs.getString("restaurant_name"),
                        rs.getInt("discount_fee"),
                        rs.getString("pay_by"),
                        rs.getInt("delivery_fee"),
                        rs.getInt("order_price"),
                        rs.getString("delivery_address"),
                        rs.getInt("order_total_price"),
                        rs.getInt("user_id")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getReceiptParams); // 해당 닉네임을 갖는 모든 User 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    //core 추가
    public GetUserAddressDetailRes getUserAddressDetail(int userId, int userAddressId) {
        String getAddressDetailQuery = "select user_address_id, detail_address, doro_name_address, way_guide, kind, address_alias, address_name from user_address\n" +
                "where user_id = ? and user_address_id = ?;";
        Object[] getAddressDetailParams = new Object[]{userId, userAddressId};
        return this.jdbcTemplate.queryForObject(getAddressDetailQuery,
                (rs, rowNum) -> new GetUserAddressDetailRes(
                        rs.getInt("user_address_id"),
                        rs.getString("detail_address"),
                        rs.getString("doro_name_address"),
                        rs.getString("way_guide"),
                        rs.getInt("kind"),
                        rs.getString("address_alias"),
                        rs.getString("address_name")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getAddressDetailParams); // 해당 닉네임을 갖는 모든 User 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    //core추가
    public List<GetUserAddressInformationRes> getUserAddressInformation(int userId) {
        String getUserAddressInformationQuery = "select user_address_id, kind, detail_address, address_name, doro_name_address from user_address\n" +
                "where user_id = ?;";
        int getUserInformationParams = userId;
        return this.jdbcTemplate.query(getUserAddressInformationQuery,
                (rs, rowNum) -> new GetUserAddressInformationRes(
                        rs.getInt("user_address_id"),
                        rs.getInt("kind"),
                        rs.getString("detail_address"),
                        rs.getString("address_name"),
                        rs.getString("doro_name_address")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getUserInformationParams); // 해당 닉네임을 갖는 모든 User 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    //core 추가
    public int modifyUserAddressDetail(int userId, int userAddressId, PatchUserAddressDetailReq patchUserAddressDetailReq) {
        String modifyUserAddressDetailQuery = "update user_address set detail_address = ?, way_guide = ?, kind = ?, address_alias = ? where (user_id = ? and user_address_id = ?);"; // 해당 userIdx를 만족하는 User를 해당 nickname으로 변경한다.
        Object[] modifyUserAddressDetailParams = new Object[]{patchUserAddressDetailReq.getDetailAddress(), patchUserAddressDetailReq.getWayGuide(), patchUserAddressDetailReq.getKind()
        , patchUserAddressDetailReq.getAddressAlias(), userId, userAddressId}; // 주입될 값들(nickname, userIdx) 순

        return this.jdbcTemplate.update(modifyUserAddressDetailQuery, modifyUserAddressDetailParams); // 대응시켜 매핑시켜 쿼리 요청(생성했으면 1, 실패했으면 0)
    }

    //core 추가
    public int createUserAddress(int userId, PostUserAddressReq postUserAddressReq){
        String createUserAddressQuery = "insert into user_address (user_id, address_name, doro_name_address, detail_address, way_guide, address_alias, kind) VALUES (?,?,?,?,?,?,?)";
        Object[] createUserAddressParams = new Object[]{userId, postUserAddressReq.getAddressName(), postUserAddressReq.getDoroNameAddress(), postUserAddressReq.getDetailAddress(),
                postUserAddressReq.getWayGuide(),postUserAddressReq.getAddressAlias(), postUserAddressReq.getKind()};
        this.jdbcTemplate.update(createUserAddressQuery, createUserAddressParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    //core추가
    public List<GetUserFavoriteRes> getUserFavorite(int userId, Double longitude, Double latitude) {
        if(longitude == null || latitude == null) {
            latitude = 37.4638409;
            longitude = 126.9526383;
        }
        String getUserFavoriteQuery = "select favorite_id,\n" +
                "       favorite.restaurant_id,\n" +
                "       image_id,\n" +
                "       restaurant_name,\n" +
                "       is_cheetah,\n" +
                "       round(avg(star_point), 1) as star_point,\n" +
                "       delivery_time,\n" +
                "       delivery_fee,\n" +
                "       round((6371*acos(cos(radians(37.4638409))*cos(radians(restaurant.latitude))*cos(radians(restaurant.longitude) -\n" +
                "                                                                                 radians(126.9526383))+sin(radians(37.4638409))*sin(radians(restaurant.latitude)))), 1)\n" +
                "           AS distance,\n" +
                "       is_packable from favorite\n" +
                "           inner join restaurant on favorite.restaurant_id = restaurant.restaurant_id\n" +
                "           inner join res_image on restaurant.restaurant_id = res_image.restaurant_id\n" +
                "           inner join review on restaurant.restaurant_id = review.restaurant_id\n" +
                "           inner join res_delivery_fee on restaurant.restaurant_id = res_delivery_fee.restaurant_id\n" +
                "                   where favorite.user_id = ? and res_image.image_id = 1\n" +
                "                   group by review.restaurant_id;\n";
        int getUserFavoriteParams = userId;
        return this.jdbcTemplate.query(getUserFavoriteQuery,
                (rs, rowNum) -> new GetUserFavoriteRes(
                        rs.getInt("favorite_id"),
                        rs.getInt("restaurant_id"),
                        rs.getInt("image_id"),
                        rs.getString("restaurant_name"),
                        rs.getInt("is_cheetah"),
                        rs.getInt("star_point"),
                        rs.getInt("delivery_time"),
                        rs.getInt("delivery_fee"),
                        rs.getDouble("distance"),
                        rs.getInt("is_packable")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getUserFavoriteParams); // 해당 닉네임을 갖는 모든 User 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    //core추가
    public boolean deleteUserFavorite(int userId, int restaurantId) {
        String deleteUserQuery = "delete from favorite where user_id = ? and restaurant_id = ?";
        Object[] args = new Object[] {userId, restaurantId};

        return jdbcTemplate.update(deleteUserQuery, args) == 1;
    }

    //core 추가
    public int createUserFavorite(int userId, int restaurantId){
        String createUserFavoriteQuery = "INSERT INTO favorite (restaurant_id, user_id) VALUES (?, ?);";
        Object[] createUserFavoriteParams = new Object[]{restaurantId, userId};
        this.jdbcTemplate.update(createUserFavoriteQuery, createUserFavoriteParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    //core추가 -> cart도메인에 필요한 메소드
    public GetUserAddressCartRes getUserAddressInfo(int userId){
        String createUserAddressCartQuery = "select user_address.user_address_id, address_name, doro_name_address, latitude, longitude from user_address\n" +
                "inner join cart on user_address.user_address_id = cart.user_address_id\n" +
                "where is_selected = 1 and user_address.user_id = ?;";
        int getUserAddressCartParams = userId;
//        this.jdbcTemplate.update(createUserAddressCartQuery, getUserAddressCartParams);

        return this.jdbcTemplate.queryForObject(createUserAddressCartQuery,
                (rs, rowNum) -> new GetUserAddressCartRes(
                        rs.getInt("user_address_id"),
                        rs.getString("address_name"),
                        rs.getString("doro_name_address"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getUserAddressCartParams);
    }
}
