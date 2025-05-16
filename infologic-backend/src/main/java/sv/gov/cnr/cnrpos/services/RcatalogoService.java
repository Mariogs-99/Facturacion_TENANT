package sv.gov.cnr.cnrpos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sv.gov.cnr.cnrpos.entities.Company;
import sv.gov.cnr.cnrpos.entities.RCatalogos;
import sv.gov.cnr.cnrpos.exceptions.ResourceNotFoundException;
import sv.gov.cnr.cnrpos.repositories.RcatalogoRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RcatalogoService {

    private final RcatalogoRepository rcatalogoRepository;

    @Autowired
    public RcatalogoService(RcatalogoRepository rcatalogoRepository) {
        this.rcatalogoRepository = rcatalogoRepository;
    }

    public RCatalogos saveRcatalogo ( RCatalogos rCatalogos){
        return rcatalogoRepository.save(rCatalogos);
    }
    public List<RCatalogos> getAllRcatalogos(){
        return rcatalogoRepository.findAll();
    }

    public void deleteRcatalogo(Long id) {
        Optional<RCatalogos> rcatalogo =rcatalogoRepository.findById(id);
        if (rcatalogo.isPresent()){
            rcatalogoRepository.delete(rcatalogo.get());
        } else {
            throw new ResourceNotFoundException("No se encontró el Catálogo");
        }
    }

    // ✅ Método para obtener un catálogo por grupo y valor
    public Optional<RCatalogos> getRCatalogoByValor(String grupo, String valor) {
        return rcatalogoRepository.findByGrupoAndValor(grupo, valor);
    }

    public List<Map<String, Object>> getDepartamentosConMunicipios() {
        List<RCatalogos> departamentos = rcatalogoRepository.findByGrupo("DEPARTAMENTOS");
        List<RCatalogos> municipios = rcatalogoRepository.findByGrupo("MUNICIPIOS");

        List<Map<String, Object>> resultado = new ArrayList<>();

        for (RCatalogos depto : departamentos) {
            Map<String, Object> deptoData = new HashMap<>();
            deptoData.put("codigo", depto.getIdMh()); // Código del departamento
            deptoData.put("nombre", depto.getValor()); // Nombre del departamento

            // Filtrar municipios que pertenecen a este departamento
            List<Map<String, Object>> municipiosList = municipios.stream()
                    .filter(m -> m.getIdMhPadre() != null && m.getIdMhPadre().equals(depto.getIdMh()))
                    .map(m -> {
                        Map<String, Object> muniData = new HashMap<>();
                        muniData.put("codigo", m.getIdMh()); // Código del municipio
                        muniData.put("nombre", m.getValor()); // Nombre del municipio
                        return muniData;
                    })
                    .collect(Collectors.toList());

            deptoData.put("municipios", municipiosList);
            resultado.add(deptoData);
        }

        return resultado;
    }


    public List<RCatalogos> getRCatalogosByGrupoAndDepartamento(String departamento) {
        return rcatalogoRepository.buscarMunicipiosPorDepartamento(departamento);
    }

    public RCatalogos getRcatalogos(Long id) {
        return rcatalogoRepository.findById(id).orElse(null);
    }



    public String getRcatalogosValue(Long id) {
        RCatalogos rcatalogo = rcatalogoRepository.findById(id).orElse(null);
        return rcatalogo != null ? rcatalogo.getValor() : null;
    }

    // Obtener registros por grupo
    public List<RCatalogos> getRCatalogosByGrupo(String grupo) {
        return rcatalogoRepository.findByGrupo(grupo);
    }

    // renombrar el listado de catálogos por grupo con la estructura { id: (ID_CATALOGO); name: (VALOR)}
    // utilizado en el frontend para los select.
    public List<Map<String, Object>> getRCatalogosAsList(String grupo) {
        List<RCatalogos> catalogosList = rcatalogoRepository.findByGrupo(grupo);
        List<Map<String, Object>> catalogoList = new ArrayList<>();

        for (RCatalogos rcatalogo : catalogosList) {
            Map<String, Object> catalogoMap = new HashMap<>();
            catalogoMap.put("id", rcatalogo.getIdCatalogo());
            catalogoMap.put("name", rcatalogo.getValor());
            catalogoMap.put("id_mh", rcatalogo.getIdMh());
            catalogoList.add(catalogoMap);
        }

        return catalogoList;
    }

    public List<Map<String, Object>> getRCatalogosAsList1(Long id) {
        List<RCatalogos> catalogosList =  rcatalogoRepository.findByIdCatalogo(id);
        List<Map<String, Object>> catalogoList = new ArrayList<>();

        for (RCatalogos rcatalogo : catalogosList) {
            Map<String, Object> catalogoMap = new HashMap<>();
            catalogoMap.put("id", rcatalogo.getIdCatalogo());
            catalogoMap.put("name", rcatalogo.getValor());
            catalogoMap.put("id_mh", rcatalogo.getIdMh());
            catalogoList.add(catalogoMap);
        }

        return catalogoList;
    }


    public List<Map<String, Object>> getMunicipios() {


        List<RCatalogos> catalogosList = rcatalogoRepository.findByGrupo("MUNICIPIOS");

        List<Map<String, Object>> catalogoList = new ArrayList<>();

        for (RCatalogos rcatalogo : catalogosList) {
            String departamento;

            if (rcatalogo.getIdPadre() == null) {
                departamento = " ";
            } else {
                departamento = getRcatalogosValue(rcatalogo.getIdPadre());
            }

            Map<String, Object> catalogoMap = new HashMap<>();

            catalogoMap.put("id", rcatalogo.getIdCatalogo());
            catalogoMap.put("id_depto", rcatalogo.getIdPadre());
            catalogoMap.put("name", rcatalogo.getValor() + " - " + departamento);
            catalogoMap.put("municipio", rcatalogo.getValor());
            catalogoMap.put("id_mh", rcatalogo.getIdMh());
            catalogoMap.put("id_mh_padre", rcatalogo.getIdMhPadre());
            catalogoMap.put("departamento", departamento);
            catalogoList.add(catalogoMap);
        }


        return catalogoList;
    }

    public List<Map<String, Object>> getDepartamentos() {


        List<RCatalogos> catalogosList = rcatalogoRepository.findByGrupo("DEPARTAMENTOS");

        List<Map<String, Object>> catalogoList = new ArrayList<>();

        for (RCatalogos rcatalogo : catalogosList) {
            String departamento;

            if (rcatalogo.getIdPadre() == null) {
                departamento = " ";
            } else {
                departamento = getRcatalogosValue(rcatalogo.getIdPadre());
            }

            Map<String, Object> catalogoMap = new HashMap<>();

            catalogoMap.put("id", rcatalogo.getIdCatalogo());
            catalogoMap.put("id_mh", rcatalogo.getIdMh());
            catalogoMap.put("departamento", rcatalogo.getValor());
            catalogoList.add(catalogoMap);
        }


        return catalogoList;
    }





}
