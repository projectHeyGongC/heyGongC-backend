package com.heygongc.device.domain;

import com.heygongc.user.domain.User;

public interface CustomDeviceRepository {
    Device findDeviceBySeqAndUser(Long deviceSeq, User user);
}