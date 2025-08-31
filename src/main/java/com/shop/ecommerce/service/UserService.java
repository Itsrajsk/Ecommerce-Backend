package com.shop.ecommerce.service;

import com.shop.ecommerce.exception.UserException;
import com.shop.ecommerce.model.User;

public interface UserService {
    User findUserById(Long userId) throws UserException;

    User findUserProfileByJwt(String jwt) throws UserException;
}
