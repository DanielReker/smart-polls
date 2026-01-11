package io.github.danielreker.smartpolls.mappers;

import io.github.danielreker.smartpolls.dao.entities.auth.UserEntity;
import io.github.danielreker.smartpolls.web.dtos.auth.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserMapper {

    @Mapping(target = "isRegistered", ignore = true)
    UserResponse toResponse(UserEntity entity);

}
