package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextpos.app.nextpos.model.entity.User;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class UserResponse {

        private final Long id;
        private final String email;
        private final String username;
        private final String firstname;
        private final String lastname;
        private final String phone;
        private final Boolean status;
        private final String profileId;
        private final String profile; // profile picture or avatar
        private final Long roleId;
        private final String roleName;
        private final Set<String> rolePermissions;
        private final Long createdBy;
        private final LocalDateTime createdAt;
        private final Long updatedBy;
        private final LocalDateTime updatedAt;
        private final Long companyId;

        /**
         * Branch/warehouse assignments for the user.
         */
        private final Set<Long> warehouseIds;

        /**
         * Default warehouse ID for the user.
         */
        private final Long defaultWarehouseId;

        public static UserResponse fromEntity(User user, MediaResponse mediaResponse) {
                return UserResponse.builder()
                                .id(user.getId())
                                .email(user.getEmail())
                                .username(user.getUsername())
                                .firstname(user.getFirstname())
                                .lastname(user.getLastname())
                                .phone(user.getPhone())
                                .status(user.getStatus())
                                .profileId(mediaResponse.getId())
                                .profile(mediaResponse.getUrl())
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
