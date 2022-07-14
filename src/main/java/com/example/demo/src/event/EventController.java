package com.example.demo.src.event;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.event.model.GetEventBannerRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/events")
public class EventController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final EventProvider eventProvider;
    @Autowired
    private final EventService eventService;
    @Autowired
    private final JwtService jwtService;

    public EventController(EventProvider eventProvider, EventService eventService, JwtService jwtService){
        this.eventProvider = eventProvider;
        this.eventService = eventService;
        this.jwtService = jwtService;
    }

/**
 * 8. 상단 광고 배너 리스트 조회 API
 * [GET] /events/top
 * @return BaseResponse<GetEventTopRes>
 *
 */
    @ResponseBody
    @GetMapping("/top")
    public BaseResponse<List<GetEventBannerRes>> getEventsTop(){
        try{
            List<GetEventBannerRes> getEventBannerRes = eventProvider.getEventTopList();
            return new BaseResponse<List<GetEventBannerRes>>(getEventBannerRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/middle")
    public BaseResponse<List<GetEventBannerRes>> getEventsMiddle(){
        try{
            List<GetEventBannerRes> getEventBannerRes = eventProvider.getEventMiddleList();
            return new BaseResponse<List<GetEventBannerRes>>(getEventBannerRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
