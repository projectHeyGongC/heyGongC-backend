package com.heygongc.analysis.application;

import com.heygongc.analysis.presentation.response.AnalysisDetailResponse;
import com.heygongc.analysis.presentation.response.AnalysisMainResponse;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.notification.domain.entity.Notification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AnalysisService {

    public List<AnalysisMainResponse.Notifications> makeAnalysisMain(List<Device> devices, List<Notification> notifications) {

        // 1. count 계산할 map, device명 관리할 map 생성
        LinkedHashMap<Long, Long> countMap = new LinkedHashMap<>();
        Map<Long, String[]> deviceMap = new HashMap<>();

        for (Device d : devices) {
            deviceMap.put(d.getDeviceSeq(), new String[]{d.getDeviceId(), d.getDeviceName()});
            countMap.put(d.getDeviceSeq(), 0L);
        }

        // 2. count 계산
        for (Notification n : notifications) {
            countMap.put(n.getDevice().getDeviceSeq(), countMap.get(n.getDevice().getDeviceSeq()) + 1);
        }

        // 3. response 생성
        List<AnalysisMainResponse.Notifications> response = new ArrayList<>();
        String returnMsg = "오늘 소리가 %d번 감지되었습니다.";
        String noContentMsg = "아무런 움직임이 없었습니다.";
        for (Long deviceSeq : countMap.keySet()) {
            String deviceId = deviceMap.get(deviceSeq)[0];
            String deviceName = deviceMap.get(deviceSeq)[1];
            Long count = countMap.get(deviceSeq);
            String contents;
            if (count > 0) {
                contents = String.format(returnMsg, count);
            } else {
                contents = noContentMsg;
            }

            response.add(
                    new AnalysisMainResponse.Notifications(
                            deviceId,
                            deviceName,
                            contents
                    )
            );
        }

        return response;
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
            short totalMinute = (short) ((hour * 60) + (minute / 5 * 5)); // 5분 단위로 나눠서 분 단위로 변경
            dateList.put(totalMinute, dateList.get(totalMinute) + 1);
        }

        // 3. graph 생성
        List<AnalysisDetailResponse.Graph> graph = new ArrayList<>();
        for (Short totalMinute : dateList.keySet()) {
            String hour = String.format("%02d", totalMinute / 60);
            String minute = String.format("%02d", totalMinute % 60);
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
