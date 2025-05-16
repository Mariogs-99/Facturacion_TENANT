package sv.gov.cnr.cnrpos.models.mappers.security;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import sv.gov.cnr.cnrpos.models.dto.UserDTO;
import sv.gov.cnr.cnrpos.models.security.User;

import java.util.HashMap;
import java.util.Map;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Mapping(target = "roles", ignore = true)
    public abstract User toEntity(UserDTO source);


    @Mapping(source = "idUser", target = "idUser")
    @Mapping(source = "firstname", target = "firstname")
    @Mapping(source = "lastname", target = "lastname")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "docType", target = "docType")
    @Mapping(source = "docNumber", target = "docNumber")
    @Mapping(target = "password", ignore = true)
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "isActive", target = "isActive")
    @Mapping(source = "testMode", target = "testMode")
    @Mapping(source = "idCompany", target = "idCompany")
    @Mapping(source = "idBranch", target = "idBranch")
    @Mapping(source = "idPosition", target = "idPosition")
    @Mapping(target = "rolIds", expression = "java(target.getRoless())")
    @Mapping(target = "roles", ignore = true)
    @Named("toDTO")
    public abstract UserDTO toDTO(User target);

    @Mapping(source = "idUser", target = "idUser")
    @Mapping(source = "firstname", target = "firstname")
    @Mapping(source = "lastname", target = "lastname")
    @Mapping(source = "carnet", target = "carnet")
    @Mapping(source = "usuario", target = "usuario")
    @Mapping(source = "email", target = "email")
    @Mapping(target = "docType", ignore = true)
    @Mapping(target = "docNumber", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(source = "isActive", target = "isActive")
    @Mapping(source = "testMode", target = "testMode")
    @Mapping(target = "idCompany", ignore = true)
    @Mapping(target = "idBranch", ignore = true)
    @Mapping(target = "idPosition", ignore = true)
    @Mapping(target = "rolIds", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Named("toPageDTO")
    public abstract UserDTO toPageDTO(User target);

    public UserDTO toDTODynamic(User target, Map<String, Object> properties) {
        UserDTO dto = toDTO(target);
        dto.setMapProperties(properties);
        return dto;
    }
}
