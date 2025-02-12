package com.hotel.diamante.service.interfac;

import com.hotel.diamante.dto.LoginRequest;
import com.hotel.diamante.dto.Response;
import com.hotel.diamante.entity.User;

public interface IUserService {
Response register(User loginRequest);
Response login(LoginRequest loginRequest);
Response getAllUsers();
Response getUserBookingHistory(String userId);
Response getUserById(String userId);
Response deleteUser(String userId);
Response getMyInfo(String email);


}
