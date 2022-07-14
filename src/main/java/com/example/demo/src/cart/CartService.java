package com.example.demo.src.cart;

import com.example.demo.config.BaseException;
import com.example.demo.src.cart.model.*;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class CartService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CartDao cartDao;
    private final CartProvider cartProvider;
    private final JwtService jwtService;
    private final UserProvider userProvider;

    @Autowired
    public CartService(CartDao cartDao, CartProvider cartProvider, JwtService jwtService, UserProvider userProvider){
        this.cartDao = cartDao;
        this.cartProvider = cartProvider;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
    }

    /* 현재 구조는 똑같은 메뉴를 추가해도 새로운 레코드를 삽입하는 구조.
    *  똑같은 메뉴를 추가하면 menuCount를 + 1 해야한다. -> 어려움
    * */
    @Transactional(rollbackOn = Exception.class)
    public PostCartRes createCart(PostCartReq postCartReq) throws BaseException{
        // 필수 옵션을 선택하지 않은경우 카트를 비운 후 insert가 반려된다.

        // TODO : cart validation 처리
        int userId = postCartReq.getUserId();
        int restaurantId = postCartReq.getRestaurantId();

        int cartId = cartProvider.getCartIdByUserId(userId);
        int resIdByUserId = cartProvider.getResIdByUserId(userId);


        // 카트가 존재하면서 res id가 다르다면 Request error
        if(cartId > 0 && restaurantId != resIdByUserId){
            throw new BaseException(POST_CARTS_ANOTHER_RESTAURANT_ID_EXISTS);
        }



        // 필수 선택 옵션을 선택하지 않았다면 Request error
        for(OptionKindInfo oki : postCartReq.getOptionKindInfoList()){
            if(oki.getIsEssential() && oki.getOptionInfoList().size() == 0){
                throw new BaseException(POST_CARTS_EMPTY_ESSENTIAL_OPTION);
            }
        }


        try {
            // 카트가 없다면 1, 있다면 그 다음의 order값.
            int menuOrder = cartDao.getLastMenuOrder(cartId) + 1;
            int menuId = postCartReq.getMenuId();
            // 카트가 없다면 카트 생성
            if(cartId == -1) {
                // 선택된 주소 정보를 기입합니다.

                Integer userAddressId = userProvider.getUserAddressId(postCartReq.getUserId());
                if(userAddressId == -1) userAddressId = null;
                cartId = cartDao.createCart(postCartReq, userAddressId);
            } else { // 카트 정보가 있다면 현재 메뉴가 중복되는지 검사합니다.
                // 현재 카트에, 현재 메뉴에 대해 존재하는 옵션이 현재 요청 메뉴의 옵션과 겹치는지.
                List<OptionKindInfo> optionKindInfoList = postCartReq.getOptionKindInfoList();
                StringBuilder sb = new StringBuilder();
                int sum = postCartReq.getMenuPrice();

                // 현재 요청 메뉴 옵션 문자열로 전환
                for(OptionKindInfo optionKindInfo : optionKindInfoList){
                    List<OptionInfo> optionInfoList = optionKindInfo.getOptionInfoList();
                    for(OptionInfo optionInfo : optionInfoList){
                        sb.append(optionInfo.getOptionName()).append(optionInfo.getOptionPrice() > 0 ? "(+"+optionInfo.getOptionPrice()+"원), " : "");
                        sum += optionInfo.getOptionPrice();
                    }
                }
                sum = sum*postCartReq.getMenuCount();

                String optionInfoNow = sb.toString();
                optionInfoNow = optionInfoNow.substring(optionInfoNow.length() - 2).contentEquals(", ") ?
                        optionInfoNow.substring(0, optionInfoNow.length() - 2) : optionInfoNow;



                // 현재 메뉴를 단락 메뉴로 가지는 모든 주문에 대해
                List<Integer> menuOrderListInCart = cartDao.getCartMenuOption(cartId, menuId);
                for(int menuOrderInCart : menuOrderListInCart){

                    String optionInfoInCart = cartDao.getOptionInfoString(cartId, menuId, menuOrderInCart);

                    // 동일한 옵션 정보를 가지는 단락메뉴 주문이 존재한다면
                    if(optionInfoInCart.contentEquals(optionInfoNow)){

                        // 해당 수를 주문 개수만큼 증가시키고 총 주문금액을 업데이트 한 뒤 종료한다.
                        int t = cartDao.increaseMenuCount(cartId, menuId, postCartReq.getMenuCount());
                        if(t != 1){
                            throw new BaseException(DATABASE_ERROR);
                        }

                        // 총 주문 금액 합산.
                        int test = cartDao.addTotalPrice(cartId, sum);
                        if(test != 1){
                            throw new BaseException(DATABASE_ERROR);
                        }

                        int totalPrice = cartProvider.getOrderPrice(userId).getOrderPrice();
                        return new PostCartRes(userId, cartId, totalPrice);
                    }
                }

            }

            int sum = cartDao.createCartMenu(cartId, menuOrder, postCartReq);
            int t = cartDao.addTotalPrice(cartId, sum);

            if(t != 1){
                throw new BaseException(DATABASE_ERROR);
            }

            int totalPrice = cartProvider.getOrderPrice(userId).getOrderPrice();

            return new PostCartRes(userId, cartId, totalPrice);
        } catch(Exception exception){

            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public UpdateCartRes deleteCart(int cartId) throws BaseException {
        int result;
        try {
            result = cartDao.deleteCart(cartId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

        if(result == 0){
            throw new BaseException(NO_UPDATED_CART_INFO); // 삭제된 카트 정보가 없음.
        }
        return new UpdateCartRes(cartId);
    }

    @Transactional(rollbackOn = Exception.class)
    public UpdateCartRes updateCart(@PathVariable int cartId, @RequestBody PatchCartMenuReq patchCartMenuReq) throws BaseException{
        int result;
        try {
            result = cartDao.updateCart(cartId, patchCartMenuReq);
        } catch (Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
        if(result == 0){
            throw new BaseException(NO_UPDATED_CART_INFO); // 업데이트된 카트 정보가 없음.
        }

        try {
            result = cartDao.updateCartOrderPrice(cartId, patchCartMenuReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
        if(result == 0){
            throw new BaseException(NO_UPDATED_CART_INFO); // 업데이트된 카트 정보가 없음.
        }

        return new UpdateCartRes(cartId);

    }
}
