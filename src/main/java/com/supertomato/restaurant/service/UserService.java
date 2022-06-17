package com.supertomato.restaurant.service;

import com.supertomato.restaurant.common.enums.UserRole;
import com.supertomato.restaurant.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author DiGiEx
 */
public interface UserService {

    User save(User user);

    User getUser(String id);

    void deleteUser(User user);

    User getById(String id);

    User getByEmail(String email);

    Page<User> getPageUser(UserRole userRole, List<String> companyIds, String sortField, boolean ascSort, int pageNumber, int pageSize);

    User getByActiveCode(String activeCode);

    User updateUser(User user);

    User getByUserId(String id);

}
