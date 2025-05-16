package sv.gov.cnr.cnrpos.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import sv.gov.cnr.cnrpos.entities.TesoreriaReporte;
import sv.gov.cnr.cnrpos.models.dto.ReportSeccionDTO;
import sv.gov.cnr.cnrpos.repositories.TesoreriaReporteRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TesoreriaReporteService {

    private final TesoreriaReporteRepository tesoreriaReporteRepository;

    public List<TesoreriaReporte> findAll(){
        return tesoreriaReporteRepository.findAll();
    }

    public List<TesoreriaReporte> findAllTesoreria(){
        return tesoreriaReporteRepository.findAllTesoreria("T");
    }

    public List<TesoreriaReporte> findAllConta(){
        return tesoreriaReporteRepository.findAllTesoreria("C");
    }

    public TesoreriaReporte findById(String nameReport){
        return tesoreriaReporteRepository.findById(nameReport).orElse(null);
    }

    public List<ReportSeccionDTO> obtenerReportesPorSeccion() {
        List<TesoreriaReporte> reportesEntities = tesoreriaReporteRepository.findAllOrderByClasificacion("C");

        Map<String, List<ReportSeccionDTO.Reporte>> reportesMap = new LinkedHashMap<>();

        for (TesoreriaReporte entity : reportesEntities) {
            String clasificacion = entity.getClasificacion();
            ReportSeccionDTO.Reporte reporte = new ReportSeccionDTO.Reporte();
            reporte.setReporteName(entity.getReporteName());
            reporte.setDescripcion(entity.getDescripcion());

            if (!reportesMap.containsKey(clasificacion)) {
                reportesMap.put(clasificacion, new ArrayList<>());
            }
            reportesMap.get(clasificacion).add(reporte);
        }

        List<ReportSeccionDTO> reportesPorSeccion = new ArrayList<>();

        for (Map.Entry<String, List<ReportSeccionDTO.Reporte>> entry : reportesMap.entrySet()) {
            ReportSeccionDTO seccion = new ReportSeccionDTO();
            seccion.setLabel(entry.getKey());
            seccion.setReportes(entry.getValue());
            reportesPorSeccion.add(seccion);
        }

        return reportesPorSeccion;
    }





}
