package nextpos.app.nextpos.controller.people;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import nextpos.app.nextpos.model.dto.request.LoginRequest;
import nextpos.app.nextpos.model.dto.request.UpdatePasswordRequest;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateUserRequest;
import nextpos.app.nextpos.model.dto.request.CreateRequest.UserRegisterRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateUserRequest;
import nextpos.app.nextpos.model.dto.response.UserResponse;
import nextpos.app.nextpos.model.dto.response.JwtResponse;
import nextpos.app.nextpos.model.dto.response.MediaResponse;
import nextpos.app.nextpos.service.interf.MediaService;
import nextpos.app.nextpos.service.interf.UserService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MediaService mediaService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody UserRegisterRequest request,
            @RequestHeader("X-Company-Id") Long companyId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signup(request, companyId));
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return userService.authenticateAndGenerateToken(loginRequest);
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid UpdatePasswordRequest request) {
        userService.updatePassword(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/profile-image")
    public ResponseEntity<MediaResponse> getProfileImage(@PathVariable Long userId) {

        List<MediaResponse> mediaList = mediaService.getMediaByEntity("USER", userId);

        if (mediaList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(mediaList.get(0));
    }
}