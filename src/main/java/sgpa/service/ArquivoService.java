package sgpa.service;

import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sgpa.controller.exception.ForbiddenException;
import sgpa.controller.exception.ResourceNotFoundException;
import sgpa.model.Arquivo;
import sgpa.model.Participacao;
import sgpa.model.Projeto;
import sgpa.repository.ArquivoRepository;

@Service
public class ArquivoService {

	private ArquivoRepository arquivoRepository;

	@Value("${sgpa.upload.directory}")
	private String diretorioArquivo;

	@Autowired
	public ArquivoService(ArquivoRepository arquivoRepository) {
		this.arquivoRepository = arquivoRepository;
	}

	public Iterable<Arquivo> listarTodosPorProjeto(Projeto projeto) {
		return arquivoRepository.findByProjetoOrderByIdDesc(projeto);
	}

	public Arquivo obterPorIdPorProjetoId(Long idArquivo, Long idProjeto) {
		return arquivoRepository.findByIdAndProjetoId(idArquivo, idProjeto);
	}

	public Arquivo salvar(MultipartFile multipartFile, Projeto projeto, Participacao participacao) {
		Arquivo arquivo = new Arquivo();
		arquivo.setNome(multipartFile.getOriginalFilename());
		arquivo.setContentType(multipartFile.getContentType());
		arquivo.setDataEnvio(new Date());
		arquivo.setProjeto(projeto);
		arquivo.setParticipacao(participacao);

		if (!multipartFile.isEmpty()) {
			try {
				String diretorioProjeto = diretorioArquivo + projeto.getId() + "\\";

				if (!new File(diretorioProjeto).exists()) {
					new File(diretorioProjeto).mkdirs();
				}
				String orgName = multipartFile.getOriginalFilename();
				String filePath = diretorioProjeto + orgName;
				File dest = new File(filePath);
				multipartFile.transferTo(dest);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return arquivoRepository.save(arquivo);
	}

	public void deletar(Arquivo arquivo) {
		String pathFile = diretorioArquivo + arquivo.getProjeto().getId() + "\\" + arquivo.getNome();
		File file = new File(pathFile);
		if (!file.delete())
			throw new ForbiddenException();

		arquivoRepository.delete(arquivo);
	}

	public File realizarDownload(Long idArquivo, Long idProjeto) {
		Arquivo arquivo = obterPorIdPorProjetoId(idArquivo, idProjeto);
		if (arquivo == null) {
			throw new ResourceNotFoundException();
		}

		String pathFile = diretorioArquivo + idProjeto + "\\" + arquivo.getNome();
		File file = new File(pathFile);
		System.out.println(pathFile);

		if (!file.exists()) {
			throw new ResourceNotFoundException();
		}
		return file;
	}

}
