
package br.edu.ifsp.associacaogaia.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        if (cloudName.isBlank() || apiKey.isBlank() || apiSecret.isBlank()) {
            throw new IllegalStateException(
                    "As credenciais do Cloudinary não foram configuradas."
            );
        }

        Map<String, String> configuracao = new HashMap<>();
        configuracao.put("cloud_name", cloudName);
        configuracao.put("api_key", apiKey);
        configuracao.put("api_secret", apiSecret);
        configuracao.put("secure", "true");

        return new Cloudinary(configuracao);
    }
}
