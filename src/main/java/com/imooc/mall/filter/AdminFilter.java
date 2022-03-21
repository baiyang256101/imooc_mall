package com.imooc.mall.filter;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.client.support.HttpRequestWrapper;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 描述：管理员校验过滤器
 */
public class AdminFilter implements Filter {
    @Resource
    UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();

        User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
        if (currentUser == null) {
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            out.write("{\n" + "    \"status\": 10007,\n" + "    \"msg\": \"NEED_LOGIN!\",\n" + "    \"data\": null\n" + "}");
            out.flush();
            out.close();
            return;
        }
        // 校验是否是管理员

        if (userService.checkAdminRole(currentUser)) { // 是管理员
            chain.doFilter(servletRequest, servletResponse);
        } else {
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            out.write("{\n" + "    \"status\": 10009,\n" + "    \"msg\": \"NEED_ADMIN!\",\n" + "    \"data\": null\n" + "}");
            out.flush();
            out.close();
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
