package com.example.bookManager.interceptor;



import com.example.bookManager.model.Ticket;
import com.example.bookManager.service.TicketService;
import com.example.bookManager.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 登录的拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

  @Autowired
  private TicketService ticketService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    //没有t票，去登陆
    String t = CookieUtil.getCookie("t",request);
    if(StringUtils.isEmpty(t)){
      response.sendRedirect("/users/login");
      return false;
    }

    //无效t票，去登陆
    Ticket ticket = ticketService.getTicket(t);
    if(ticket == null){
      response.sendRedirect("/users/login");
      return false;
    }

    //过期t票，去登陆
    if(ticket.getExpiredAt().before(new Date())){
      response.sendRedirect("/users/login");
      return false;
    }

    return true;
  }
}
