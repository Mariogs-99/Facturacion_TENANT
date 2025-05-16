package sv.gov.cnr.factelectrcnrservice.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sv.gov.cnr.factelectrcnrservice.models.enums.TipoDTE;

@Component
@RequiredArgsConstructor
public class DTEFactory {
    private final DTEFactura dteFactura;
    private final DTEComprobanteCreditoFiscal dteComprobanteCreditoFiscal;
    private final DTEComprobanteLiquidacion dteComprobanteLiquidacion;
    private final DTEComprobanteRetencion dteComprobanteRetencion;
    private final DTEFacturaExportacion dteFacturaExportacion;
    private final DTEFacturaSujetoExcluido dteFacturaSujetoExcluido;
    private final DTENotaCredito dteNotaCredito;
    private final DTENotaDebito dteNotaDebito;
    private final DTEContingencia dteContingencia;
    private final DTEInvalidacion dteInvalidacion;
    public DTE fabricarObjetoDTE(TipoDTE tipoDTE) {
        return switch (tipoDTE) {
            case FACTURA -> dteFactura;
            case COMPROBANTE_CREDITO_FISCAL -> dteComprobanteCreditoFiscal;
            case COMPROBANTE_LIQUIDACION -> dteComprobanteLiquidacion;
            case COMPROBANTE_RETENCION -> dteComprobanteRetencion;
            case FACTURAS_EXPORTACION -> dteFacturaExportacion;
            case FACTURA_SUJETO_EXCLUIDO -> dteFacturaSujetoExcluido;
            case NOTA_CREDITO -> dteNotaCredito;
            case NOTA_DEBITO -> dteNotaDebito;
            case CONTINGENCIA -> dteContingencia;
            case INVALIDACION -> dteInvalidacion;
            default -> throw new IllegalArgumentException("Tipo de DTE no implementado: " + tipoDTE);
        };
    }
}
