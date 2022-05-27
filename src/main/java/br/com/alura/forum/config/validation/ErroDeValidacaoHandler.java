package br.com.alura.forum.config.validation;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 
 * @author Hanely
 * Classe para interceptar as exceptions que forem lançadas nos métodos das classes controller
 * e chama o interceptador controllerAdvice
 * 
 * Para tratar os erros de validação do Bean Validation e personalizar o JSON, que será devolvido 
 * ao cliente da API, com as mensagens de erro, devemos criar um método na classe @RestControllerAdvice 
 * e anotá-lo com @ExceptionHandler e @ResponseStatus
 */
@RestControllerAdvice
public class ErroDeValidacaoHandler {
	
	@Autowired
	private MessageSource messageSource;
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	//qualquer exception que ocorre no RestController o MethodArgumentNotValidException faz o tratamento
	public List<ErroDeFormDto> handle(MethodArgumentNotValidException exception) {
		List<ErroDeFormDto> dto = new ArrayList<>();
		//lista contendo todos os erros
		
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		fieldErrors.forEach(e -> {
			String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			ErroDeFormDto erro = new ErroDeFormDto(e.getField(), mensagem);
			dto.add(erro);
		});
		
		return dto;
	}
}
