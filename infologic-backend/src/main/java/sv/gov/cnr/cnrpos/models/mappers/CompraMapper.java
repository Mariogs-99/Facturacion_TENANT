package sv.gov.cnr.cnrpos.models.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sv.gov.cnr.cnrpos.entities.Compra;
import sv.gov.cnr.cnrpos.models.dto.CompraDTO;

@Mapper(componentModel = "spring")
public abstract class CompraMapper {

    public abstract Compra toEntity(CompraDTO source);


    @Mapping(target = "idCompra", source = "idCompra")
    @Mapping(target = "codigoGeneracion", source = "codigoGeneracion")
    @Mapping(target = "numeroControl", source = "numeroControl")
    @Mapping(target = "fechaEmision", source = "fechaEmision")
    @Mapping(target = "tipoOperacion", source = "tipoOperacion")
    @Mapping(target = "selloRecepcion", source = "selloRecepcion")
    @Mapping(target = "idProveedor", source = "idProveedor")
    @Mapping(target = "totalGravado", source = "totalGravado")
    @Mapping(target = "totalExento", source = "totalExento")
    @Mapping(target = "totalOperacion", source = "totalOperacion")
    @Mapping(target = "user", source = "user", ignore = true)
    @Mapping(target = "sucursal", source = "sucursal", ignore = true)
    @Mapping(target = "createdAt", source = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", source = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", source = "deletedAt", ignore = true)
    public abstract CompraDTO toDtoList(Compra target);

    @Mapping(target = "idCompra", source = "idCompra")
    @Mapping(target = "codigoGeneracion", source = "codigoGeneracion")
    @Mapping(target = "numeroControl", source = "numeroControl")
    @Mapping(target = "fechaEmision", source = "fechaEmision")
    @Mapping(target = "tipoOperacion", source = "tipoOperacion")
    @Mapping(target = "selloRecepcion", source = "selloRecepcion")
    @Mapping(target = "idProveedor", source = "idProveedor")
    @Mapping(target = "totalGravado", source = "totalGravado")
    @Mapping(target = "totalExento", source = "totalExento")
    @Mapping(target = "totalOperacion", source = "totalOperacion")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "sucursal", source = "sucursal")
    @Mapping(target = "createdAt", source = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", source = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", source = "deletedAt", ignore = true)
    public abstract CompraDTO toDto(Compra target);

}
