package com.zyq.frechwind.user.service;

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
        User user = userAccount(userName);
        if (user == null){
            user = new User();
            user.setAccount(userName);
            user.setDelFlag("N");
            user.setUserName(userName);
            user.setPassWord(password);
            user.setEmail(email);
            user = userDao.create(user);
        }
        return user;
    }

    public User wechatCreate(String openId, String nickName, String headImgUrl){
        User user = userOpenId(openId);
        if (user == null){
            user.setOpenId(openId);
            user.setDelFlag("N");
            user.setNickName(nickName);
            user.setHeadImgUrl(headImgUrl);
            user = userDao.create(user);
        }
        return user;
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
     * 账号检测
     * @param openId
     * @return
     */
    public User userOpenId(String openId){
        String hql = "from User where openid=? and delFlag=?";
        User user = userDao.getUnique(hql,openId,"N");
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
