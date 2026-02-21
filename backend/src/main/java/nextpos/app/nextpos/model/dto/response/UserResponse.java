package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.model.entity.UserProfile;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class UserResponse {

        private final Long id;
        private final String email;
        private final String phone;
        private final String firstname;
        private final String lastname;
        private final Boolean status;
        private final String profileId;
        private final String profile;
        private final Long roleId;
        private final String roleName;
        private final Set<String> rolePermissions;
        private final Long createdBy;
        private final LocalDateTime createdAt;
        private final Long updatedBy;
        private final LocalDateTime updatedAt;
        private final Long companyId;
        private final Set<Long> warehouseIds;
        private final Long defaultWarehouseId;

        public static UserResponse fromEntity(User user, MediaResponse mediaResponse) {
                UserProfile profile = user.getProfile();

                return UserResponse.builder()
                                .id(user.getId())
                                .email(user.getEmail())
                                .phone(user.getPhone())
                                .firstname(profile != null ? profile.getFirstname() : null)
                                .lastname(profile != null ? profile.getLastname() : null)
                                .status(user.getStatus())
                                .profileId(mediaResponse != null ? mediaResponse.getId() : null)
                                .profile(mediaResponse != null ? mediaResponse.getUrl() : null)
                                .roleId(user.getRole() != null ? user.getRole().getId() : null)
                                .roleName(user.getRole() != null ? user.getRole().getName() : null)
                                .rolePermissions(user.getRole() != null
                                                ? user.getRole().getPermissions()
                                                                .stream()
                                                                .map(permission -> permission.getName().name())
                                                                .collect(Collectors.toSet())
                                                : Set.of())
                                .createdBy(user.getCreatedBy())
                                .createdAt(user.getCreatedAt())
                                .updatedBy(user.getUpdatedBy())
                                .updatedAt(user.getUpdatedAt())
                                .companyId(user.getCompanyId())
                                .warehouseIds(user.getUserWarehouses() != null
                                                ? user.getUserWarehouses().stream()
                                                                .map(uw -> uw.getWarehouse().getId())
                                                                .collect(Collectors.toSet())
                                                : Set.of())
                                .defaultWarehouseId(user.getDefaultWarehouse() != null
                                                ? user.getDefaultWarehouse().getId()
                                                : null)
                                .build();
        }
}
