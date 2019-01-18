package cn.itrip.auth.service;

public interface SmsService {
    public void send(String to,String templateId,String[] datas) throws Exception;
}
