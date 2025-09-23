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

public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Criar novo post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<PostModel> cadastraPost(
            @RequestParam String titulo,
            @RequestParam String autor,
            @RequestParam String conteudo,
            @RequestParam("imagem") MultipartFile imagemFile,
            @RequestParam(value = "video", required = false) MultipartFile videoFile,
            @RequestParam Status status,
            @RequestParam(required = false) Long categoriaId) {

        try {
            // 🔹 Validação básica
            if (titulo == null || titulo.isBlank() ||
                    autor == null || autor.isBlank() ||
                    conteudo == null || conteudo.isBlank() ||
                    imagemFile == null || imagemFile.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            String nomeImagem = uploadService.armazenarArquivo(imagemFile);
            String nomeVideo = videoFile != null ? uploadService.armazenarArquivo(videoFile) : null;

            // 🔹 Buscar categoria se fornecida
            CategoriaModel categoria = null;
            if (categoriaId != null) {
                categoria = categoriaService.buscarPorId(categoriaId);
                if (categoria == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
            }

            // 🔹 Montar o post
            PostModel post = new PostModel();
            post.setTitulo(titulo);
            post.setAutor(autor);
            post.setConteudo(conteudo);
            post.setImagem(nomeImagem);
            post.setVideo(nomeVideo);
            post.setStatus(status);
            post.setDataCriacao(LocalDateTime.now());
            post.setDataAtualizacao(LocalDateTime.now());
            post.setDataPublicacao(LocalDateTime.now());
            post.setCategoriaModel(categoria); // ⚠️ Aqui é o correto

            // 🔹 Salvar no banco
            PostModel salvo = postService.cadastraPost(post);

            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);

        } catch (Exception e) {
            e.printStackTrace();
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

    @Operation(summary = "Buscar posts por nome da categoria")
    @GetMapping("/posts/categoria/{nome}")
    public ResponseEntity<List<PostModel>> buscarPostsPorCategoria(@PathVariable String nome) {
        try {
            CategoriaModel categoria = categoriaService.buscarPorNome(nome);
            List<PostModel> posts = postService.buscarPorCategoria(categoria);

            if (posts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(posts, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
