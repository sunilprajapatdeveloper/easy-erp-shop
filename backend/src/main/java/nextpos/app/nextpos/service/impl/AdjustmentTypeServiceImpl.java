package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateAdjustmentTypeRequest;
import nextpos.app.nextpos.model.dto.response.AdjustmentTypeResponse;
import nextpos.app.nextpos.model.entity.AdjustmentType;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.repository.AdjustmentTypeRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.AdjustmentTypeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdjustmentTypeServiceImpl implements AdjustmentTypeService {

    private final AdjustmentTypeRepository adjustmentTypeRepository;
    private final UserRepository userRepository;

    @Override
    public AdjustmentTypeResponse createAdjustmentType(CreateAdjustmentTypeRequest request) {
        // Get authenticated user using helper
        User createdBy = UserContext.getAuthenticatedUser(userRepository);

        if (request.getStockEffect() == null) {
            throw new IllegalArgumentException("Stock effect is required.");
        }

        AdjustmentType adjustmentType = new AdjustmentType();
        adjustmentType.setName(request.getName());
        adjustmentType.setDescription(request.getDescription());
        adjustmentType.setStockEffect(request.getStockEffect());
        adjustmentType.setCreatedBy(createdBy.getId());
        adjustmentType.setCreatedAt(LocalDateTime.now());
        adjustmentType.setCompanyId(createdBy.getCompanyId());

        AdjustmentType saved = adjustmentTypeRepository.save(adjustmentType);
        return toResponse(saved);
    }

    @Override
    public AdjustmentTypeResponse getAdjustmentTypeById(Long id) {
        AdjustmentType adjustmentType = adjustmentTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adjustment type not found with id: " + id));
        return toResponse(adjustmentType);
    }

    @Override
    public List<AdjustmentTypeResponse> findAllByCreatedBy(Long userId) {
        return adjustmentTypeRepository.findAll().stream()
                .filter(type -> type.getCreatedBy() != null && type.getCreatedBy().equals(userId))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdjustmentTypeResponse> getAllAdjustmentTypes() {
        return adjustmentTypeRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AdjustmentTypeResponse updateAdjustmentType(Long id, CreateAdjustmentTypeRequest request) {
        AdjustmentType adjustmentType = adjustmentTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adjustment type not found with id: " + id));

        adjustmentType.setName(request.getName());
        adjustmentType.setDescription(request.getDescription());
        adjustmentType.setStockEffect(request.getStockEffect());

        return toResponse(adjustmentTypeRepository.save(adjustmentType));
    }

    @Override
    public void deleteAdjustmentType(Long id) {
        adjustmentTypeRepository.deleteById(id);
    }

    private AdjustmentTypeResponse toResponse(AdjustmentType adjustmentType) {
        return AdjustmentTypeResponse.builder()
                .id(adjustmentType.getId())
                .name(adjustmentType.getName())
                .description(adjustmentType.getDescription())
                .stockEffect(adjustmentType.getStockEffect())
                .build();
    }
}