package com.krab51.webapp.services.userdetails;

import com.krab51.webapp.domain.Operator;
import com.krab51.webapp.repositories.OperatorRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.krab51.webapp.configuration.WebSecurityConfig.ADMIN;
import static com.krab51.webapp.configuration.WebSecurityConfig.OPERATOR;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final OperatorRepository userRepository;

//    @Value("${spring.security.user.name}")
    private String adminUserName;
//    @Value("${spring.security.user.password}")
    private String adminPassword;


    public CustomUserDetailsService(OperatorRepository userRepository,
                                    @Value("${spring.security.user.name}") String adminUserName,
                                    @Value("${spring.security.user.password}") String adminPassword) {
        this.userRepository = userRepository;
        this.adminUserName = adminUserName;
        this.adminPassword = new BCryptPasswordEncoder().encode(adminPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (adminUserName.equals(username)) {
            return new CustomUserDetails(null, username, adminPassword, List.of(new SimpleGrantedAuthority("ROLE_" + ADMIN)));
        } else {
            Operator user = userRepository.findUserByUserName(username);
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (user.role.id == 1L) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + OPERATOR));
            }
            return new CustomUserDetails(user.id.intValue(), username, user.password, authorities);
        }

//        else {
//            User user = userRepository.findUserByLoginAndIsDeletedFalse(username);
//            List<GrantedAuthority> authorities = new ArrayList<>();
//
//            //ROLE_OPERATOR
//            authorities.add(new SimpleGrantedAuthority(user.role.id == 1L ? "ROLE_" + UserRolesConstants.USER : "ROLE_" + UserRolesConstants.OPERATOR));
//
//            return new CustomUserDetails(user.id.intValue(), username, user.password, authorities);
//        }
    }
}
//public final class CustomUserDetailsServiceBuilder {
//    private CustomUserDetails customUserDetails;
//
//    public CustomUserDetailsServiceBuilder setPageCount(CustomUserDetails customUserDetails) {
//        this.customUserDetails = customUserDetails;
//        return this;
//    }
//
//    public CustomUserDetailsService createCustomUserDetailsService() {
//        return new CustomUserDetailsService((OperatorRepository) customUserDetails);
//    }
//}
