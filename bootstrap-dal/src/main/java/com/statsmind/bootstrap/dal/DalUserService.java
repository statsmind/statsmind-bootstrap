package com.statsmind.bootstrap.dal;

import com.statsmind.bootstrap.common.entity.UserEntity;
import com.statsmind.bootstrap.dal.DalDaoInflector;
import com.statsmind.bootstrap.dal.DalUserDao;
import com.statsmind.commons.security.SM3PasswordEncoder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DalUserService {
    @NonNull
    private PasswordEncoder passwordEncoder;
    @NonNull
    private DalDaoInflector dalDaoInflector;

    @Transactional
    public UserEntity createUser(String mobile, String password) {
        UserEntity user = new UserEntity()
            .setMobile(mobile)
            .setPasswordHash(passwordEncoder.encode(password));

        DalUserDao dalUserDao = dalDaoInflector.user();
        return dalUserDao.save(user);
    }

    public UserEntity getUserById(Long userId) {
        return dalDaoInflector.user().findById(userId).orElse(null);
    }

    public UserEntity getUserByMobile(String mobile) {
        return dalDaoInflector.user().findByMobile(mobile).fetchOne();
    }
}
