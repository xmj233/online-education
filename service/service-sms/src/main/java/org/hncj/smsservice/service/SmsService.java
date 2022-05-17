package org.hncj.smsservice.service;

public interface SmsService {
    boolean sendSmsPhone(String phone, String code);
}
