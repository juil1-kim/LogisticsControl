package org.example.logistics.logViewer;

import lombok.Data;

@Data
public class LogViewerVO {
    private String timestamp; // 로그 생성 시간
    private String operation; // 작업 코드
    private String entity;    // 대상 엔터티
    private String message;   // 메시지 내용

    public LogViewerVO(String timestamp, String operation, String entity, String message) {
        this.timestamp = timestamp;
        this.operation = operation;
        this.entity = entity;
        this.message = message;
    }
}

