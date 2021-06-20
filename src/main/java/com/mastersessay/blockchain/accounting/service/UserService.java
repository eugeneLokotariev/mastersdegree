package com.mastersessay.blockchain.accounting.service;

import com.mastersessay.blockchain.accounting.consts.UserRole;
import com.mastersessay.blockchain.accounting.dto.request.user.LoginRequest;
import com.mastersessay.blockchain.accounting.dto.request.user.SignupRequest;
import com.mastersessay.blockchain.accounting.dto.response.user.JwtResponse;
import com.mastersessay.blockchain.accounting.dto.response.user.MessageResponse;
import com.mastersessay.blockchain.accounting.model.user.BlockchainAccountingUserDetails;
import com.mastersessay.blockchain.accounting.model.user.Role;
import com.mastersessay.blockchain.accounting.model.user.User;
import com.mastersessay.blockchain.accounting.repository.RoleRepository;
import com.mastersessay.blockchain.accounting.repository.UserRepository;
import com.mastersessay.blockchain.accounting.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.RolesName.*;
import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Security.*;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    private AuthenticationManager authenticationManager;
    private PasswordEncoder encoder;
    private JwtUtils jwtUtils;

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Transactional
    public JwtResponse processUserSignIn(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        BlockchainAccountingUserDetails userDetails =
                (BlockchainAccountingUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return JwtResponse
                .builder()
                .token(jwt)
                .type("Bearer")
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
    }

    @Transactional
    public MessageResponse registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new IllegalArgumentException(USER_IS_ALREADY_TAKEN_ERROR_MESSAGE);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException(EMAIL_IS_ALREADY_IN_USE_ERROR_MESSAGE);
        }

        User newUser = User
                .builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .isEnabled(true)
                .build();

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            throw new RuntimeException(ROLE_NOT_FOUND_ERROR_MESSAGE);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case CATALOG_ADMIN:
                        Role catalogAdminRole = roleRepository
                                .findByName(UserRole.ROLE_CATALOG_ADMIN)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR_MESSAGE));
                        roles.add(catalogAdminRole);

                        break;
                    case ORDER_ADMIN:
                        Role orderAdminRole = roleRepository
                                .findByName(UserRole.ROLE_ORDER_ADMIN)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR_MESSAGE));
                        roles.add(orderAdminRole);

                        break;
                    case MAINTENANCE_ADMIN:
                        Role maintenanceAdminRole = roleRepository
                                .findByName(UserRole.ROLE_MAINTENANCE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR_MESSAGE));
                        roles.add(maintenanceAdminRole);

                        break;
                    case USER_ADMIN:
                        Role userAdminRole = roleRepository
                                .findByName(UserRole.ROLE_USER_ADMIN)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR_MESSAGE));
                        roles.add(userAdminRole);

                        break;
                    case USER_PLAIN:
                        Role plainUserRole = roleRepository
                                .findByName(UserRole.ROLE_USER_PLAIN)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR_MESSAGE));
                        roles.add(plainUserRole);

                        break;
                }
            });
        }

        if (roles.isEmpty()) {
            throw new IllegalArgumentException(ROLE_NOT_FOUND_ERROR_MESSAGE);
        }

        newUser.setRoles(roles);
        newUser.setCreatedWhen(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")));

        userRepository.save(newUser);

        return MessageResponse
                .builder()
                .message(SUCCESSFUL_REGISTRATION_MESSAGE)
                .build();
    }

    @Autowired
    public UserService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder encoder,
                       JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }
}
