package com.example.demo.src.categories;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.categories.model.GetCategoryRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app/categories")
public class CategoryController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CategoryProvider categoryProvider;
//    @Autowired
//    private final CategoryService categoryService;
    @Autowired
    private final JwtService jwtService;

    public CategoryController(CategoryProvider categoryProvider, JwtService jwtService){
        this.categoryProvider = categoryProvider;
        this.jwtService = jwtService;
    }

    /**
     * 9. 카테고리 정보 API
     * [GET] /categories
     * @return BaseResponse<List<GetCategoryRes>>
     *
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetCategoryRes>> getCategoryList(){
        try{
            List<GetCategoryRes> getCategoryListRes = categoryProvider.getCategoryList();
            return new BaseResponse<List<GetCategoryRes>>(getCategoryListRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
