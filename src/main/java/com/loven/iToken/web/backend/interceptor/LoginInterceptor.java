package com.loven.iToken.web.backend.interceptor;

import com.loven.iToken.common.domain.TbSysUser;
import com.loven.iToken.common.web.configuration.AddressConfig;
import com.loven.iToken.common.web.utils.HttpServletUtils;
import com.loven.iToken.commons.constant.ConstantString;
import com.loven.iToken.commons.utils.CookieUtils;
import com.loven.iToken.commons.utils.MapperUtils;
import com.loven.iToken.web.backend.service.RedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by loven on 2020/5/3.
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Value("${service.sso_address}")
    private String sso_address;

    @Autowired
    RedisService redisService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieUtils.getCookieValue(request, ConstantString.TOKEN);
        if (StringUtils.isBlank(token)) {
            response.sendRedirect(String.format("%s/v1/user/login?url=%s", sso_address, HttpServletUtils.getFullPath(request)));
            return false;
        }
        else {
            HttpSession session = request.getSession();
            TbSysUser user = (TbSysUser) session.getAttribute(ConstantString.SESSION_USER);

            //已登录状态
            if (user != null) {
                return true;
            }

            //未登录状态
            else {
                if (StringUtils.isNotBlank(token)) {
                    String loginCode = redisService.get(token);
                    if (StringUtils.isNotBlank(loginCode)) {
                        String json = redisService.get(loginCode);
                        if (StringUtils.isNotBlank(json)) {
                            try {
                                user = MapperUtils.json2pojo(json, TbSysUser.class);
                                request.getSession().setAttribute(ConstantString.SESSION_USER, user);
                                return true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            //二次确认是否有用户信息
            if (user == null) {
                response.sendRedirect(String.format("%s/v1/user/login?url=%s", sso_address, HttpServletUtils.getFullPath(request)));
            }
            return false;
        }
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView != null){
            modelAndView.addObject(ConstantString.SESSION_USER,request.getSession().getAttribute(ConstantString.SESSION_USER));
            modelAndView.addObject("Host", sso_address);
            modelAndView.addObject("url", HttpServletUtils.getFullPath(request));
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
