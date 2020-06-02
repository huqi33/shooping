package com.huqi.fronted.domain;

import java.time.LocalDateTime;

public class SmsCode {

    private String phone;
    private String code;
    private LocalDateTime expire;

    //查看验证码是否过期
    public boolean isExpire(){
        return LocalDateTime.now().isBefore(expire);
    }

    public SmsCode(String phone, String code, int expire) {
        this.phone = phone;
        this.code = code;
        this.expire = LocalDateTime.now().plusSeconds(expire);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpire() {
        return expire;
    }

    public void setExpire(LocalDateTime expire) {
        this.expire = expire;
    }
}
