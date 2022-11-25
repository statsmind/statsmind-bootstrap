package com.statsmind.bootstrap.dal;

import com.querydsl.jpa.impl.JPAQuery;
import com.statsmind.bootstrap.common.entity.QUserEntity;
import com.statsmind.bootstrap.common.entity.UserEntity;
import com.statsmind.commons.jpa.JpaEntityDao;
import org.springframework.stereotype.Repository;

@Repository
class DalUserDao extends JpaEntityDao<UserEntity, Long> {
    private static final QUserEntity Q = QUserEntity.userEntity;

    @Override
    public JPAQuery<UserEntity> find() {
        return super.find();
    }

    public JPAQuery<UserEntity> findByMobile(String mobile) {
        return find().where(Q.mobile.eq(mobile));
    }
}
