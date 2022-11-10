package com.imooc.admin.controller;

import com.imooc.admin.service.CategoryService;
import com.imooc.api.BaseController;
import com.imooc.api.controller.admin.CategoryMngControllerApi;
import com.imooc.enums.ResponseStatusEnum;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.CategoryBO;
import com.imooc.utils.GraceJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryMngController extends BaseController implements CategoryMngControllerApi {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisOperator redis;

    @Override
    public GraceJSONResult saveOrUpdateCategory(CategoryBO categoryBO, BindingResult result) {
        if (result.hasErrors()) {
            return GraceJSONResult.errorMap(getErrorMap(result));
        }

        if (categoryBO.getId() == null) {
            if (!categoryService.IfNameExist(categoryBO.getName())) {
                // create category
                categoryService.createCategory(categoryBO);
                redis.del(REDIS_CATEGORY);
                return GraceJSONResult.ok();
            } else {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.CATEGORY_EXIST_ERROR);
            }
        } else {
            if (categoryService.IfNameExist(categoryBO.getOldName()) &&
                    !categoryService.IfNameExist(categoryBO.getName())) {
                // update category
                categoryService.updateCategory(categoryBO);
                redis.del(REDIS_CATEGORY);
                return GraceJSONResult.ok();
            } else {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.CATEGORY_EXIST_ERROR);
            }
        }
    }

    @Override
    public GraceJSONResult getCatList() {
        String categoryJson = redis.get(REDIS_CATEGORY);
        List<Category> categoryList = null;
        if (StringUtils.isNotBlank(categoryJson)) {
            categoryList = JsonUtils.jsonToList(categoryJson, Category.class);
        } else {
            categoryList = categoryService.getCategoryList();
            redis.set(REDIS_CATEGORY, JsonUtils.objectToJson(categoryList));
        }
        return GraceJSONResult.ok(categoryList);
    }

    @Override
    public GraceJSONResult getCats() {
        String categoryJson = redis.get(REDIS_CATEGORY);
        List<Category> categoryList = null;
        if (StringUtils.isNotBlank(categoryJson)) {
            categoryList = JsonUtils.jsonToList(categoryJson, Category.class);
        } else {
            categoryList = categoryService.getCategoryList();
            redis.set(REDIS_CATEGORY, JsonUtils.objectToJson(categoryList));
        }
        return GraceJSONResult.ok(categoryList);
    }
}
