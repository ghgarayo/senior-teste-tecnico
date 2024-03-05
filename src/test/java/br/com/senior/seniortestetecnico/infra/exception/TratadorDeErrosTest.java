package br.com.senior.seniortestetecnico.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TratadorDeErrosTest {

    @InjectMocks
    private TratadorDeErros tratadorDeErros;

    @Mock
    private MethodArgumentNotValidException exception;

    @Mock
    private FieldError fieldError;

    @Test
    @DisplayName("Deve tratar MethodArgumentNotValidException e retornar resposta bad request")
    void testMethodArgumentNotValidException() {
        ResponseEntity<?> response = tratadorDeErros.tratarErro(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Deve tratar IllegalArgumentException e retornar resposta bad request")
    void testIllegalArgumentException() {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Illegal argument");

        ResponseEntity<?> response = tratadorDeErros.tratarErro(illegalArgumentException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Illegal argument", response.getBody());
    }

    @Test
    @DisplayName("Deve tratar EntityNotFoundException e retornar resposta not found")
    void testeEntityNotFoundException() {
        EntityNotFoundException entityNotFoundException = new EntityNotFoundException("Entity not found");

        ResponseEntity<?> response = tratadorDeErros.tratarErro(entityNotFoundException);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Entity not found", response.getBody());
    }
}