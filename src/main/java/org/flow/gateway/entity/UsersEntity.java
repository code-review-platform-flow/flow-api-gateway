package org.flow.gateway.entity;

import java.util.ArrayList;
import java.util.List;
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
@Table("users")
public class UsersEntity extends BaseEntity {

    @Id
    private Long userId;

    private String email;

    private String password;

    private List<UserInfoEntity> userInfoEntities = new ArrayList<>();

    private UserSessionsEntity userSessionEntity;

    private int version;

}
