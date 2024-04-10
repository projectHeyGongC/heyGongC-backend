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
            if (data.isSinglePush()) {
                // 단일 푸시 메시지 발송
                Message message;
                if (data.isSilent()) {
                    message = createSilentMessage(data.getToken(), data.getData());
                } else {
                    message = createMessage(data.getToken(), data.getBody(), data.getData());
                }

                FirebaseMessaging.getInstance().send(message);

            } else if (data.isMultiPush()) {
                // 멀티 푸시 메시지 발송
                List<Message> messages = new ArrayList<>();
                for (String token : data.getTokens()) {
                    Message message;
                    if (data.isSilent()) {
                        message = createSilentMessage(token, data.getData());
                    } else {
                        message = createMessage(token, data.getBody(), data.getData());
                    }
                    messages.add(message);
                }

                FirebaseMessaging.getInstance().sendEach(messages);

            } else {
                throw new Exception("Invalid Fcm Token");
            }
            log.info("Firebase Cloud Messaging Success");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new Exception("Firebase Cloud Messaging Failed");
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

    private static Message createSilentMessage(String token, HashMap<String, String> data) {

        Aps aps = Aps.builder().setContentAvailable(true).build();
        ApnsConfig apnsConfig = ApnsConfig.builder()
                .setAps(aps)
                .build();
        return Message.builder()
                .setToken(token)
                .setApnsConfig(apnsConfig)
                .putAllData(data)
                .build();
    }

}
