package org.flow.gateway.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.flow.gateway.common.entity.BaseEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("user_sessions")
public class UserSessionsEntity extends BaseEntity {

    @Id
    private Long sessionId;

    private Long userId;

    private String accessToken;

    private String refreshToken;

    private LocalDateTime expiration;

    private int version;

}
