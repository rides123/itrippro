package cn.itrip.auth.service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("mailService")
public class MailServiceImpl implements MailService {
    @Resource
    private SimpleMailMessage activationMailMessage;

    @Resource
    private MailSender mailSender;
    @Override
    public void sendActivationMail(String mailTo, String activationCode) {
        activationMailMessage.setTo(mailTo);
        activationMailMessage.setText("您的激活码是："+activationCode+"；请在三十分钟之内激活您的账号");
        mailSender.send(activationMailMessage);
    }
}
