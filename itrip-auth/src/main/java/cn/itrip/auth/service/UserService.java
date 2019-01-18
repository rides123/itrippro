package cn.itrip.auth.service;

import cn.itrip.beans.pojo.ItripUser;

public interface UserService {
    public void itriptxcreateUser(ItripUser user) throws Exception;
    public boolean activate(String mail,String code) throws  Exception;
    public ItripUser findUserByUserCode(String userCode) throws Exception;
    public boolean validatePhone(String phoneNum,String code) throws Exception;
    public void itriptxCreateUserByPhone(ItripUser user) throws Exception;
    public ItripUser login(String userCode,String userPasswrod) throws Exception;
}
