package sv.gov.cnr.factelectrcnrservice.utils;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sv.gov.cnr.factelectrcnrservice.entities.Transaccion;
import sv.gov.cnr.factelectrcnrservice.services.CatalogoService;
import sv.gov.cnr.factelectrcnrservice.services.CompanyService;
import sv.gov.cnr.factelectrcnrservice.services.RangoService;

@Component
@RequiredArgsConstructor
public class Validator {

    private final CompanyService companyService;
    private final CatalogoService catalogoService;
    private final RangoService rangoService;


    public boolean condicionesMinimas(Transaccion transaccion){
        try{
            var tipoDte = catalogoService.obtenerIdCatalogoRegistroPorIDMHyIDCatalogo(transaccion.getTipoDTE(),CatalogoService.TIPO_DE_DOCUMENTO);
            Integer correlativoDte = rangoService.findRangoActivoPorDte(tipoDte.intValue(), transaccion.getSucursal().getIdSucursal());
        }
        catch(Exception e) {
            return false;
        }
        var emisor = companyService.getEmisor();
        if(emisor.getNit() == null || emisor.getIdActividadMH() == null || emisor.getClavePrimariaCert()  == null ||
                emisor.getIdMuniCompany() == null || emisor.getIdDeptoCompany() == null || emisor.getMhUser()== null ||
                emisor.getNrc() == null || emisor.getMhPass() == null){
            return false;
        }
        if(transaccion.getSucursal().getCodigoSucursal() == null || transaccion.getSucursal().getCodigoSucursal().length() > 8){
            return false;
        }
        return true;
    }

}
