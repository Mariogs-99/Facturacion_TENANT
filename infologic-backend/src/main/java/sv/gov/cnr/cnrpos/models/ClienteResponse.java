    package sv.gov.cnr.cnrpos.models;

    import com.fasterxml.jackson.annotation.JsonAlias;
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class ClienteResponse {
        @JsonAlias("nombre")
        private String name;
        private int tipo_documento;
        private String nit;
        private String dui;
        private String nrc;
        private String direccion;
        private String actividad_codigo;
        private String actividad_nombre;
        private String departamento_codigo;
        private String departamento_nombre;
        private String municipio_codigo;
        private String municipio_nombre;
        private String correo;
        private String telefono;
        private String descuento;
        private String contribuyente;
        private String aplica_retencion;
        private String es_contribuyente; // determina si es nit o dui el documento


        // Nuevo campo para el nombre concatenado
        private String nombreConcatenado;

        public void concatenarCampos() {
            StringBuilder sb = new StringBuilder();
            if (name != null) {
                sb.append(name);
            }
            if (nit != null && !nit.isEmpty()) {
                sb.append(" - ").append(nit);
            }
            if (dui != null && !dui.isEmpty()) {
                sb.append(" - ").append(dui);
            }
            if (nrc != null && !nrc.isEmpty()) {
                sb.append(" - ").append(nrc);
            }
            this.nombreConcatenado = sb.toString();
        }
    }
