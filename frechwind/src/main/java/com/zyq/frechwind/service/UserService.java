package com.zyq.frechwind.service;

import com.zyq.frechwind.base.Finder;
import com.zyq.frechwind.bean.User;
import com.zyq.frechwind.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserDao userDao;


    public List<User> userList(){
        Finder finder = new Finder("User");
        return userDao.find(finder);
    }

    public User create(String userName, String password, String email){
        User user = new User();
        user.setAccount(userName);
        user.setDelFlag("N");
        user.setUserName(userName);
        user.setPassWord(password);
        return userDao.create(user);
    }

    /**
     * 账号检测
     * @param account
     * @return
     */
    public User userAccount(String account){
        String hql = "from User where account=? and delFlag=?";
        User user = userDao.getUnique(hql,account,"N");
        return user;
    }

    /**
     * 账号密码登录
     * @param account
     * @param passWord
     * @return
     */
    public User login(String account, String passWord){
        String hql = "from User where account = ? and passWord = ? and delFlag = ?";
        User user = userDao.getUnique(hql,account,passWord,"N");
        return user;
    }
}
