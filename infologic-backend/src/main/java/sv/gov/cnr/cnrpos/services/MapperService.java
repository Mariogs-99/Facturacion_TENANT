package sv.gov.cnr.cnrpos.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service
public class MapperService {

    private final ApplicationContext context;

    // Constructor con inyección de dependencias
    public MapperService(ApplicationContext context) {
        this.context = context;
    }

    public <S, D> D map(S source, String mapperName, String methodName, Class<D> destinationType) {
        Object mapper = context.getBean(mapperName);
        try {
            Method method = mapper.getClass().getMethod(methodName, source.getClass());
            return (D) method.invoke(mapper, source);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking mapper method", e);
        }
    }
}
