package sv.gov.cnr.factelectrcnrservice.factory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.allegro.finance.tradukisto.MoneyConverters;
import pl.allegro.finance.tradukisto.ValueConverters;
import sv.gov.cnr.factelectrcnrservice.entities.DteTransaccion;
import sv.gov.cnr.factelectrcnrservice.entities.Transaccion;
import sv.gov.cnr.factelectrcnrservice.models.dto.MotivoAnulacionDTO;
import sv.gov.cnr.factelectrcnrservice.services.DteTransaccionService;


import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.Optional;
import java.util.UUID;

@Component
public abstract class DTE {

    @Autowired
    DteTransaccionService dteTransaccionService;

    @Autowired
    ObjectMapper objectMapper;
    public abstract Object crearDTEJSON(Transaccion transaccion) throws JsonProcessingException;

    String obtenerCodigoGeneracion(){return UUID.randomUUID().toString().toUpperCase();}

    String obtenerFechaEmision(){
        // Definir la zona horaria de El Salvador
        ZoneId zonaHorariaElSalvador = ZoneId.of("America/El_Salvador");

        // Obtener la fecha actual en la zona horaria de El Salvador
        ZonedDateTime fechaActualEnElSalvador = ZonedDateTime.now(zonaHorariaElSalvador);
        LocalDate fechaActual = fechaActualEnElSalvador.toLocalDate();

        // Formatear la fecha
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return fechaActual.format(formatoFecha);
    }

    String obtenerHoraEmision(){
        ZoneId zonaHorariaElSalvador = ZoneId.of("America/El_Salvador");

        // Obtener la hora actual en la zona horaria de El Salvador
        ZonedDateTime horaActualEnElSalvador = ZonedDateTime.now(zonaHorariaElSalvador);
        LocalTime horaActual = horaActualEnElSalvador.toLocalTime();

        // Formatear la hora
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        return horaActual.format(formatoHora);
    }

    String generarJSON(Object dte) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dte);
    }

    String cantidadALetras(BigDecimal cantidad){
        var converter = MoneyConverters.SPANISH_BANKING_MONEY_VALUE;
        return converter.asWords(cantidad, "$" );
    }


    public static AbstractMap.SimpleEntry<String, String> actualizarFechaHora(String date, String time, long hoursToAdd) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        LocalTime localTime = LocalTime.parse(time, timeFormatter);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

        LocalDateTime updatedDateTime = localDateTime.plusHours(hoursToAdd);

        String updatedDate = updatedDateTime.format(dateFormatter);
        String updatedTime = updatedDateTime.format(timeFormatter);

        return new AbstractMap.SimpleEntry<>(updatedDate, updatedTime);
    }


    public void crearDteInfo(DteTransaccion dteTransaccion){
        dteTransaccionService.save(dteTransaccion);
    }

    public abstract Object crearJsonInvalidacion(Transaccion transaccion, MotivoAnulacionDTO data);


}
