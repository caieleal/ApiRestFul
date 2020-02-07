package com.example.ApiRestFul;

import com.example.ApiRestFul.Usuario.Usuario;
import com.example.ApiRestFul.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("teste")
public class UsuarioControllerTest {

    private static final ObjectMapper om = new ObjectMapper();


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioRepository mockRepository;

    @Before
    public void init(){
        Usuario usuario = new Usuario();
        usuario.setId(9L);
        usuario.setNome("teste");
        usuario.setEmail("testando@teste.com");
        usuario.setDataNascimento(LocalDate.of(1996,05,15));
        when(mockRepository.findById(9L)).thenReturn(Optional.of(usuario));
    }
    @Test
    public void getUsuario() throws Exception{

        mockMvc.perform(get("/usuario/9"))
                /*.andDo(print())*/
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(9)))
                .andExpect(jsonPath("$.nome", is("teste")))
                .andExpect(jsonPath("$.email", is("testando@teste.com")))
                .andExpect(jsonPath("$.dataNascimento", is("1996-05-15")));

        verify(mockRepository, times(1)).findById(9L);
    }
    @Test
    public void listUsuarios() throws Exception{

        List<Usuario> usuario = new ArrayList<>();
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("teste");
        usuario1.setEmail("teste@gmail.com");
        usuario1.setDataNascimento(LocalDate.of(2000,05,15));
        usuario.add(usuario1);

        when(mockRepository.findAll()).thenReturn(usuario);
        mockMvc.perform(get("/usuario")).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nome", is("teste")))
                .andExpect(jsonPath("$[0].email", is("teste@gmail.com")))
                .andExpect(jsonPath("$[0].dataNascimento", is("2000-05-15")));
                verify(mockRepository, times(1)).findAll();
    }
    @Test
    public void saveUsuario() throws Exception{

        UsuarioRequestSaveUp usuario2 = new UsuarioRequestSaveUp();
        usuario2.setNome("teste");
        usuario2.setEmail("testando@hotmail.com");
        usuario2.setDataNascimento("1999-01-12");

        Usuario usuario3 = new Usuario();
        usuario3.setId(2L);
        usuario3.setNome("teste");
        usuario3.setEmail("testando@hotmail.com");
        usuario3.setDataNascimento(LocalDate.of(1999,01,12));


        when(mockRepository.save(any(Usuario.class))).thenReturn(usuario3);

        mockMvc.perform(post("/usuario").content(om.writeValueAsString(usuario2)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.nome", is("teste")))
                .andExpect(jsonPath("$.email", is("testando@hotmail.com")))
                .andExpect(jsonPath("$.dataNascimento", is("1999-01-12")));
                verify(mockRepository, times(1)).save(any(Usuario.class));
    }
    @Test
    public void atualiUsuario()throws Exception {
        UsuarioRequestSaveUp usuario4 = new UsuarioRequestSaveUp();
        usuario4.setNome("teste3");
        usuario4.setEmail("testando@teste.com");
        usuario4.setDataNascimento("1999-01-12");

        Usuario upUsuario = new Usuario();
        upUsuario.setId(3L);
        upUsuario.setNome("teste3");
        upUsuario.setEmail("testando@teste.com");
        upUsuario.setDataNascimento(LocalDate.of(1999, 01, 12));

        when(mockRepository.findById(3L)).thenReturn(Optional.of(upUsuario));
        when(mockRepository.save(any(Usuario.class))).thenReturn(upUsuario);

        mockMvc.perform(put("/usuario/3").content(om.writeValueAsString(usuario4)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.nome", is("teste3")))
                .andExpect(jsonPath("$.email", is("testando@teste.com")))
                .andExpect(jsonPath("$.dataNascimento", is("1999-01-12")));

    }
    @Test
    public void deleteUsuario() throws Exception{
        Usuario usuario = new Usuario();
        when(mockRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(mockRepository).deleteById(1L);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/usuario/1").
                contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }



 }
