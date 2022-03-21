package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

/**
 * 描述：  用户控制器
 *
 * @author Lenovo
 */
@RestController
public class UserController {
    @Resource
    UserService userService;

    /**
     * 测试接口
     *
     * @return
     */
    @GetMapping("/test")
    @ResponseBody
    public User personalPage() {
        return userService.getUser();
    }

    /**
     * 注册接口
     *
     * @param username
     * @param password
     * @return
     * @throws ImoocMallException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(@RequestParam("username") String username, @RequestParam("password") String password) throws ImoocMallException, NoSuchAlgorithmException {
        if (StringUtils.isEmpty(username)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }
        if (password.length() < 8) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_TO_SHORT);
        }
        userService.register(username, password);
        return ApiRestResponse.success();
    }

    /**
     * 登录接口
     *
     * @param username
     * @param password
     * @param session
     * @return
     * @throws ImoocMallException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/login")
    @ResponseBody
    public ApiRestResponse login(@RequestParam String username, @RequestParam String password, HttpSession session) throws ImoocMallException, NoSuchAlgorithmException {
        if (StringUtils.isEmpty(username)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }

        // 登录
        User user = userService.login(username, password);

        // 保存用户信息时不保存密码
        user.setPassword(null);
        session.setAttribute(Constant.IMOOC_MALL_USER, user);

        return ApiRestResponse.success(user);
    }

    /**
     * 更新个性签名
     *
     * @param session
     * @param signature
     * @return
     * @throws ImoocMallException
     */
    @PostMapping("/user/update")
    public ApiRestResponse updateUserInfo(HttpSession session, @RequestParam String signature) throws ImoocMallException {

        // 获取登录的时候，存储的session 信息
        User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
        // 获取不到就是未登录
        if (currentUser == null) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
        }

        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);

        userService.updateInformation(user);
        return ApiRestResponse.success();
    }

    /**
     * 登出接口
     *
     * @param session
     * @return
     */
    @PostMapping("/user/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpSession session) {
        session.removeAttribute(Constant.IMOOC_MALL_USER);
        return ApiRestResponse.success();
    }


    /**
     * 管理员登录接口
     *
     * @param username
     * @param password
     * @param session
     * @return
     * @throws ImoocMallException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/adminLogin")
    @ResponseBody
    public ApiRestResponse adminLogin(@RequestParam String username, @RequestParam String password, HttpSession session) throws ImoocMallException, NoSuchAlgorithmException {
        if (StringUtils.isEmpty(username)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }

        // 登录
        User user = userService.login(username, password);

        // 检查是否管理员
        if (userService.checkAdminRole(user)) {
            // 保存用户信息时不保存密码
            user.setPassword(null);
            session.setAttribute(Constant.IMOOC_MALL_USER, user);
            return ApiRestResponse.success(user);
        } else {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
        }
    }
}
