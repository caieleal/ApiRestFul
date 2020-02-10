package com.example.ApiRestFul.controller;

import com.example.ApiRestFul.Usuario.Usuario;
import com.example.ApiRestFul.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping(path = "/usuario")
    public List<Usuario> Get(){

        return usuarioRepository.findAll();
    }
    @GetMapping(path = "/usuario/{id}")
    public ResponseEntity<Usuario> GetBydId(@PathVariable(value = "id")@NotNull long id){
        Optional<Usuario> usuario = usuarioRepository.findById(id);

        if (usuario.isPresent()){
            return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/usuario")
    public Usuario Post(@Valid @RequestBody Usuario usuario){

        return usuarioRepository.save(usuario);


    }

    @PutMapping(path = "/usuario/{id}")
    public ResponseEntity<Usuario> Put(@PathVariable(value = "id")@NotNull long id, @Valid @RequestBody Usuario usu){
        Optional<Usuario> usuarios = usuarioRepository.findById(id);
        usu.setId(usuarios.get().getId());

            usuarioRepository.save(usu);
            return ResponseEntity.ok(usu);

        }
    @DeleteMapping(path = "/usuario/{id}")
    public ResponseEntity<Object> Delete(@PathVariable(value = "id")@NotNull long id){
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()){
            usuarioRepository.delete(usuario.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
