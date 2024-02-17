package com.heygongc.global.infra;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class FirebaseCloudMessaging {

    private static final Logger log = LoggerFactory.getLogger(FirebaseCloudMessaging.class);

    private static class NotificationUtilHolder {
        static FirebaseCloudMessaging instance = new FirebaseCloudMessaging();
    }

    public static FirebaseCloudMessaging getInstance() {
        return NotificationUtilHolder.instance;
    }

    @PostConstruct
    public void init() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource("firebase/heygongc-firebase-sdk.json").getInputStream())
                    .createScoped(Collections.singletonList("https://www.googleapis.com/auth/firebase.messaging"));

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 단일 푸시 메세지 발송
     * @param token 푸시토큰
     * @param body 푸시 메세지
     * @param data 추가 데이터
     */
    public void sendMessage(String token, String body, HashMap<String, String> data) {
        log.info("Push Payload Checked: {}", data);

        try {
            Message message = createMessage(token, body, data);
            FirebaseMessaging.getInstance().send(message);
            log.info("Firebase Cloud Messaging Success");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 멀티 푸시 메세지 발송
     * @param tokens 푸시토큰 리스트
     * @param body 푸시 메세지
     * @param data 추가 데이터
     */
    public void sendMessage(List<String> tokens, String body, HashMap<String, String> data) {
        log.info("Push Payload Checked: {}", data);

        try {
            List<Message> messages = new ArrayList<>();

            for (String token : tokens) {
                Message message = createMessage(token, body, data);
                messages.add(message);
                log.info("Firebase Cloud Messaging Success");
            }

            FirebaseMessaging.getInstance().sendEach(messages);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private static Message createMessage(String token, String body, HashMap<String, String> data) {
        Notification notification = Notification.builder()
                .setTitle(null)
                .setBody(body)
                .build();

        // 아이폰 소리지정 (안드로이드는 따로 설정 안함)
        Aps aps = Aps.builder().setSound("default").build();
        ApnsConfig apnsConfig = ApnsConfig.builder().setAps(aps).build();

        return Message.builder()
                .setToken(token)
                .setApnsConfig(apnsConfig)
                .setNotification(notification)
                .putAllData(data)
                .build();
    }

}
