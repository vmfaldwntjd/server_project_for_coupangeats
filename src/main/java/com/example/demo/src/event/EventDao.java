package com.example.demo.src.event;

import com.example.demo.src.event.model.GetEventBannerRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static com.example.demo.src.event.query.EventQuery.getEventMiddleListQuery;
import static com.example.demo.src.event.query.EventQuery.getEventTopListQuery;

@Repository
public class EventDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetEventBannerRes> getEventTopList(){
        return this.jdbcTemplate.query(getEventTopListQuery,
                (rs,rowNum) -> new GetEventBannerRes(
                        rs.getInt("event_id"),
                        rs.getString("event_image_url")
                ));
    }

    public List<GetEventBannerRes> getEventMiddleList(){
        return this.jdbcTemplate.query(getEventMiddleListQuery,
                (rs,rowNum) -> new GetEventBannerRes(
                        rs.getInt("event_id"),
                        rs.getString("event_image_url")
                ));
    }
}
