package cn.itrip.auth.service;

public interface MailService {
    public void sendActivationMail(String mailTo,String activationCode);
}
