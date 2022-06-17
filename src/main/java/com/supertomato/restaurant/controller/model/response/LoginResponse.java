package com.supertomato.restaurant.controller.model.response;

import com.supertomato.restaurant.common.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DiGiEx
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse   {
    private String accessToken;
    private Long expireTime;
    private UserRole role;
}
