//package com.example.appointmentsystem.service;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.example.appointmentsystem.entity.User;
//import com.example.appointmentsystem.interfaces.NotificationSender;
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;
//
//import jakarta.annotation.PostConstruct;
//
//@Service
//public class SmsNotificationSender implements NotificationSender {
//
//    @Value("${twilio.account.sid}")
//    private String accountSid;
//
//    @Value("${twilio.auth.token}")
//    private String authToken;
//
//    @Value("${twilio.from.number}")
//    private String fromNumber;
//
//    @PostConstruct
//    public void init() {
//        Twilio.init(accountSid, authToken);
//    }
//
//    @Override
//    public void send(String mobileNumber, String message) {
//        System.out.println("Sending REAL SMS to " + mobileNumber);
//
//        Message.creator(
//                new PhoneNumber(mobileNumber),
//                new PhoneNumber(fromNumber),
//                message
//        ).create();
//    }
//}
//
//
//
//
