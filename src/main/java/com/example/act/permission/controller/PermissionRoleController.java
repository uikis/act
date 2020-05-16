package com.example.act.permission.controller;

import com.example.act.permission.domain.*;
import com.example.act.permission.service.PermissionRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("permission")
public class PermissionRoleController {
    @Autowired
    PermissionRoleService permissionRoleService;


    @RequestMapping("editroles")
    public String editrole(Integer id, Model model) {
        Role role = permissionRoleService.selectRoleById(id);
        model.addAttribute("name", role.getName());

        return "editrole";
    }

    @RequestMapping("assignroles")
    public String assign(Integer id, Model model) {
        List<Role> roles = permissionRoleService.selectAllRoles();
        User user = permissionRoleService.selectUserById(id);
        List<Role> assignedRoles = new ArrayList<>();
        List<Role> unassignedRoles = new ArrayList<>();
        List<Integer> assignedrole = permissionRoleService.queryAssignedRole(id);//利用用户id找到用户在关系表中存在关系的角色id
        //LIST集合可以使用contain
        for (Role role : roles) {
            if (assignedrole.contains(role.getId())) {
                assignedRoles.add(role);
            } else {
                unassignedRoles.add(role);
            }
        }//遍历全部对象，如果角色的id在关系表中能够找到即说明其已经被分配
        model.addAttribute("assignedRoles", assignedRoles);
        model.addAttribute("unassignedRoles", unassignedRoles);//将得到的数据传给前台显示
        model.addAttribute("user", user);//用户信息传给隐藏框，传给前台

        return "assignRole";
    }//接下来就有ajax刷新数据

    @RequestMapping("roleQueryDo")
    @ResponseBody
    public Object pageQuery(String queryText, Integer pageNo, Integer pageNum) {//一个是默认的当前页数，一个是默认的展现条数
        AjaxResult ajaxResult = new AjaxResult();
        try {
//            pageNo = 1;
//            pageNum = 3;
            Map<String, Object> map = new HashMap<>();//使用hashmap实现map
            map.put("start", (pageNo - 1) * pageNum);
            map.put("pageNum", pageNum);//通过计算获得开始的值和个数
            map.put("queryText", queryText);
            List<Role> roles = permissionRoleService.selectRoles(map);
            //        model.addAttribute("users", users);//不跳转页面了，所以不需要传递
            //        model.addAttribute("pageNo",pageNo);//把User对象和当前页数传给前台
            //1.先获取最大数据库中的最大条数
            int maxCount = permissionRoleService.selectCount(map);
            int maxpageNo = 1;
            //2.计算总的页数
            if (maxCount % pageNum == 0) {
                maxpageNo = maxCount / pageNum;
            } else {
                maxpageNo = maxCount / pageNum + 1;
            }

            Page<Role> rolePage = new Page<>();
            rolePage.setMaxCount(maxCount);
            rolePage.setMaxpageNo(maxpageNo);
            rolePage.setPageNo(pageNo);
            rolePage.setObject(roles);
            //【将已知的全部加入封装属性,在将封装好的属性添加到ajaxResult中】
            ajaxResult.setObject(rolePage);//***************
            //       model.addAttribute("maxpageNo", maxpageNo);//需要将总页码和当前页码都传递给前台
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }
        return ajaxResult;
    }

    @RequestMapping("addRoleDo")
    @ResponseBody
    public Object adduser(Role role) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            permissionRoleService.insertRole(role);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }
        return ajaxResult;
    }

    @RequestMapping("editRoleDo")
    @ResponseBody
    public Object editRole(Role role) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            permissionRoleService.updateRole(role);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }
        return ajaxResult;
    }

    @RequestMapping("deleteRoleDo")
    @ResponseBody
    public Object deleteuser(Integer id) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            permissionRoleService.deleteRoleById(id);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }

        return ajaxResult;
    }

    @RequestMapping("deleteRolesDo")
    @ResponseBody
    public Object deleteroles(Integer[] roleid) {
        AjaxResult ajaxResult = new AjaxResult();
        Map<String, Object> map = new HashMap<>();
        map.put("roleid", roleid);
        try {
            permissionRoleService.deleteRoles(map);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }
        return ajaxResult;
    }

    @RequestMapping("unassignDo")
    @ResponseBody
    public Object unassignDo(Integer[] unassignrole, Integer id) {//这儿的id是前台直接传输过来的
        AjaxResult ajaxResult = new AjaxResult();//通过隐藏input取值*****表单序列化
        Map<String, Object> map = new HashMap<>();
        map.put("unassignrole", unassignrole);//这些是左边的对象，插入
        map.put("id", id);//用户id
        try {
            permissionRoleService.unassignDo(map);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }

        return ajaxResult;
    }

    @RequestMapping("assignDo")
    @ResponseBody
    public Object assignDo(Integer[] assignrole, Integer id) {
        AjaxResult ajaxResult = new AjaxResult();
        Map<String, Object> map = new HashMap<>();
        map.put("unassignrole", assignrole);//这些是右边的对象，删除
        map.put("id", id);
        try {
            permissionRoleService.assignDo(map);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }

        return ajaxResult;
    }

    @RequestMapping("assignPermissionDo")
    @ResponseBody
    public Object assignPermissionDo(Integer roleid, Integer[] permissionid) {
        AjaxResult ajaxResult = new AjaxResult();
        Map<String, Object> map = new HashMap<>();
        map.put("roleid", roleid);
        map.put("permissionids", permissionid);
        try {
            permissionRoleService.deletePermissionByRoleid(map);//先删除所有
            if (permissionid != null) {//如果选中的为空值，证明为初始化，即不需要插入permissionid
                permissionRoleService.assignPermission(map);//再选中所有
            }
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            ajaxResult.setSuccess(false);
            e.printStackTrace();
        }

        return ajaxResult;
    }//先删除所有权限，再新增选中的权限

    @RequestMapping("loadAssignDataDo")
    @ResponseBody
    public Object loadAssignData(Integer id) {

        List<Permission> permissions = permissionRoleService.queryAllPermisson(id);
        return permissions;
    }
}
