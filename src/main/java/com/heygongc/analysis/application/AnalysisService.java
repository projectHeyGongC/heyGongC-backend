package com.heygongc.analysis.application;

import com.heygongc.analysis.presentation.response.AnalysisDetailResponse;
import com.heygongc.analysis.presentation.response.AnalysisMainResponse;
import com.heygongc.notification.domain.entity.Notification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class AnalysisService {

    public List<AnalysisMainResponse.Notifications> makeAnalysisMainResponse(List<Notification> notifications) {

        // 1. Map 만들어서 count 계산
        LinkedHashMap<List<String>, Long> map = new LinkedHashMap<>();
        for (Notification n : notifications) {
            List<String> key = List.of(n.getDevice().getDeviceId(), n.getDevice().getDeviceName());
            if (!map.containsKey(key)) {
                map.put(key, 1L);
            } else {
                Long count = map.get(key) + 1;
                map.put(key, count);
            }
        }

        // 2. response 생성
        List<AnalysisMainResponse.Notifications> responseNotifications = new ArrayList<>();
        String returnMsg = "오늘 소리가 %d번 감지되었습니다.";
        for (List<String> keys : map.keySet()) {
            String deviceId = keys.get(0);
            String deviceName = keys.get(1);
            Long count = map.get(keys);
            String contents = String.format(returnMsg, count);

            responseNotifications.add(
                    new AnalysisMainResponse.Notifications(
                            deviceId,
                            deviceName,
                            contents
                    )
            );
        }

        return responseNotifications;
    }

    public List<AnalysisDetailResponse.Graph> makeAnalysisGraph(List<Notification> notifications) {

        // 1. 0 ~ 1440 5분 단위로 Map 생성
        LinkedHashMap<Short, Long> dateList = new LinkedHashMap<>();
        for (short s = 0; s < 1440; s += 5) {
            dateList.put(s, 0L);
        }

        // 2. Map에 count 저장
        // TODO: 로직 확인 필요
        for (Notification n : notifications) {
            LocalDateTime notificationAt = n.getNotificationAt();
            int hour = notificationAt.getHour();
            int minute = notificationAt.getMinute();
            short totalMinute = (short) ((hour * 60) + (minute % 5) * 5); // 5분 단위로 나눠서 분 단위로 변경
            dateList.put(totalMinute, dateList.get(totalMinute) + 1);
        }

        // 3. graph 생성
        List<AnalysisDetailResponse.Graph> graph = new ArrayList<>();
        for (Short totalMinute : dateList.keySet()) {
            String hour = String.valueOf(totalMinute / 60);
            String minute = String.valueOf(totalMinute % 60);
            String time = hour + ":" + minute;
            graph.add(
                    new AnalysisDetailResponse.Graph(
                            time,
                            dateList.get(totalMinute)
                    )
            );
        }

        return graph;
    }
}
