package com.KeyStone.FieldServiceProject.Security;

import java.util.Set;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.KeyStone.FieldServiceProject.ENUM.Permissions;
import com.KeyStone.FieldServiceProject.Entity.UserAuth;
import com.KeyStone.FieldServiceProject.Repository.UserAuthRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserAuthRepository userRepo;

    public CustomUserDetailsService(UserAuthRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail)
            throws UsernameNotFoundException {

        UserAuth user = userRepo.findByUserEmail(userEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + userEmail));

        Set<Permissions> perms =
                RoleBasedPermissions.getRoleBasedPermission()
                        .get(user.getRole().name());

        return User.builder()
                .username(user.getUserEmail())
                .password(user.getPassword())
                .authorities(perms.stream()
                               .map(permission ->
                                        new SimpleGrantedAuthority(
                                                permission.name()
                                        )
                                )
                                .collect(Collectors.toSet())
                )
                .build();
    }
}