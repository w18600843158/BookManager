package com.example.bookManager.service;


import com.example.bookManager.model.User;
import com.example.bookManager.util.ConcurrentUtil;
import org.springframework.stereotype.Service;

/**
 * 包装 oncurrentUtils.java 中的方法
 */
@Service
public class HostHolder {

    public User getUser() {
        return ConcurrentUtil.getHost();
    }

    public void setUser(User user) {
        ConcurrentUtil.setHost(user);
    }
}
