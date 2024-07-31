package org.flow.gateway.dto.usersessions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSessionsDto {

    private Long sessionId;
    private Long userId;
    private String accessToken;
    private String refreshToken;
}
