package br.com.codigocomcafe.controller;

import br.com.codigocomcafe.enumerador.Status;
import br.com.codigocomcafe.model.CategoriaModel;
import br.com.codigocomcafe.model.PostModel;
import br.com.codigocomcafe.service.CategoriaService;
import br.com.codigocomcafe.service.PostService;
import br.com.codigocomcafe.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Post", description = "Endpoints para gerenciamento de posts")
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private CategoriaService categoriaService;

    // ------------------- CREATE -------------------
    @Operation(summary = "Criar novo post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostModel> cadastraPost(
            @RequestParam String titulo,
            @RequestParam String conteudo,
            @RequestParam("imagem") MultipartFile imagemFile,
            @RequestParam(value = "video", required = false) MultipartFile videoFile,
            @RequestParam Status status,
            @RequestParam Long categoriaId) {

        try {
            byte[] imagemBytes = imagemFile != null ? imagemFile.getBytes() : null;
            byte[] videoBytes = videoFile != null ? videoFile.getBytes() : null;

            CategoriaModel categoria = categoriaService.buscarPorId(categoriaId);

            PostModel post = new PostModel();
            post.setTitulo(titulo);
            post.setConteudo(conteudo);
            post.setImagem(imagemBytes);
            post.setVideo(videoBytes);
            post.setStatus(status);
            post.setDataCriacao(LocalDateTime.now());
            post.setDataAtualizacao(LocalDateTime.now());
            post.setDataPublicacao(LocalDateTime.now());
            post.setCategoria(categoria);

            PostModel salvo = postService.cadastraPost(post);

            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ------------------- READ ALL -------------------
    @Operation(summary = "Listar todos os posts")
    @GetMapping("/posts")
    public ResponseEntity<List<PostModel>> buscarTodosPosts() {
        List<PostModel> todosPosts = postService.buscarTodosPosts();
        return new ResponseEntity<>(todosPosts, HttpStatus.OK);
    }

    @Operation(summary = "Buscar post por ID")
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostModel> buscarPorId(@PathVariable Long id) {
        return postService.buscarPorId(id)
                .map(post -> new ResponseEntity<>(post, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // ------------------- UPDATE -------------------
    @Operation(summary = "Atualizar post existente")
    @PutMapping("/posts/{id}")
    public ResponseEntity<PostModel> atualizarPost(@PathVariable Long id, @RequestBody PostModel postModel) {
        if (!postService.buscarPorId(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        postModel.setId(id);
        PostModel postAtualizado = postService.atualizarPost(postModel);
        return new ResponseEntity<>(postAtualizado, HttpStatus.OK);
    }

    // ------------------- DELETE -------------------
    @Operation(summary = "Deletar post por ID")
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletarPost(@PathVariable Long id) {
        if (!postService.buscarPorId(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        postService.deletarPorId(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
