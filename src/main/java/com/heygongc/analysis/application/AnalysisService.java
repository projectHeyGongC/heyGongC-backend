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

        // 1. 00:00 ~ 23:55까지 5분 단위로 Map 생성
        LinkedHashMap<String, Long> dateList = new LinkedHashMap<>();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j = j + 5) {
                String hour = String.valueOf(i);
                String minute = String.valueOf(j);
                String key = hour + ":" + minute;
                dateList.put(key, 0L);
            }
        }

        // 2. Map에 count 저장
        for (Notification n : notifications) {
            LocalDateTime createdAt = n.getCreated_at();
            String hour = String.valueOf(createdAt.getHour());
            int intMinute = createdAt.getMinute() / 5; // 5분 단위로 나눠서
            String minute = String.valueOf(intMinute);
            String key = hour + ":" + minute;

            dateList.put(key, dateList.get(key) + 1); // 1씩 증가
        }

        // 3. graph 생성
        List<AnalysisDetailResponse.Graph> graph = new ArrayList<>();
        for (String key : dateList.keySet()) {
            graph.add(
                    new AnalysisDetailResponse.Graph(
                            key,
                            dateList.get(key)
                    )
            );
        }

        return graph;
    }
}
