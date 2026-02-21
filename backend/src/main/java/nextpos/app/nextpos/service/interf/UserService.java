package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateUserRequest;
import nextpos.app.nextpos.model.dto.request.CreateRequest.UserRegisterRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateUserRequest;
import nextpos.app.nextpos.model.dto.request.LoginRequest;
import nextpos.app.nextpos.model.dto.request.UpdatePasswordRequest;
import nextpos.app.nextpos.model.dto.response.JwtResponse;
import nextpos.app.nextpos.model.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);

    UserResponse signup(UserRegisterRequest request, Long companyId);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(Long id, UpdateUserRequest request);

    void updatePassword(UpdatePasswordRequest request);

    void deleteUser(Long id);

    JwtResponse authenticateAndGenerateToken(LoginRequest loginRequest);
}