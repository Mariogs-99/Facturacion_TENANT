package sv.gov.cnr.factelectrcnrservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("GET", "POST", "PUT", "DELETE")
//                .allowedHeaders("Origin", "X-Requested-With", "Content-Type", "Accept","Authorization");
//    }


    // ENTORNO MAS SEGURO PARA EL BACKEND
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica CORS solo a rutas de la API
                .allowedOrigins("http://localhost", "http://localhost:4200") // Permite Angular y backend en Docker-------ENTORNO DEV
                //.allowedOrigins("https://tudominio.com", "https://otrodominio.com") // Define orígenes específicos -------ENTORNO PROD
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Métodos permitidos
                .allowedHeaders("Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization")
                .allowCredentials(true); // Permite credenciales (cookies, JWT, etc.)
    }
}