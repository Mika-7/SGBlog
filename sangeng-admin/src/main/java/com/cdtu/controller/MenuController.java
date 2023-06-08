package com.cdtu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.entity.Menu;
import com.cdtu.service.MenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Resource
    MenuService menuService;
    @GetMapping("/list")
    public ResponseResult<?> listMenu(String status, String menuName) {
        return menuService.listMenu(status, menuName);
    }
    @GetMapping("/{id}")
    public ResponseResult<?> getMenu(@PathVariable("id")Long id) {
        return menuService.getMenu(id);
    }

    @PostMapping
    public ResponseResult<?> addMenu(@RequestBody Menu menu) {
        return menuService.addMenu(menu);
    }
    @PutMapping
    public ResponseResult<?> modifyMenu(@RequestBody Menu menu) {
        return menuService.modifyMenu(menu);
    }
    @DeleteMapping("/{ids}")
    public ResponseResult<?> removeMeun(@PathVariable("ids") String ids) {
        String[] idArray = ids.split(",");
        for (String str_id : idArray) {
            Long id = Long.parseLong(str_id);
            menuService.removeMenu(id);
        }
        return ResponseResult.okResult();
    }
    @GetMapping("/treeselect")
    public ResponseResult<?> getTreeSelect() {
        return menuService.getTreeSelect();
    }
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult<?> getRoleMenuTreeSelect(@PathVariable("id")Long id) {
        return menuService.getRoleMenuTreeSelect(id);
    }
}
