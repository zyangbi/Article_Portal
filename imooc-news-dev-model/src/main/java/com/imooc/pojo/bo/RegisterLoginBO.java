package com.imooc.pojo.bo;

import javax.validation.constraints.NotBlank;

public class RegisterLoginBO {

    @NotBlank(message = "mobile number cannot be blank")
    private String mobile;
    @NotBlank(message = "SMS code cannot be blank")
    private String smsCode;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    @Override
    public String toString() {
        return "RegisterLoginBO{" +
                "mobile='" + mobile + '\'' +
                ", smsCode='" + smsCode + '\'' +
                '}';
    }

}
