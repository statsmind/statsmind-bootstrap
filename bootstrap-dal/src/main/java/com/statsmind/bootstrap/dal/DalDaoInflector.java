package com.statsmind.bootstrap.dal;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class DalDaoInflector {
    @NonNull
    private ApplicationContext applicationContext;

    public DalUserDao user() {
        return applicationContext.getBean(DalUserDao.class);
    }

}
