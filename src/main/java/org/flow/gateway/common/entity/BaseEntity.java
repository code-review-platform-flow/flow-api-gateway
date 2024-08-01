package org.flow.gateway.common.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Column(name = "use_yn", nullable = false)
    private boolean useYn;

    @Column(name = "create_code", updatable = false)
    private String createCode;

    @CreatedDate
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @Column(name = "modify_code")
    private String modifyCode;

    @Column(name = "modify_date")
    private LocalDateTime modifyDate;

    @Column(name = "delete_code")
    private String deleteCode;

    @Column(name = "delete_date")
    private LocalDateTime deleteDate;

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
        this.createCode = "flow-api-gateway";
        this.useYn = true;
    }

    public void markModified(){
        this.modifyDate = LocalDateTime.now();
        this.modifyCode = "flow-api-gateway";
    }

    public void markDeleted(){
        this.deleteDate = LocalDateTime.now();
        this.deleteCode = "flow-api-gateway";
        this.useYn = false;
    }
}
