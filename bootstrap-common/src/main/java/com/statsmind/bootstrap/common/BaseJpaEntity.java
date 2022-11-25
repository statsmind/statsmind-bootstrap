package com.statsmind.bootstrap.common;

import com.statsmind.commons.Status;
import com.statsmind.commons.jpa.JpaEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@MappedSuperclass
public class BaseJpaEntity extends JpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @Comment("索引号")
    private Long id;

    @Comment("标准化状态，1表示正常，其他均为非正常")
    @Column(columnDefinition = "int not null default 1", nullable = false)
    private Integer statusId = Status.AVAILABLE;

    @Comment("记录创建人")
    @CreatedBy
    @Column
    private Long createdBy;

    @Comment("记录修改人")
    @LastModifiedBy
    @Column
    private Long updatedBy;

    @Comment("记录创建时间")
    @Column(columnDefinition = "datetime not null default CURRENT_TIMESTAMP",
        nullable = false, insertable = false, updatable = false)
    private Timestamp createdTime;

    @Comment("记录修改时间")
    @Column(columnDefinition = "datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP",
        nullable = false, insertable = false, updatable = false)
    private Timestamp updatedTime;
}
