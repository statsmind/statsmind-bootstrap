package com.statsmind.bootstrap.common.entity;

import com.statsmind.bootstrap.common.BaseJpaEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "user", uniqueConstraints = {
    @UniqueConstraint(name = "uni_mobile", columnNames = {"mobile"})
})
public class UserEntity extends BaseJpaEntity {
    @Column(length = 16, nullable = false)
    @Comment("用户手机号")
    private String mobile;

    @Column(length = 255, nullable = false)
    @Comment("混淆之后的密码")
    private String passwordHash;

    @Column(length = 32)
    @Comment("用户显示名，昵称")
    private String displayName;
}
