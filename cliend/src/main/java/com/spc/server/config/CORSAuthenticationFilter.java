package com.spc.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spc.server.Controller.JsonResult;
import com.spc.server.pojo.AuthToken;
import com.spc.server.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CORSAuthenticationFilter extends FormAuthenticationFilter {


    /**
     * 获取token
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        String requestToken = getRequestToken((HttpServletRequest) servletRequest);
        return new AuthToken(requestToken);
    }

    /**
     * 对跨域提供支持
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 步骤1.所有请求全部拒绝访问
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        return false;
    }

    /**
     * 验证token
     * 当访问拒绝时是否已经处理了；
     * 如果返回true表示需要继续处理；
     * 如果返回false表示该拦截器实例已经处理完成了，将直接返回即可。
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        //完成token登入
        //1.检查请求头中是否含有token
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String login = ((HttpServletRequest) servletRequest).getServletPath();

        //如果为登录,就放行
        if ("/login.action".equals(login)) {
            return true;
        }
        //如果为登出,就放行
        if ("/logout".equals(login)) {
            return true;
        }
        //查看是否登出
        Subject subject = SecurityUtils.getSubject();
        subject.getSession().getId();
        System.out.println("------subject.getSession().getId()"+subject.getSession().getId());
//        if(subject.getPrincipal()==null){
//            responseTokenError(servletResponse, "已退出，请重新登录");
//            return false;
//        }
        String token = getRequestToken(httpServletRequest);
        //2. 如果客户端没有携带token，拦下请求
        if (null == token || "".equals(token)) {
            responseTokenError(servletResponse, "Token为空，您无权访问该接口");
            return false;
        }
        //3. 如果有，对进行进行token验证
        return executeLogin(servletRequest, servletResponse);
    }

    /**
     * 执行认证
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        AuthToken token = (AuthToken) createToken(request, response);

        try {
            Subject subject = SecurityUtils.getSubject();
            //调用realm doGetAuthenticationInfo 验证
            subject.login(token);
            return true;
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }

    }


    /**
     * 获取请求的token
     */
    private String getRequestToken(HttpServletRequest httpRequest) {
        //从header中获取token
        String token = httpRequest.getHeader("token");

        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isEmpty(token)) {
            token = httpRequest.getParameter("token");
        }
        if (StringUtils.isEmpty(token)) {
            Cookie[] cks = httpRequest.getCookies();
            if (cks != null) {
                for (Cookie cookie : cks) {
                    if (cookie.getName().equals("token")) {
                        token = cookie.getValue();
                        return token;
                    }
                }
            }
        }
        return token;
    }

    /**
     * 无需转发，直接返回Response信息 Token认证错误
     */
    private void responseTokenError(ServletResponse response, String msg) {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try {
            PrintWriter out = httpServletResponse.getWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            String data = objectMapper.writeValueAsString(new JsonResult<>(401, msg));
            out.append(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * token失效时候调用
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpResponse.setCharacterEncoding("UTF-8");
        try {
            //处理登录失败的异常
            PrintWriter out = httpResponse.getWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            String data = objectMapper.writeValueAsString(new JsonResult<>(401, e.getMessage()));
            out.append(data);
        } catch (IOException e1) {
        }
        return false;
    }
}
