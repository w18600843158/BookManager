package com.example.bookManager.interceptor;

import com.example.bookManager.model.Ticket;
import com.example.bookManager.model.User;
import com.example.bookManager.service.TicketService;
import com.example.bookManager.service.UserService;
import com.example.bookManager.util.ConcurrentUtil;
import com.example.bookManager.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.ConcurrentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 需求: 希望在做所有对书本的操作之前，都进行一次t票的认证操作，即通过t票找到t票对应的用户，并将用户信息放入HostHoder中.
 * host info 拦截器, 功能 :拦截器试图通过请求中的Cookie来寻找t票, 一旦寻找到t票并成功的从数据库中找到了对应的用户，就直接放入HostHolder。
 * 这里解释了，为什么=在登录一次之后，再进行其他的操作时，服务器都能识别操作用户是谁，甚至你关闭浏览器之后再次打开也不用重新登录，因为服务器跟浏览器发送的请求中附带的Cookie对你的身份自动进行了认证。
 *
 */


@Component
/**
 * 拦截器的功能实现, 实现接口 HandlerInterceptor
 */
public class HostInfoInterceptor implements HandlerInterceptor {
    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception{
        String t = CookieUtil.getCookie("t", request); //从浏览器的请求中获取 cookie
        if (!StringUtils.isEmpty(t)) {
            Ticket ticket = ticketService.getTicket(t); // cookie 不为空时, 获取 ticket
            if (ticket != null && ticket.getExpiredAt().after(new Date())) { // 从数据库中能获取到 ticket, 且 ticket 未过期
                User host = userService.getUser(ticket.getUserId()); // 获取用户
                ConcurrentUtil.setHost(host); // 将用户与线程 id 绑定
            }
        }
        return true;
    }
}
