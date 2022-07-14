package com.example.demo.src.categories;

import com.example.demo.src.categories.model.GetCategoryRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static com.example.demo.src.categories.query.CategoryQuery.getCategoryListQuery;

@Repository
public class CategoryDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetCategoryRes> getCategoryList(){
        return this.jdbcTemplate.query(getCategoryListQuery,
                (rs, rowNum) -> new GetCategoryRes(
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getString("category_image_url")
                ));
    }

}
