package com.bozhinov.todomanagement.service;

import com.bozhinov.todomanagement.dto.JwtAuthResponse;
import com.bozhinov.todomanagement.dto.LoginDto;
import com.bozhinov.todomanagement.dto.RegisterDto;

public interface AuthService {
    String register(RegisterDto registerDto);
    JwtAuthResponse login(LoginDto loginDto);
}
