package com.example.bookManager.biz;

import com.example.bookManager.model.Ticket;
import com.example.bookManager.model.User;
import com.example.bookManager.model.exceptions.LoginRegisterException;
import com.example.bookManager.service.TicketService;
import com.example.bookManager.service.UserService;
import com.example.bookManager.util.ConcurrentUtil;
import com.example.bookManager.util.MD5;
import com.example.bookManager.util.TicketUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户的登录服务
 */
@Service
public class LoginBiz {
    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    /**
     * 登录逻辑，先检查邮箱和密码，然后更新t票。
     *
     * @param password : 用户登录时输入的密码
     * @return 返回最新t票
     * @throws Exception 账号密码错误
     */
    public String login(String email, String password) throws Exception {
        User user = userService.getUser(email); // 根据用户输入的 email, 获取存储的用户的密码

        //登录信息检查
        if (user == null) {
            throw new LoginRegisterException("邮箱不存在");
        }
        if (!StringUtils.equals(MD5.next(password), user.getPassword())) {
            throw new LoginRegisterException("密码不正确");
        }

        // 检查 ticket
        Ticket t = ticketService.getTicket(user.getId());

        // case1: 如果没有 t 票, 就先获取一个
        if (t == null) {
            t = TicketUtils.next(user.getId());
            ticketService.addTicket(t);
            return t.getTicket();
        }

        // case2: t票过期,
        if (t.getExpiredAt().before(new Date())) { // 截止时间比现在早?
            //删除
            ticketService.deleteTicket(t.getId());
        }

        // 默认生成一个, 没有单点登录功能?
        t = TicketUtils.next(user.getId());
        ticketService.addTicket(t);

        ConcurrentUtil.setHost(user);
        return t.getTicket();
    }

    /**
     * 用户退出登录，只需要删除数据库中用户的t票
     *
     * @param t
     */
    public void logout(String t) {
        ticketService.deleteTicket(t);
    }

    /**
     * 注册一个用户，并返回用户t票
     *
     * @return 用户当前的t票
     */
    public String register(User user) throws Exception {
        // 信息检查, 看用户是否已经注册
        if (userService.getUser(user.getEmail()) != null) {
            throw new LoginRegisterException("用户邮箱已经存在！");
        }

        //密码加密
        String plain = user.getPassword();
        String md5 = MD5.next(plain);
        user.setPassword(md5);

        //数据库添加用户
        userService.addUser(user);

        //生成用户t票
        Ticket ticket = TicketUtils.next(user.getId());
        //数据库添加t票
        ticketService.addTicket(ticket);

        ConcurrentUtil.setHost(user);
        return ticket.getTicket();
    }
}
