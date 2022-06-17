package com.supertomato.restaurant.repository;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.enums.UserRole;
import com.supertomato.restaurant.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;

/**
 * @author DiGiEx Group
 */
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    User findByIdAndStatus(String id, AppStatus status);

    User findByEmailAndStatusNot(String email, AppStatus status);

    User findOneById(String id);

    User findByIdAndStatusNot(String id, AppStatus status);

    User findByActiveCode(String activeCode);


}
