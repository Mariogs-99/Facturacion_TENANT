package sv.gov.cnr.factelectrcnrservice.models.enums;

public enum TipoReporte {

    LibroIvaContribuyentes("00"),
    LibroIvaConsumidorfinal("01");
    
    

    private final String codigo;
    TipoReporte(String codigo){
        this.codigo = codigo;
    }
}
