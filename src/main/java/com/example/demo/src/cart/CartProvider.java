package com.example.demo.src.cart;

import com.example.demo.config.BaseException;
import com.example.demo.src.cart.model.DiscountInfo;
import com.example.demo.src.cart.model.GetOrderPriceRes;
import com.example.demo.src.cart.model.RequestMessageInfo;
import com.example.demo.src.cart.model.ResOrderMenuInfo;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class CartProvider {

    private final CartDao cartDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CartProvider(CartDao cartDao, JwtService jwtService){
        this.cartDao = cartDao;
        this.jwtService = jwtService;
    }

    public int getResIdByUserId(int userId) throws BaseException{
        try{
            return cartDao.getResIdByUserId(userId);
        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getCartIdByUserId(int userId) throws BaseException{
        try {
            return cartDao.getCartIdByUserId(userId);
        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetOrderPriceRes getOrderPrice(int userId) throws BaseException {
        try {
            return cartDao.getOrderPrice(userId);
        } catch(EmptyResultDataAccessException exception) {
            throw new BaseException(NO_CART_FOR_USER_ID);
        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getOptionInfoString(int cartId, int menuId, int menuOrder) throws BaseException {
        try {
            return cartDao.getOptionInfoString(cartId, menuId,  menuOrder);
        } catch (Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<ResOrderMenuInfo> getResOrderMenuInfo(int cartId) throws BaseException {
        try {
            return cartDao.getResOrderMenuInfo(cartId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public RequestMessageInfo getRequestMessageInfo(int cartId) throws BaseException{
        try {
            return cartDao.getRequestMessageInfo(cartId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public DiscountInfo getDiscountInfo(int cartId) throws BaseException {
        try {
            return cartDao.getDiscountInfo(cartId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
