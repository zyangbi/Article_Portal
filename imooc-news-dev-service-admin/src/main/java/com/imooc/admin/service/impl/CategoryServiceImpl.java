package com.imooc.admin.service.impl;

import com.imooc.admin.mapper.CategoryMapper;
import com.imooc.admin.service.CategoryService;
import com.imooc.enums.ResponseStatusEnum;
import com.imooc.exception.GraceException;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.CategoryBO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Transactional
    @Override
    public void createCategory(CategoryBO categoryBO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryBO, category);
        int result = categoryMapper.insert(category);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
    }

    @Transactional
    @Override
    public void updateCategory(CategoryBO categoryBO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryBO, category);
        int result = categoryMapper.updateByPrimaryKey(category);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
    }

    @Override
    public boolean IfNameExist(String name) {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        Category category = categoryMapper.selectOneByExample(example);
        if (category != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Category> getCategoryList() {
        return categoryMapper.selectAll();
    }

}
