package com.zyq.frechwind.service;

import com.zyq.frechwind.base.Finder;
import com.zyq.frechwind.bean.User;
import com.zyq.frechwind.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TestService {

    @Autowired
    private UserDao userDao;


    public List<User> userList(){
        Finder finder = new Finder("User");
        return userDao.find(finder);
    }

    public User create(){
        User user = new User();
        user.setAccount("123456");
        user.setDelFlag("N");
        user.setUserName("zyq");
        user.setPassWord("123456");
        return userDao.create(user);
    }

    public List<String> numberList(){
        List<String> numberList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numberList.add(String.valueOf(i));
        }
        return numberList;
    }
}
