package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.MaisInfoDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizarTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private CursoRepository cursoRepository;

	// @RequestMapping(value="/topicos", method = RequestMethod.GET)
	@GetMapping
	public List<TopicoDto> lista(String nomeCurso) {
		// System.out.println(nomeCurso);
		if (nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDto.converter(topicos);
		} else {
			List<Topico> topicos = topicoRepository.findAllByCurso_Nome(nomeCurso);
			return TopicoDto.converter(topicos);
		}

	}

	/**
	 * @GetMapping("/{id}") public ResponseEntity<Topico> findId(@PathVariable Long
	 * id){ return topicoRepository.findById(id).map(resp ->
	 * ResponseEntity.ok(resp)) .orElse(ResponseEntity.notFound().build()); }
	 **/

	@GetMapping("/{id}")
	public TopicoDto getId(@PathVariable Long id) {
		Topico topico = topicoRepository.getReferenceById(id);
		return new TopicoDto(topico);
	}

	@GetMapping("/detalhes/{id}")
	public MaisInfoDoTopicoDto getDetalhes(@PathVariable Long id) {
		Topico topico = topicoRepository.getReferenceById(id);
		return new MaisInfoDoTopicoDto(topico);
	}

	@PostMapping
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		// receber o form do TopicoForm (dto) e converter para salvar em topico
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);

		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
	
	@PutMapping("/{id}")
	@Transactional
	// @Transactional salva as alterações no banco de dados
	public ResponseEntity<TopicoDto> atualizar(@RequestBody @Valid AtualizarTopicoForm form, @PathVariable long id) {
		Topico topico = form.atualizar(id, topicoRepository);

		return ResponseEntity.ok(new TopicoDto(topico));
	}
}
