package org.flow.gateway.common.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;

@Getter
@Setter
public class BaseEntity {

    private boolean useYn;

    private String createCode;

    @CreatedDate
    private LocalDateTime createDate;

    private String modifyCode;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    private String deleteCode;

    private LocalDateTime deleteDate;

    @Transient
    public void prePersist(){
        this.createDate = LocalDateTime.now();
        this.createCode = "flow-api-gateway";
        this.useYn = true;
    }

    @Transient
    public void markModified(){
        this.modifyDate = LocalDateTime.now();
        this.modifyCode = "flow-api-gateway";
    }

    @Transient
    public void markDeleted(){
        this.deleteDate = LocalDateTime.now();
        this.deleteCode = "flow-api-gateway";
        this.useYn = false;
    }

}
