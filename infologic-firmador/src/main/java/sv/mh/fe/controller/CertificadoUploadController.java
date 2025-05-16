package sv.mh.fe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/certificado")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CertificadoUploadController {

    private static final String UPLOAD_DIR = "uploads";

    @PostMapping("/subir")
    public ResponseEntity<String> subirCertificado(@RequestParam("certificado") MultipartFile certificado,
                                                   @RequestParam("nit") String nit) {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            File destino = new File(UPLOAD_DIR, nit + ".crt");
            try (InputStream in = certificado.getInputStream(); FileOutputStream out = new FileOutputStream(destino)) {
                in.transferTo(out);
            }
            return ResponseEntity.ok("✅ Certificado subido correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Error al subir el certificado: " + e.getMessage());

        }
    }

    @GetMapping("/ping")
    public ResponseEntity<String> prueba() {
        return ResponseEntity.ok("✅ El endpoint de certificado está funcionando correctamente.");
    }

}
