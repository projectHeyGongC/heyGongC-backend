package com.heygongc.global.infra;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.heygongc.global.type.OsType;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class FirebaseCloudMessaging {

    private static final Logger log = LoggerFactory.getLogger(FirebaseCloudMessaging.class);

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
     * 푸시 메시지 발송
     * @param data 푸시 데이터(토큰, 메시지, 데이터)
     */
    public void sendMessage(FirebaseData data) throws Exception {
        log.info("Push Payload Checked: {}", data.getData());

        try {
            List<Message> messages = new ArrayList<>();
            for (String token : data.getTokens()) {
                Message message = null;
                if (data.isSilent()) {
                    if (OsType.AOS.equals(data.getOsType())) {
                        message = createSilentMessageByAos(token, data.getData());
                    } else if (OsType.IOS.equals(data.getOsType())){
                        message = createSilentMessageByIos(token, data.getTitle(), data.getBody(), data.getData());
                    }
                } else {
                    message = createMessage(token, data.getTitle(), data.getBody(), data.getData());
                }
                messages.add(message);
            }
            FirebaseMessaging.getInstance().sendEach(messages);
            log.info("Firebase Cloud Messaging Success");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new Exception("Firebase Cloud Messaging Failed");
        }
    }

    private static Message createMessage(String token, String title, String body, HashMap<String, String> data) {
        // 아이폰 소리지정
        Aps aps = Aps.builder().setSound("default").build();
        ApnsConfig apnsConfig = ApnsConfig.builder().setAps(aps).build();

        // 안드로이드 우선순위 지정
        AndroidConfig androidConfig = AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH)
                .build();

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        return Message.builder()
                .setToken(token)
                .setAndroidConfig(androidConfig)
                .setApnsConfig(apnsConfig)
                .setNotification(notification)
                .putAllData(data)
                .build();
    }

    private static Message createSilentMessageByAos(String token, HashMap<String, String> data) {
        return Message.builder()
                .setToken(token)
                .putAllData(data)
                .build();
    }

    private static Message createSilentMessageByIos(String token, String title, String body, HashMap<String, String> data) {
        Aps aps = Aps.builder().setContentAvailable(true).build();
        // iOS 13부터 priority, push-type 설정해야 background push 가능
        ApnsConfig apnsConfig = ApnsConfig.builder().setAps(aps)
                .putHeader("apns-priority", "5")
                .putHeader("apns-push-type", "background")
                .build();

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        return Message.builder()
                .setToken(token)
                .setApnsConfig(apnsConfig)
                .setNotification(notification)
                .putAllData(data)
                .build();
    }
}
