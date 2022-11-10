package com.imooc.api.controller.admin;

import com.imooc.pojo.bo.CategoryBO;
import com.imooc.utils.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("categoryMng")
@Api(value = "categoryMngControllerApi")
public interface CategoryMngControllerApi {

    @PostMapping("saveOrUpdateCategory")
    @ApiOperation(value = "save or update category", notes = "save or update category", httpMethod = "POST")
    public GraceJSONResult saveOrUpdateCategory(@RequestBody @Valid CategoryBO newCategoryBO,
                                                BindingResult result);

    @PostMapping("getCatList")
    @ApiOperation(value = "get category list (by admin)", notes = "get category list (by admin)", httpMethod = "POST")
    public GraceJSONResult getCatList();

    @GetMapping("getCats")
    @ApiOperation(value = "get category list (by user)", notes = "get category list (by user)", httpMethod = "GET")
    public GraceJSONResult getCats();

}
