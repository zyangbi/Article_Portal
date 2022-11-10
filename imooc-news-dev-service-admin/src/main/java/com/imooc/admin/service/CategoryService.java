package com.imooc.admin.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.bo.CategoryBO;

import java.util.List;

public interface CategoryService {

        public void createCategory(CategoryBO categoryBO);

        public void updateCategory(CategoryBO categoryBO);

        public boolean IfNameExist(String name);

        public List<Category> getCategoryList();

}
