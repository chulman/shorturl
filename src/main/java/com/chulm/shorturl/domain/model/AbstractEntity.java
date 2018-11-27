package com.chulm.shorturl.domain.model;

import com.chulm.shorturl.util.LocalDateTimePersistenceConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//JPA Entity 클래스들이 이 클래스를 상속할 경우 필드들을 컬럼으로 인식하도록 설정
@MappedSuperclass
//AuditingEntityListener 를 통해 Entity Persist 시에 시간 정보, 생성자 정보 등을 자동으로 넣을 수 있다.
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    @CreatedDate
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    @Column(updatable = false, nullable = false)
    protected LocalDateTime createdAt = LocalDateTime.now();

    public String getCreatedAt() {
        createdAt = createDate();
        return getCreatedAt(createdAt, "yyyy.MM.dd HH:mm:ss");
    }

    public String getCreatedAt(LocalDateTime dateTime, String format) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public abstract LocalDateTime createDate();
}
