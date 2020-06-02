package com.huqi.fronted.service.impl;

public class SmsService {

    //发送短信的服务
    public void sendSms(String phone, String code){

        //调用API接口发送消息
        System.out.println("手机号 : " + phone + " 发送验证码 " + code + " 成功");

    }

}
