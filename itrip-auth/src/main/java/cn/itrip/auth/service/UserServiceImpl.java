package cn.itrip.auth.service;

import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.common.MD5;
import cn.itrip.common.RedisAPI;
import cn.itrip.dao.user.ItripUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private ItripUserMapper itripUserMapper;
    @Resource
    private MailService mailService;
    @Resource
    private RedisAPI redisAPI;
    @Resource
    private SmsService smsService;
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

    @Override
    public boolean activate(String mail, String code) throws Exception {
        //1.验证激活码
        String  value = redisAPI.get("activation:"+mail);
        if(value.equals(code)){
            Map<String,Object> map = new HashMap<>();
            map.put("userCode",mail);

            List<ItripUser> list = itripUserMapper.getItripUserListByMap(map);
            if (list.size() > 0) {

                ItripUser user = list.get(0);
                user.setActivated(1);
                user.setFlatID(user.getId());
                user.setUserType(0);
                if(itripUserMapper.updateItripUser(user)>0){
                    return true;
                }else{
                    return false;
                }

            }
        }
        //2.更新用户
        return false;
    }
    @Override
    public ItripUser findUserByUserCode(String userCode) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("userCode",userCode);
        List<ItripUser> list = itripUserMapper.getItripUserListByMap(map);
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public void itriptxCreateUserByPhone(ItripUser user) throws Exception {
        //1.创建用户
        itripUserMapper.insertItripUser(user);
        //2.生产验证码
        int value = MD5.getRandomCode();
        //3.发送验证码
        smsService.send(user.getUserCode(),"1",new String[]{String.valueOf(value),"1"});
        //4.存到redis
        redisAPI.set("activation:"+user.getUserCode(),5*60,String.valueOf(value));
    }

    /**
     * 用户名密码的登录
     * @param userCode
     * @param userPasswrod
     * @return
     * @throws Exception
     */
    @Override
    public ItripUser login(String userCode, String userPasswrod) throws Exception {
        ItripUser user = this.findUserByUserCode(userCode);
        if(null!=user && user.getUserPassword().equals(userPasswrod)){
            if(user.getActivated()!=1)
                throw new Exception("用户未激活");
            return user;
        }else
            return null;
    }

    public boolean validatePhone(String phoneNum,String code) throws Exception{
        //1.比对验证码
        String key = "activation:"+phoneNum;
        String value = redisAPI.get(key);
        if(null!=value && value.equals(code)){
            ItripUser user = this.findUserByUserCode(phoneNum);
            if(null!=user){
                user.setActivated(1);
                user.setFlatID(user.getId());
                user.setUserType(0);
                itripUserMapper.updateItripUser(user);
                return true;
            }
        }
        //2.更新用户激活状态
        return false;
    }

}
