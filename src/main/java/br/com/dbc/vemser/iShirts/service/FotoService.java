package br.com.dbc.vemser.iShirts.service;

import br.com.dbc.vemser.iShirts.dto.foto.FotoDTO;
import br.com.dbc.vemser.iShirts.model.Foto;
import br.com.dbc.vemser.iShirts.exceptions.RegraDeNegocioException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import br.com.dbc.vemser.iShirts.model.Variacao;
import br.com.dbc.vemser.iShirts.repository.FotoRepository;
import br.com.dbc.vemser.iShirts.utils.MediaTypeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FotoService {

    private final FotoRepository fotoRepository;
    private final VariacaoService variacaoService;
    private final MediaTypeUtil mediaTypeUtil;
    private final ObjectMapper objectMapper;

    public FotoDTO criar(MultipartFile arquivo, Integer idVariacao) throws RegraDeNegocioException, IOException {

       Variacao variacao = variacaoService.buscarPorId(idVariacao);

       Foto fotoEntity = gerarFoto(arquivo);
       fotoEntity.setIdFoto(idVariacao);
       fotoEntity.setVariacao(variacao);
       fotoEntity = fotoRepository.save(fotoEntity);

       FotoDTO fotoDTO = objectMapper.convertValue(fotoEntity, FotoDTO.class);
       return fotoDTO;
    }

    public FotoDTO atualizar(Integer idFoto, MultipartFile arquivo) throws RegraDeNegocioException, IOException {

        Foto fotoEntity = buscarPorId(idFoto);
        Foto fotoUpdate = gerarFoto(arquivo);

        fotoEntity.setArquivo(fotoUpdate.getArquivo());

        fotoRepository.save(fotoEntity);

        FotoDTO fotoDTO = objectMapper.convertValue(fotoEntity, FotoDTO.class);
        return fotoDTO;
    }


    public FotoDTO obterPorId(Integer idFoto) throws RegraDeNegocioException {
        Foto foto = buscarPorId(idFoto);
        FotoDTO fotoDTO = objectMapper.convertValue(foto, FotoDTO.class);
        return fotoDTO;
    }

    public void deletar(Integer idFoto) throws RegraDeNegocioException {
        Foto foto = buscarPorId(idFoto);

        fotoRepository.delete(foto);
    }

    public Foto buscarPorId(Integer idFoto) throws RegraDeNegocioException {
        return fotoRepository.findById(idFoto)
                .orElseThrow(() -> new RegraDeNegocioException("Imagem não encontrada!"));
    }

    public Foto gerarFoto(MultipartFile arquivo) throws RegraDeNegocioException, IOException {
        String tipoArquivo = mediaTypeUtil.getTipoArquivo(arquivo);
        validarFormato(tipoArquivo);
        String nome = LocalDateTime.now() + "_" + arquivo.getOriginalFilename();

        if (arquivo.getSize() > 3145728) {
            throw new SizeLimitExceededException("Tamanho do arquivo excede o limite máximo de 3 MB", arquivo.getSize(), 3145728);
        }

        if (nome.length() > 255) {
            throw new RegraDeNegocioException("Nome do arquivo é muito grande");
        }
        Foto fotoEntity = new Foto();
        fotoEntity.setArquivo(arquivo.getBytes());
        return fotoEntity;
    }

    void validarFormato(String formato) throws RegraDeNegocioException {
        String formatoUpper = formato.toUpperCase();
        if (formatoUpper.equals("JPEG") || formatoUpper.equals("JPG") || formatoUpper.equals("WEBP") || formatoUpper.equals("PNG") || formatoUpper.equals("GIF") || formatoUpper.equals("BMP")) {
            return;
        }

        throw new RegraDeNegocioException("Formato de arquivo não suportado, formatos suportados: WEBP, JPG, JPEG, GIF, PNG, BMP");
    }

}
