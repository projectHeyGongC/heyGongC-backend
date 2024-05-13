package com.heygongc.notification.presentation;

import com.heygongc.common.ControllerTest;
import com.heygongc.notification.application.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
//@WebMvcTest(value = NotificationController.class,
//        excludeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebMvcConfig.class})}
//)
class NotificationControllerTest extends ControllerTest {

    @MockBean
    private NotificationService notificationService;

    @Test
    @DisplayName("사용자의 알림 목록을 조회한다")
    public void getAllNotifications() throws Exception {
//        Notification notification = 알림();
//        List<Notification> notifications = List.of(notification);
//        List<NotificationResponse> response = List.of(new NotificationResponse("거실에서 소리가 감지되었습니다.", notification.getNotificationAt()));
        given(notificationService.getNotifications(any())).willReturn(Collections.emptyList());


        mockMvc.perform(
                get("/v1/notifications")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                ).andExpect(status().isOk());
//                .andExpect(content().json(objectMapper.writeValueAsString(response)));

    }
}