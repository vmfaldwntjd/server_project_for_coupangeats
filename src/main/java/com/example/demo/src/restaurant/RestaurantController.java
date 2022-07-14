package com.example.demo.src.restaurant;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.restaurant.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/restaurants")
public class RestaurantController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final RestaurantProvider restaurantProvider;

    public RestaurantController(JwtService jwtService, RestaurantProvider restaurantProvider){
        this.jwtService = jwtService;
        this.restaurantProvider = restaurantProvider;
    }

    @ResponseBody
    @GetMapping("/franchise")
    public BaseResponse<List<GetFNRestaurantRes>> getFranchiseRestaurantList(@RequestParam(required = false) Double longitude, @RequestParam(required = false) Double latitude) {
        try {
            List<GetFNRestaurantRes> getRestaurantResList = restaurantProvider.getFranchiseRestaurantList(longitude, latitude);
            return new BaseResponse<>(getRestaurantResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/new")
    public BaseResponse<List<GetFNRestaurantRes>> getNewRestaurantList (@RequestParam(required = false) Double longitude, @RequestParam(required = false) Double latitude) {
        try {
                List<GetFNRestaurantRes> getRestaurantResList = restaurantProvider.getNewRestaurantList(longitude, latitude);
                return new BaseResponse<>(getRestaurantResList);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * 12. 현재 위치 및 조건에 따른 카테고리별 가게 리스트 API
     * [GET] ?categoryId={categoryId}&longitude={longitude}&latitude={latitude}
     * @return BaseResponse<List<GetRestaurantByCategoryIdRes>>
     *
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetRestaurantByCategoryIdRes>> getRestaurantListByCategoryId(@RequestParam(required = false) Integer categoryId, @RequestParam(required = false) Double longitude, @RequestParam(required = false) Double latitude, @RequestParam(required = false) String sortBy){
        try{
            if(categoryId == null){ // 골라먹는 맛집
                List<GetRestaurantByCategoryIdRes> getRestaurantResList = restaurantProvider.getRestaurantList(longitude, latitude, sortBy);
                return new BaseResponse<>(getRestaurantResList);
            }
            // 10번 카테고리별 가게 리스트
            List<GetRestaurantByCategoryIdRes> getRestaurantResList = restaurantProvider.getRestaurantListByCategoryId(categoryId, longitude, latitude, sortBy);
            return new BaseResponse<>(getRestaurantResList);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * 13. 가게 메인 화면 정보 API
     * [GET] /:restaurantId??longitude={longitude}&latitude={latitude}
     * @return BaseResponse<GetRestaurantRes>
     *
     */
    @ResponseBody
    @GetMapping("/{restaurantId}")
    public BaseResponse<GetRestaurantByIdRes> getRestaurantById(@PathVariable Integer restaurantId, @RequestParam(required = false) Double longitude, @RequestParam(required = false) Double latitude){
        try{
            GetRestaurantByIdRes getRestaurantRes = restaurantProvider.getRestaurantById(restaurantId, longitude, latitude);
            return new BaseResponse<>(getRestaurantRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 14. 가게 메인 화면 메뉴 종류 API
     * [GET] /:restaurantId/kinds
     * @return BaseResponse<List<GetResKindRes>>
     *
     */
    @ResponseBody
    @GetMapping("/{restaurantId}/kinds")
    public BaseResponse<List<GetResKindRes>> getResKindList(@PathVariable Integer restaurantId){
        try{
            List<GetResKindRes> getResKindResList = restaurantProvider.getResKindList(restaurantId);
            return new BaseResponse<>(getResKindResList);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 15. 가게 메인 화면 종류별 메뉴
     * [GET] /:restaurantId/menus
     * @return BaseResponse<List<GetResKindMenuRes>>
     *
     */
    @ResponseBody
    @GetMapping("/{restaurantId}/menus")
    public BaseResponse<List<GetResKindMenuRes>> getResKindMenuList(@PathVariable Integer restaurantId){
        try {
            List<GetResKindMenuRes> getResKindMenuList = restaurantProvider.getResKindMenuList(restaurantId);
            return new BaseResponse<>(getResKindMenuList);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 16. 메뉴 정보 API
     * [GET] /:restaurantId/menus/:menuId
     * @return BaseResponse<GetResMenuRes>
     *
     */
    @ResponseBody
    @GetMapping("/{restaurantId}/menus/{menuId}")
    public BaseResponse<GetResMenuRes> getResMenuList(@PathVariable Integer restaurantId, @PathVariable int menuId){
        try {
            ResMenuInfo resMenuInfo = restaurantProvider.getResMenuInfo(restaurantId, menuId);
            List<ResMenuOption> resMenuOptionList = restaurantProvider.getResMenuOption(restaurantId, menuId);
            GetResMenuRes getResMenu = new GetResMenuRes(resMenuInfo, resMenuOptionList);
            return new BaseResponse<>(getResMenu);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
