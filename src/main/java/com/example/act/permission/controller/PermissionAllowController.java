package com.example.act.permission.controller;

import com.example.act.permission.domain.AjaxResult;
import com.example.act.permission.domain.Permission;
import com.example.act.permission.service.PermissionAllowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("permission")
public class PermissionAllowController {

    @Autowired
    PermissionAllowService permissionAllowService;

    @RequestMapping("loadDataDo")
    @ResponseBody
    public Object load() {
        List<Permission> permissions = permissionAllowService.queryAllPermisson();

        return permissions;
    }

    @RequestMapping("deletepermission")
    @ResponseBody
    public Object deletePermission(Permission permission) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            permissionAllowService.deletePermissionById(permission);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }
        return ajaxResult;
    }

    @RequestMapping("addpermission")
    public String addpermission(Permission permission) {
        permissionAllowService.insertPermission(permission);
        return "permission";
    }

    @RequestMapping("editpermission")
    public String updatepermission(Permission permission) {
        Permission permission1 = permissionAllowService.queryPermissionById(permission);
//        model.addAttribute("name", permission1.getName());
//        model.addAttribute("url", permission1.getUrl());
        permissionAllowService.updataPermission(permission);
        return "permission";
    }
}
