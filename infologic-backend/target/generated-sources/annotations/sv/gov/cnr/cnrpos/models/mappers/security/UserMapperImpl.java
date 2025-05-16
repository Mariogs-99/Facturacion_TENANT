package sv.gov.cnr.cnrpos.models.mappers.security;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sv.gov.cnr.cnrpos.models.dto.UserDTO;
import sv.gov.cnr.cnrpos.models.security.User;
import sv.gov.cnr.cnrpos.models.security.User.UserBuilder;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T11:51:33-0600",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class UserMapperImpl extends UserMapper {

    @Override
    public User toEntity(UserDTO source) {
        if ( source == null ) {
            return null;
        }

        UserBuilder user = User.builder();

        user.idUser( source.getIdUser() );
        user.firstname( source.getFirstname() );
        user.lastname( source.getLastname() );
        user.email( source.getEmail() );
        user.docType( source.getDocType() );
        user.docNumber( source.getDocNumber() );
        user.password( source.getPassword() );
        user.phone( source.getPhone() );
        user.isActive( source.getIsActive() );
        user.testMode( source.getTestMode() );
        user.idCompany( source.getIdCompany() );
        user.idBranch( source.getIdBranch() );
        user.idPosition( source.getIdPosition() );
        user.carnet( source.getCarnet() );
        user.usuario( source.getUsuario() );
        user.tipo( source.getTipo() );
        List<Long> list = source.getRolIds();
        if ( list != null ) {
            user.rolIds( new ArrayList<Long>( list ) );
        }

        return user.build();
    }

    @Override
    public UserDTO toDTO(User target) {
        if ( target == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setIdUser( target.getIdUser() );
        userDTO.setFirstname( target.getFirstname() );
        userDTO.setLastname( target.getLastname() );
        userDTO.setEmail( target.getEmail() );
        userDTO.setDocType( target.getDocType() );
        userDTO.setDocNumber( target.getDocNumber() );
        userDTO.setPhone( target.getPhone() );
        userDTO.setIsActive( target.getIsActive() );
        userDTO.setTestMode( target.getTestMode() );
        userDTO.setIdCompany( target.getIdCompany() );
        userDTO.setIdBranch( target.getIdBranch() );
        userDTO.setIdPosition( target.getIdPosition() );
        userDTO.setCarnet( target.getCarnet() );
        userDTO.setUsuario( target.getUsuario() );
        userDTO.setTipo( target.getTipo() );

        userDTO.setRolIds( target.getRoless() );

        return userDTO;
    }

    @Override
    public UserDTO toPageDTO(User target) {
        if ( target == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setIdUser( target.getIdUser() );
        userDTO.setFirstname( target.getFirstname() );
        userDTO.setLastname( target.getLastname() );
        userDTO.setCarnet( target.getCarnet() );
        userDTO.setUsuario( target.getUsuario() );
        userDTO.setEmail( target.getEmail() );
        userDTO.setIsActive( target.getIsActive() );
        userDTO.setTestMode( target.getTestMode() );
        userDTO.setTipo( target.getTipo() );

        return userDTO;
    }
}
