package com.supertomato.restaurant.repository.specification;


import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.enums.UserRole;
import com.supertomato.restaurant.common.util.Constant;
import com.supertomato.restaurant.entity.User;
import com.supertomato.restaurant.entity.UserOutlet;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserSpecification {

    public Specification<User> getPageUser(UserRole userRole, List<String> userIds, String sortField, boolean ascSort) {
        return (Root<User> userRoot, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            cq.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.notEqual(userRoot.get("status"), AppStatus.INACTIVE));

            if (UserRole.COMPANY_ADMIN.equals(userRole)){
                predicates.add(cb.notEqual(userRoot.get("userRole"), UserRole.SUPER_ADMIN));
            }
            if(userIds != null && !userIds.isEmpty()) {
                predicates.add(userRoot.get("id").in(userIds));
            }

            Path orderClause;
            switch (sortField) {
                case Constant.SORT_BY_NAME:
                    orderClause = userRoot.get("firstName");
                    break;
                case Constant.SORT_BY_EMAIL:
                    orderClause = userRoot.get("email");
                    break;
                default:
                    orderClause = userRoot.get("createdDate");
                    break;
            }

            if (ascSort) {
                cq.orderBy(cb.asc(orderClause));
            } else {
                cq.orderBy(cb.desc(orderClause));
            }

            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
