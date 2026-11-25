
package br.edu.ifsp.associacaogaia.service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class ImagemStorageService {

    private static final Set<String> TIPOS_PERMITIDOS = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private static final String PASTA_EVENTOS = "associacao-gaia/eventos";
    private static final String PASTA_TRABALHOS = "associacao-gaia/trabalhos";

    private final Cloudinary cloudinary;

    public ImagemStorageService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public ResultadoUpload upload(MultipartFile arquivo) {
        return upload(arquivo, PASTA_EVENTOS);
    }

    /** Envia a foto de um trabalho (card) de artesão para a pasta de trabalhos. */
    public ResultadoUpload uploadTrabalho(MultipartFile arquivo) {
        return upload(arquivo, PASTA_TRABALHOS);
    }

    private ResultadoUpload upload(MultipartFile arquivo, String pasta) {
        validarArquivo(arquivo);

        try {
            Map<?, ?> resultado = cloudinary.uploader().upload(
                    arquivo.getBytes(),
                    ObjectUtils.asMap(
                            "folder", pasta,
                            "resource_type", "image"
                    )
            );

            String url = (String) resultado.get("secure_url");
            String publicId = (String) resultado.get("public_id");

            if (url == null || publicId == null) {
                throw new IllegalStateException(
                        "O Cloudinary não retornou os dados esperados da imagem."
                );
            }

            return new ResultadoUpload(url, publicId);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Não foi possível enviar a imagem para o Cloudinary.", e
            );
        }
    }

    public void excluir(String publicId) {
        if (publicId == null || publicId.isBlank()) {
            return;
        }

        try {
            cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.asMap("resource_type", "image")
            );
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Não foi possível excluir a imagem do Cloudinary.", e
            );
        }
    }

    private void validarArquivo(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new br.edu.ifsp.associacaogaia.exception.ImagemInvalidaException(
                    "Selecione uma imagem para enviar."
            );
        }

        if (!TIPOS_PERMITIDOS.contains(arquivo.getContentType())) {
            throw new br.edu.ifsp.associacaogaia.exception.ImagemInvalidaException(
                    "Formato inválido. Envie uma imagem JPG, PNG ou WEBP."
            );
        }
    }

    public record ResultadoUpload(String url, String publicId) {
    }
}
