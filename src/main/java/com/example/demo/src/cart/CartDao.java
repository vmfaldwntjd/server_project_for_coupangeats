package com.example.demo.src.cart;

import com.example.demo.src.cart.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static com.example.demo.src.cart.query.CartQuery.createCartQuery;
import static com.example.demo.src.cart.query.CartQuery.getMenuListFromCartQuery;

@Repository
public class CartDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int getResIdByUserId(int userId) {
        try {
            String checkCartByUserIdQuery = "select restaurant_id from cart where user_id = ?";
            return this.jdbcTemplate.queryForObject(checkCartByUserIdQuery,
                    int.class,
                    userId);
        } catch (IncorrectResultSetColumnCountException exception){ //userId에 대해 여러개 조회
            return -1;
        } catch (EmptyResultDataAccessException exception){ // userId에 대해 결과 없음
            return -1;
        }
    }

    public int getCartIdByUserId(int userId){
        try {
            String checkCartByUserIdQuery = "select cart_id from cart where user_id = ?";
            return this.jdbcTemplate.queryForObject(checkCartByUserIdQuery,
                    int.class,
                    userId);
        } catch (IncorrectResultSetColumnCountException exception){//userId에 대해 여러개 조회
            return -1;
        } catch (EmptyResultDataAccessException exception){ // userId에 대해 결과 없음
            return -1;
        }
    }


    public int createCart(PostCartReq postCartReq, Integer userAddressId){

        Object[] creatCartParams = new Object[]{postCartReq.getUserId(), userAddressId, postCartReq.getRestaurantId()};
        this.jdbcTemplate.update(createCartQuery, creatCartParams);

        // 마지막으로 update/insert된 테이블의 PK값.
        return this.jdbcTemplate.queryForObject("select last_insert_id()", int.class);
    }

    // 카트 내에서 마지막 순서값 반환.
    public int getLastMenuOrder(int cartId){
        Integer lastMenuOrder = this.jdbcTemplate.queryForObject("SELECT MAX(menu_order) FROM cart_menu WHERE cart_id = ?", int.class, cartId);
        return lastMenuOrder == null ? 0 : lastMenuOrder;
    }

    public int createCartMenu(int cartId, int menuOrder, PostCartReq postCartReq){
        int sum = postCartReq.getMenuPrice(); // 현재 들어온 메뉴에 대한모든 값을 저장하고 반환합니다.

        // 매개변수값이 너무 많아서 확인차 이곳에 쿼리 작성.
        String createCartMenuQuery = "INSERT INTO cart_menu (cart_id, menu_id, price, " +
                "count, menu_name, parent_menu_id, menu_order)\n" +
                "VALUES(?, ?, ?, ?, ?, ?, ?);";
        Object[] createCartMenuParam = new Object[]{cartId, postCartReq.getMenuId(), postCartReq.getMenuPrice(),
        postCartReq.getMenuCount(), postCartReq.getMenuName(), postCartReq.getMenuId(), menuOrder};
        this.jdbcTemplate.update(createCartMenuQuery, createCartMenuParam);

        List<OptionKindInfo> optionKindInfoList = postCartReq.getOptionKindInfoList();
        for(OptionKindInfo oki : optionKindInfoList) {
            List<OptionInfo> optionInfoList = oki.getOptionInfoList();
            for(OptionInfo oi : optionInfoList){
                sum += oi.getOptionPrice();
                // 옵션은 수가 단 하나
                createCartMenuParam = new Object[]{cartId, oi.getOptionId(), oi.getOptionPrice(),
                        1, oi.getOptionName(), postCartReq.getMenuId(), menuOrder};
                this.jdbcTemplate.update(createCartMenuQuery, createCartMenuParam);
            }
        }
//        System.out.println("단위 메뉴당 합계 : "+sum);
//        System.out.println("단위 메뉴 개수 : "+postCartReq.getMenuCount());

       return sum*postCartReq.getMenuCount();
    }

    public int addTotalPrice(int cartId, int totalPrice){
        return this.jdbcTemplate.update("UPDATE cart SET order_price = order_price + ? WHERE cart_id = ?", totalPrice, cartId);
    }

    public GetOrderPriceRes getOrderPrice(int userId){
        return this.jdbcTemplate.queryForObject("SELECT cart_id, order_price FROM cart WHERE user_id = ?",
                (rs, rowNum) -> new GetOrderPriceRes(
                        rs.getInt("cart_id"),
                        rs.getInt("order_price")
                ), userId);
    }

    // 한 단위 메뉴에 해당되는 모든 옵션정보를 문자열로 전환한다.
    public String getOptionInfoString(int cartId, int menuId, int menuOrder){
        StringBuilder sb = new StringBuilder();
        List<OptionInfo> optionInfoList = this.jdbcTemplate.query("SELECT price as option_price, menu_name as option_name FROM cart_menu WHERE cart_id = ?" +
                        " AND parent_menu_id = ? AND menu_id != ? AND menu_order = ?;",
                (rs,rowNum) -> new OptionInfo(
                        -1, // 임시로 넣어두는 가짜값. 필요없음.
                        rs.getInt("option_price"),
                        rs.getString("option_name")
                ),
                new Object[]{cartId, menuId, menuId, menuOrder});
        for(OptionInfo optionInfo : optionInfoList){
            sb.append(optionInfo.getOptionName()).append(optionInfo.getOptionPrice() > 0 ? "(+"+optionInfo.getOptionPrice()+"원), " : "");
        }
        String optionInfo = sb.toString();

        // 마지막 쉼표 삭제
        optionInfo = optionInfo.substring(optionInfo.length() - 2).contentEquals(", ") ?
                optionInfo.substring(0, optionInfo.length() - 2) : optionInfo;

        return optionInfo;
    }

    // 현재 메뉴와 같은 메뉴인, 이미 들어와있는 단락 메뉴 리스트 정보를 가져온다.
    public List<Integer> getCartMenuOption(int cartId, int menuId){
        return this.jdbcTemplate.query("SELECT menu_order FROM cart_menu WHERE cart_id =? AND menu_id = ?",
                (rs, rowNum) -> rs.getInt("menu_order"), new Object[]{cartId, menuId});
    }

    public int increaseMenuCount(int cartId, int menuId, int menuCount){
        return this.jdbcTemplate.update("UPDATE cart_menu SET count = count + ? WHERE cart_id = ? AND menu_id = ?", menuCount, cartId, menuId);
    }


    public List<ResOrderMenuInfo> getResOrderMenuInfo(int cartId){
        List<ResOrderMenuInfo> resOrderMenuInfoList = this.jdbcTemplate.query(getMenuListFromCartQuery,
                (rs, rowNum) -> new ResOrderMenuInfo(
                        rs.getInt("menu_id"),
                        rs.getInt("menu_price"),
                        rs.getInt("menu_count"),
                        rs.getString("menu_name"),
                        rs.getInt("menu_order"),
                        null
                ), cartId);

        for(ResOrderMenuInfo resOrderMenuInfo : resOrderMenuInfoList){
            resOrderMenuInfo.setOptionInfo(getOptionInfoString(cartId, resOrderMenuInfo.getMenuId(), resOrderMenuInfo.getMenuOrder()));
            System.out.println("optionInfo Test : "+resOrderMenuInfo.getOptionInfo());
        }
        return resOrderMenuInfoList;
    }

    // 가게 요청사항 정보
    public RequestMessageInfo getRequestMessageInfo(int cartId) {
        return this.jdbcTemplate.queryForObject("SELECT res_message, is_need_disposable, del_message FROM cart WHERE cart_id = ?",
                (rs, rowNum) -> new RequestMessageInfo(
                        rs.getString("res_message"),
                        rs.getInt("is_need_disposable"),
                        rs.getString("del_message")
                ), cartId);
    }

    // 카트 할인 정보
    public DiscountInfo getDiscountInfo(int cartId) {
        return this.jdbcTemplate.queryForObject("SELECT coupon_discount,\n" +
                        "       cash_discount,\n" +
                        "       (coupon_discount + cart.cash_discount) * -1 as total_price\n" +
                        "FROM cart\n" +
                        "WHERE cart_id = ?",
                (rs, rowNum) -> new DiscountInfo(
                        0,
                        rs.getInt("coupon_discount"),
                        rs.getInt("cash_discount"),
                        rs.getInt("total_price") // 아직 총 결재금액이 아님! 아직은 할인 가격 합산!
                ), cartId);
    }

    public int deleteCart(int cartId){
        String deleteCartQuery = "DELETE FROM cart WHERE cart_id = ?";
        return this.jdbcTemplate.update(deleteCartQuery, cartId);
    }

    public int updateCart(int cartId, PatchCartMenuReq patchCartMenuReq){
        String updateCartQuery = "UPDATE cart_menu SET count = ? WHERE cart_id = ? AND menu_order = ? AND menu_id = ?;";
        Object[] updateCartParam = new Object[]{patchCartMenuReq.getMenuCount(), cartId, patchCartMenuReq.getMenuOrder(), patchCartMenuReq.getMenuId()};
        return this.jdbcTemplate.update(updateCartQuery, updateCartParam);
    }

    public int updateCartOrderPrice(int cartId, PatchCartMenuReq patchCartMenuReq){
        String getSumQuery = "SELECT sum(price) as sum FROM cart_menu WHERE cart_id = ? AND menu_order = ?;";
        String getCountQuery = "SELECT count FROM cart_menu WHERE cart_id = ? AND menu_order = ? AND menu_id = ?;";
        List<Integer> sumList = this.jdbcTemplate.query(getSumQuery,
                (rs, rowNum) -> rs.getInt("sum"),
                new Object[]{cartId, patchCartMenuReq.getMenuOrder()});
        List<Integer> countList = this.jdbcTemplate.query(getCountQuery,
                (rs, rowNum) -> rs.getInt("count"),
                new Object[]{cartId, patchCartMenuReq.getMenuOrder(), patchCartMenuReq.getMenuId()});

        int orderPrice = 0;
        for(int i = 0; i < sumList.size(); i++){
            orderPrice += sumList.get(i)*countList.get(i);
        }

        return this.setOrderPrice(cartId, orderPrice);
    }
    public int setOrderPrice(int cartId, int orderPrice){
        return this.jdbcTemplate.update("UPDATE cart SET order_price = ? WHERE cart_id = ?", orderPrice, cartId);
    }
}
