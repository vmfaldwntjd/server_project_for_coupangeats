package com.example.demo.src.event;

import com.example.demo.config.BaseException;
import com.example.demo.src.event.model.GetEventBannerRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class EventProvider {
    private final EventDao eventDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public EventProvider(EventDao eventDao, JwtService jwtService) {
        this.eventDao = eventDao;
        this.jwtService = jwtService;
    }

    public List<GetEventBannerRes> getEventTopList() throws BaseException{
        try{
            List<GetEventBannerRes> getEventTopList = eventDao.getEventTopList();
            return getEventTopList;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public List<GetEventBannerRes> getEventMiddleList() throws BaseException{
        try{
            List<GetEventBannerRes> getEventMiddleList = eventDao.getEventMiddleList();
            return getEventMiddleList;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
