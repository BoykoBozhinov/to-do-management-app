package com.bozhinov.todomanagement.service.impl;

import com.bozhinov.todomanagement.dto.JwtAuthResponse;
import com.bozhinov.todomanagement.dto.LoginDto;
import com.bozhinov.todomanagement.dto.RegisterDto;
import com.bozhinov.todomanagement.entity.RoleEntity;
import com.bozhinov.todomanagement.entity.UserEntity;
import com.bozhinov.todomanagement.exception.TodoAPIException;
import com.bozhinov.todomanagement.repository.RoleRepository;
import com.bozhinov.todomanagement.repository.UserRepository;
import com.bozhinov.todomanagement.security.JwtTokenProvider;
import com.bozhinov.todomanagement.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.bozhinov.todomanagement.constants.Constants.*;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public String register(RegisterDto registerDto) {

        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, USER_EXIST_ERROR);
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, EMAIL_EXIST_ERROR);
        }

        UserEntity user = new UserEntity();

        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity role = roleRepository.findByName("ROLE_USER");
        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);

        return SUCCESSFUL_REGISTRATION;
    }

    @Override
    public JwtAuthResponse login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmailOrUsername(),
                loginDto.getPassword()
        ));

        String token = tokenProvider.generateToken(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Optional<UserEntity> userOptional = userRepository.findByUsernameOrEmail(loginDto.getEmailOrUsername(), loginDto.getEmailOrUsername());

        String role = null;
        if (userOptional.isPresent()) {
            UserEntity loggedInUser = userOptional.get();
            Optional<RoleEntity> optionalRole = loggedInUser.getRoles().stream().findFirst();

            if (optionalRole.isPresent()) {
                RoleEntity userRole = optionalRole.get();
                role = userRole.getName();
            }
        }

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setRole(role);
        jwtAuthResponse.setAccessToken(token);

        return jwtAuthResponse;
    }
}
