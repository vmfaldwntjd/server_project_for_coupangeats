package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    @Transactional
    public PostSignUpInRes createUser(PostSignUpReq postSignUpReq) throws BaseException {
        //중복
        if(userProvider.checkEmail(postSignUpReq.getEmail()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        //중복
        if(userProvider.checkPhone(postSignUpReq.getPhone()) ==1){
            throw new BaseException(POST_USERS_EXISTS_PHONE);
        }

//        개발 기간동안은 편의를 위해 암호화하지 않은 비밀번호를 저장합니다.
//        String pwd;
//        try{
//            //암호화
//            pwd = new SHA256().encrypt(postSignUpReq.getPassword());
//            postSignUpReq.setPassword(pwd);
//
//        } catch (Exception ignored) {
//            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
//        }
        try{
            int userId = userDao.createUser(postSignUpReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userId);
            String refreshJwt = jwtService.createRefreshJwt();
            userDao.setTokens(userId, jwt, refreshJwt);
            return new PostSignUpInRes(userId, jwt, refreshJwt);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public PostSignUpInRes signIn(PostSignInReq postSignInReq) throws BaseException{
        User user = userDao.getUser(postSignInReq);

        String encryptPwd = postSignInReq.getPassword();
//          개발 기간동안은 편의를 위해 암호화하지 않은 비밀번호를 저장합니다.
//        String encryptPwd;
//        try {
//            encryptPwd=new SHA256().encrypt(postSignInReq.getPassword());
//        } catch (Exception ignored) {
//            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
//        }

        if(user.getPassword().equals(encryptPwd)){
            int userId = user.getUserId();
//            String jwt = jwtService.createJwt(userId);
//            String refreshJwt = jwtService.createRefreshJwt();
            String jwt = userDao.getUserAccessToken(userId);
            String refreshJwt = userDao.getUserRefreshToken(userId);
            return new PostSignUpInRes(userId, jwt, refreshJwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    public GetSignInJwtRes signInByJwt() throws BaseException{
        int userId = jwtService.getUserId();
        if(userDao.checkId(userId) != 1){ // 존재하지 않는 유저 정보의 jwt
            throw new BaseException(NOT_EXIST_USER_ID_BY_JWT);
        }

        try{
            //String jwt = jwtService.createJwt(userId);
            String jwt = userDao.getUserAccessToken(userId);
            return new GetSignInJwtRes(userId, jwt);
        }
        catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //core 추가
    public void modifyUserAddressDetail(int userId, int userAddressId, PatchUserAddressDetailReq patchUserAddressDetailReq) throws BaseException {
        try {
            int result = userDao.modifyUserAddressDetail(userId, userAddressId, patchUserAddressDetailReq); // 해당 과정이 무사히 수행되면 True(1), 그렇지 않으면 False(0)입니다.
            if (result == 0) { // result값이 0이면 과정이 실패한 것이므로 에러 메서지를 보냅니다.
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //core 추가
    public PostUserAddressRes createUserAddress(int userId, PostUserAddressReq postUserAddressReq) throws BaseException {
        try {
            int userIdx = userDao.createUserAddress(userId, postUserAddressReq);
            return new PostUserAddressRes(userIdx);

        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //core 추가
    public boolean deleteUserFavorite(int userId, int restaurantId) throws BaseException {
        try {
            boolean deleteUserFavoriteRes = userDao.deleteUserFavorite(userId, restaurantId);
            return deleteUserFavoriteRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //core 추가
    public PostUserFavoriteRes createUserFavorite(int userId, int restaurantId) throws BaseException {
        try {
            int favoriteId = userDao.createUserFavorite(userId, restaurantId);
            return new PostUserFavoriteRes(favoriteId);

        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
