package com.example.demo.src.cart;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.cart.model.*;
import com.example.demo.src.restaurant.RestaurantProvider;
import com.example.demo.src.restaurant.model.RecommendMenuInfo;
import com.example.demo.src.restaurant.model.RestaurantInfo;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.model.GetUserAddressCartRes;
import com.example.demo.utils.JwtService;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.hibernate.sql.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/carts")
public class CartController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final CartProvider cartProvider;
    @Autowired
    private final CartService cartService;
    @Autowired
    private final RestaurantProvider restaurantProvider;
    @Autowired
    private final UserProvider userProvider;

    public CartController(JwtService jwtService, CartProvider cartProvider, CartService cartService,
                          RestaurantProvider restaurantProvider, UserProvider userProvider){
        this.jwtService = jwtService;
        this.cartProvider = cartProvider;
        this.cartService = cartService;
        this.restaurantProvider = restaurantProvider;
        this.userProvider = userProvider;
    }

    /**
     * 18. 카트 담기 요청 API
     * [POST]
     * @return BaseResponse<PostCartRes>
     *
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostCartRes> createCart(@RequestBody PostCartReq postCartReq){
        try{
            PostCartRes postCartRes = cartService.createCart(postCartReq);
            return new BaseResponse<>(postCartRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 50. 카트 총 금액 조회 API
     * [POST]
     * @return BaseResponse<GetCartRes>
     *
     */
    @ResponseBody
    @GetMapping("/totalPrice")
    public BaseResponse<GetOrderPriceRes> getTotalPrice(){
        try{
            int userId = jwtService.getUserId();
            return new BaseResponse<>(cartProvider.getOrderPrice(userId));
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
//     * 17. 카트 화면 조회 API
//     * [GET]
//     * @return BaseResponse<GetCartRes>
//     * */
    @ResponseBody
    @GetMapping("/{cartId}")
    public BaseResponse<GetCartRes> getCartRes(@PathVariable int cartId){

        try{
            int userId = jwtService.getUserId();
            GetOrderPriceRes getOrderPrice = cartProvider.getOrderPrice(userId);


            // 현재 유저의 선택된 주소 정보를 가져온다. 메소드 core 작성.
            GetUserAddressCartRes getUserAddressCartRes = userProvider.getUserAddressInfo(userId);

            // 위의 주소 정보를 기반으로 배달 소요시간과 배달 비용을 계산한다. 거리가 5km이상이라면 배달 불가 에러 코드 반환.
            RestaurantInfo restaurantInfo = restaurantProvider.getResInfo(cartId, getOrderPrice.getOrderPrice(), getUserAddressCartRes.getLatitude(), getUserAddressCartRes.getLongitude()); // 구현 완료


            // 가게에 주문하려는 메뉴의 상세 리스트 정보를 조회한다.
            List<ResOrderMenuInfo> resOrderMenuInfo = cartProvider.getResOrderMenuInfo(getOrderPrice.getCartId());
            restaurantInfo.setResOrderMenuInfo(resOrderMenuInfo);

            // 해당 가게의 함께 주문하면 좋을 메뉴 리스트
            List<RecommendMenuInfo> recommendMenuInfoList = restaurantProvider.getRecommendMenuList(restaurantInfo.getRestaurantId()); // 구현 완료

            // 현재 카트에 적용된 할인 정보를 가져온다.
            DiscountInfo discountInfo = cartProvider.getDiscountInfo(cartId);
            // discountInfo.setCouponCount(couponProvider.getCouponCount(restaurantInfo.getRestaurantId())); // 현재 가게에 대해 사용가능한 쿠폰 개수 설정
            // 가격 정보 최종 합산.
            discountInfo.setTotalPrice(discountInfo.getTotalPrice() + getOrderPrice.getOrderPrice());

            // 가게 요청사항, 일회용품 사용 여부, 배달 요청사항 정보 로드.
            RequestMessageInfo requestMessageInfo = cartProvider.getRequestMessageInfo(cartId);

            // 결재 수단의 경우 현재 유저의 선택된 결재 수단 정보를 갖고온다.
            // PaymentInfo paymentInfo = paymentProvider.getPaymentInfo(userId);
            // 현재는 paymentProvider의 기능이 구현되어있지 않기 때문에 임의의 값을 설정한다.
            // 순서대로 결제수단 id, 결제수단 type, 결제수단 이름이며, 클라이언트는 출력시 결제수단 이름만을 사용한다.
            Object[] paymentInfo = new Object[]{1, "card", "농협은행 ****4568"};

            return new BaseResponse<>(new GetCartRes(
                    getUserAddressCartRes,
                    restaurantInfo,
                    recommendMenuInfoList,
                    discountInfo,
                    requestMessageInfo,
                    paymentInfo
            ));
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 48. 카트 삭제(장바구니 비우기)API
     * [GET]
     * @return BaseResponse<UpdateCartRes>
     * */
    @ResponseBody
    @DeleteMapping("/{cartId}")
    public BaseResponse<UpdateCartRes> deleteCart(@PathVariable int cartId){
        try {
            UpdateCartRes deleteCartRes = cartService.deleteCart(cartId);
            return new BaseResponse<>(deleteCartRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 49. 카트 정보 수정 API
     * [GET]
     * @return BaseResponse<UpdateCartRes>
     * */
    @ResponseBody
    @PatchMapping("/{cartId}/menus")
    public BaseResponse<UpdateCartRes> updateCart(@PathVariable int cartId, @RequestBody PatchCartMenuReq patchCartMenuReq){
        try {
            UpdateCartRes updateCartRes = cartService.updateCart(cartId, patchCartMenuReq);
            return new BaseResponse<>(updateCartRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
