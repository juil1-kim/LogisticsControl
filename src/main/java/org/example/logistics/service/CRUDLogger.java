package org.example.logistics.service;

import com.mongodb.client.MongoCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CRUDLogger {
    private static final Logger logger = LogManager.getLogger(CRUDLogger.class);
    private static final MongoCollection<Document> collection;

    static {
        collection = MongoDBConnection.getDatabase().getCollection("logs");
    }

    public static void log(String operationCode, String entity, String message) {
        try {
            // 현재 시간 포맷
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
            String timestamp = LocalDateTime.now().format(formatter);

            // MongoDB 로그 생성
            Document log = new Document("시간", timestamp)
                    .append("작업", operationCode) // 예: CREATE, READ, UPDATE, DELETE
                    .append("대상", entity)       // 예: 상품, 주문
                    .append("메시지", message);  // 예: 상품 추가: 샘플 상품

            // 로그 기록 사용 예시(아래 코드) - DAO 클래스 적절한 위치에 추가해야함 예: 제품 생성 메소드 마지막 부분
            // CRUDLogger.log("CREATE", "상품", "상품 추가: " + product.getName());

            collection.insertOne(log);

            // Log4j를 통한 로그 출력
            logger.info("[{}] {} - {}", operationCode, entity, message);
        } catch (Exception e) {
            logger.error("로그 저장 실패: {}", e.getMessage());
        }
    }
}


