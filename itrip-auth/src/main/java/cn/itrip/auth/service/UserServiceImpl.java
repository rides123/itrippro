package cn.itrip.auth.service;

import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.common.MD5;
import cn.itrip.common.RedisAPI;
import cn.itrip.dao.user.ItripUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private ItripUserMapper itripUserMapper;
    @Resource
    private MailService mailService;
    @Resource
    private RedisAPI redisAPI;
    @Override
    public void itriptxcreateUser(ItripUser user) throws Exception {
        //1.添加用户信息
        itripUserMapper.insertItripUser(user);
        //2.生产激活码
        String activationCode = MD5.getMd5(new Date().toLocaleString(),32);
        //3.发送邮箱
        mailService.sendActivationMail(user.getUserCode(),activationCode);
        //4.存入rides
        redisAPI.set("activation:"+user.getUserCode(),30*60,activationCode);
    }
}
