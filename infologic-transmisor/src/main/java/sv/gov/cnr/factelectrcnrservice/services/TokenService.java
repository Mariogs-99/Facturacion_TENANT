package sv.gov.cnr.factelectrcnrservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.gov.cnr.factelectrcnrservice.entities.TokenMh;
import sv.gov.cnr.factelectrcnrservice.repositories.TokenRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    public Optional<TokenMh> obtenerTokenPorHorasTranscurridas(int horas) {
        var now = Instant.now();
        var instante = now.minusSeconds(horas * 3600L);
        var instanceHaceCantHoras = Timestamp.from(instante);
        return tokenRepository.findRecentToken(instanceHaceCantHoras);
    }
    public TokenMh guardarToken(String tokenStr){
        var token = TokenMh.builder()
                .token(tokenStr)
                .build();
        return tokenRepository.save(token);
    }
}
