package cn.itrip.auth.service;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("smsService")
public class SmsServiceImpl implements SmsService {

    @Override
    public void send(String to, String templateId, String[] datas) throws  Exception {
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init("app.cloopen.com","8883");
        sdk.setAccount("8aaf07086812057f016835479b5f12a7","b81440b67fd345179a5ddefd554f5854");
        sdk.setAppId("8aaf07086812057f016835479bb012ad");
        HashMap result =sdk.sendTemplateSMS(to,templateId,datas);
        if("000000".equals(result.get("statusCode"))){
            System.out.println("短信发送成功");
        }else{
            throw new Exception(result.get("statusCode").toString()+result.get("statusMsg").toString());
        }
    }
}
