package com.heygongc.global.type;

public enum FcmActionType {
    // 카메라앱
    QR_CODE("QR코드"),
    SOUND_SENSING("소리감지모드"),
    SENSITIVITY("민감도"),
    CAMERA_ORIENTATION("카메라방향"),
    STREAM("스트리밍"),
    CAMERA("카메라"),
    FLASH("플래시"),
    SPEAKING("말하기"),
    LOW_LIGHT("저조도"),
    REMOTE_EXECUTION("원격실행"),
    REMOTE_SHUTDOWN("원격종료"),

    // 메인앱
    SOUND_ALERT("소리알림")
    ;

    private final String body;

    FcmActionType(String body) {
        this.body = body;
    }

    public String body() {
        return body;
    }
}
