package com.cdtu.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.dto.AddCategoryDto;
import com.cdtu.domain.dto.ListCategoryDto;
import com.cdtu.domain.dto.ModifyCategory;
import com.cdtu.domain.entity.Category;
import com.cdtu.domain.vo.category.ExcelCategoryVo;
import com.cdtu.enums.AppHttpCodeEnum;
import com.cdtu.service.CategoryService;
import com.cdtu.utils.BeanCopyUtils;
import com.cdtu.utils.WebUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Resource
    CategoryService categoryService;
    @GetMapping("/listAllCategory")
    public ResponseResult<?> listAllCategory() {
        return categoryService.listAllCategory();
    }
    @GetMapping("/export")
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    public void exportToExcel(HttpServletResponse response) {
        try {
            // 设置响应头
            String fileName = "分类导出文件.xlsx";
            WebUtils.setExcelHeader(fileName, response);
            // 获取需要导出的数据
            List<Category> categories = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categories, ExcelCategoryVo.class);
            // 写入响应体
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (IOException e) {
            // 如果出现异常也要响应 json
            response.reset();
            ResponseResult<?> result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
    @GetMapping("/list")
    public ResponseResult<?> getListCategory(
            @RequestParam("pageNum")Integer pageNum,
            @RequestParam("pageSize")Integer pageSize,
            ListCategoryDto listCategoryDto) {
        return categoryService.getListCategory(pageNum, pageSize, listCategoryDto);
    }
    @PostMapping
    public ResponseResult<?> addCategory(@RequestBody AddCategoryDto addCategoryDto) {
        return categoryService.addCategory(addCategoryDto);
    }
    @GetMapping("/{id}")
    public ResponseResult<?> getCategoryById(@PathVariable("id")Long id) {
        return categoryService.getCategoryById(id);
    }
    @PutMapping
    public ResponseResult<?> modifyCategory(@RequestBody ModifyCategory modifyCategory) {
        return categoryService.modifyCategory(modifyCategory);
    }
    @DeleteMapping("/{ids}")
    public ResponseResult<?> removeCategory(@PathVariable("ids") String ids) {
        String[] idArray = ids.split(",");
        for (String str_id : idArray) {
            Long id = Long.parseLong(str_id);
            categoryService.removeCategory(id);
        }
        return ResponseResult.okResult();
    }
}
