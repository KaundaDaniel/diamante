package com.hotel.diamante.service.impl;

import com.hotel.diamante.dto.BookingDTO;
import com.hotel.diamante.dto.LoginRequest;
import com.hotel.diamante.dto.Response;
import com.hotel.diamante.dto.UserDTO;
import com.hotel.diamante.entity.User;
import com.hotel.diamante.exception.CustomerException;
import com.hotel.diamante.repository.UserRepository;
import com.hotel.diamante.service.interfac.IUserService;
import com.hotel.diamante.utils.JWTUtils;
import com.hotel.diamante.utils.Utils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Response register(User user) {
        Response response = new Response();
        try {
            if(user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }
            if(userRepository.existsByEmail(user.getEmail())) {
                throw new CustomerException(user.getEmail() + "Email already exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User userSave=userRepository.save(user);
            UserDTO userDTO= Utils.mapUserEntityToUserDTO(userSave);


        }catch (CustomerException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Ocorreu um erro  " + e.getMessage());
        }
        return response ;
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var user=userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()->new CustomerException("Usuário não encontrado"));
               String token=jwtUtils.generateToken(user);
               response.setStatusCode(200);
               response.setToken(token);
               response.setRole(user.getRole());
               response.setExpirationTime("7 dias");
               response.setMessage("successful");
               response.setUser(Utils.mapUserEntityToUserDTO(user));

        }catch (CustomerException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Ocorreu um erro durante o login  " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response = new Response();
        try {
            List<User>userList= userRepository.findAll();
            List<UserDTO> userDTOList=Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("Obtido com sucesso");
            response.setUserDTOList(userDTOList);
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Erro ao óbter todos os Usuários: "+e.getMessage());


        }
        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {
        Response response = new Response();
        try {
            User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new CustomerException("Usuário não encontrado"));
            UserDTO userDTO= Utils.mapUserEntityToUserPlusBookingsAndRoom(user);
            response.setStatusCode(200);
            response.setMessage("Obtido com sucesso");
            response.setUser(userDTO);

        }catch (CustomerException e) {
        response.setStatusCode(404);
        response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Ocorreu um erro ao obter o histórico de reservas do Usuário: "+e.getMessage());
        }

        return response;
    }

    @Override
    public Response getUserById(String userId) {
        Response response = new Response();
        try {
            User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new CustomerException("Usuário não encontrado"));
            UserDTO userDTO= Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Obtido com sucesso");
            response.setUser(userDTO);

        }catch (CustomerException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Ocorreu um erro ao obter o histórico de reservas do Usuário: "+e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        Response response = new Response();
        try {
            userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new CustomerException("Usuário não encontrado"));
            userRepository.deleteById(Long.parseLong(userId));
            response.setStatusCode(200);
            response.setMessage("Usuário apagado com sucesso");


        }catch (CustomerException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Ocorreu um erro ao obter o histórico de reservas do Usuário: "+e.getMessage());
        }

        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response = new Response();
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomerException("Usuário não encontrado"));
            UserDTO userDTO= Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Obtido com sucesso");
            response.setUser(userDTO);

        }catch (CustomerException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Ocorreu um erro ao obter o histórico de reservas do Usuário: "+e.getMessage());
        }

        return response;
    }
}
