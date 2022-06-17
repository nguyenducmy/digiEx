package com.supertomato.restaurant.service;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.enums.UserRole;
import com.supertomato.restaurant.entity.User;
import com.supertomato.restaurant.repository.UserRepository;
import com.supertomato.restaurant.repository.specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author DiGiEx
 */
@Service
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;
    final UserSpecification userSpecification;

    public UserServiceImpl(
            UserRepository userRepository,
            UserSpecification userSpecification) {

        this.userRepository = userRepository;
        this.userSpecification = userSpecification;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUser(String id) {
        return userRepository.findOneById(id);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public User getById(String id) {
        return userRepository.findByIdAndStatus(id, AppStatus.ACTIVE);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmailAndStatusNot(email, AppStatus.INACTIVE );
    }

    @Override
    public Page<User> getPageUser(UserRole userRole, List<String> userIds, String sortField, boolean ascSort, int pageNumber, int pageSize) {
        Specification<User> specification = userSpecification.getPageUser(userRole,userIds, sortField, ascSort);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return userRepository.findAll(specification, pageable);

    }
    @Override
    public User getByActiveCode(String activeCode){
        return userRepository.findByActiveCode(activeCode);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getByUserId(String id) {
        return userRepository.findByIdAndStatusNot(id, AppStatus.INACTIVE);
    }
}
