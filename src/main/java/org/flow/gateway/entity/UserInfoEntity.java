package org.flow.gateway.entity;

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
@Table("user_info")
public class UserInfoEntity extends BaseEntity {

    @Id
    private Long userInfoId;

    private Long userId; // Foreign key reference to UsersEntity

    private String studentNumber;

    private String role;

    private int version;

}