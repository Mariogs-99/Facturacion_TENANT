package sv.gov.cnr.factelectrcnrservice.models.enums;

import lombok.Getter;

@Getter
public enum EstatusCola {

    NA(0),
    ERROR(1),
    APROBADO(2),
    RECHAZADO(3),
    PENDIENTE(4),
    MARCADO_CONTINGENCIA(5),
    APROBADO_CONTINGENCIA(6),
    ANULADO(7),
    ENVIADO(8);

    private final int codigo;
    EstatusCola(int codigo){
        this.codigo = codigo;
    }
}
