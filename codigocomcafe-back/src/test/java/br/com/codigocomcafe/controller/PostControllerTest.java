package br.com.codigocomcafe.controller; 
// define o pacote deste arquivo de teste (organização do projeto)

import br.com.codigocomcafe.model.PostModel; 
// importa a classe de modelo PostModel (antes era Post)

import br.com.codigocomcafe.service.PostService; 
// importa o service que será mockado nos testes

import br.com.codigocomcafe.exception.ResourceNotFoundException; 
// importa a exceção custom usada em cenários de erro

import com.fasterxml.jackson.databind.ObjectMapper; 
// importa o ObjectMapper para converter objetos Java <-> JSON

import org.junit.jupiter.api.Test; 
// importa a anotação @Test do JUnit 5

import org.springframework.beans.factory.annotation.Autowired; 
// importa @Autowired para injeção de dependência em testes

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest; 
// importa @WebMvcTest para teste slice do controller (sobe só camada web)

import org.springframework.boot.test.mock.mockito.MockBean; 
// importa @MockBean para inserir mocks no contexto Spring

import org.springframework.http.MediaType; 
// importa MediaType para definir content-type das requisições

import org.springframework.test.web.servlet.MockMvc; 
// importa MockMvc, que simula requisições HTTP sem subir servidor real

import java.util.List; 
// importa List do Java

import java.util.Optional; 
// importa Optional para representar possíveis ausências de valor

// importações estáticas para facilitar escrita dos mocks e das asserções do MockMvc
import static org.mockito.ArgumentMatchers.any; 
// importa staticamente any(...) para usar em when(... any(...))

import static org.mockito.Mockito.when; 
// importa staticamente when(...) do Mockito para definir comportamentos

import static org.mockito.Mockito.doThrow; 
// importa staticamente doThrow(...) para simular exceções em void methods

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; 
// importa staticamente verbos HTTP (get, post, put, delete, etc.) para MockMvc

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; 
// importa staticamente matchers (status, jsonPath, etc.) para validar respostas

@WebMvcTest(PostController.class) // sobe só o contexto MVC para o PostController (isolado)
class PostControllerTest { // define a classe de teste do controller

    @Autowired
    private MockMvc mockMvc; // injeta o MockMvc para simular requisições HTTP ao controller

    @MockBean
    private PostService postService; // cria um mock do PostService e registra no contexto Spring

    @Autowired
    private ObjectMapper objectMapper; // injeta ObjectMapper para serializar/deserializar JSON

    @Test
    void deveSalvarPost() throws Exception { // teste que verifica o endpoint POST /posts
        // prepara o objeto de teste (entrada/retorno esperado)
        PostModel postModel = new PostModel(1L, "Título", "Conteúdo");PostModel novoPost = new PostModel(null, 
            "titulo", "conteudo","imagem","video","RASCUNHO",
            "2025-09-19T20:23:19.613Z","2025-09-19T20:23:19.613Z","2025-09-19T20:23:19.613Z",
            "categoria");
        // define comportamento do mock: quando postService.salvar(qualquer PostModel) for chamado, retorna postModel
        when(postService.cadastraPost(any(PostModel.class))).thenReturn(postModel);

        // executa a requisição POST /posts com o JSON do postModel
        mockMvc.perform(
                post("/posts") // usa o builder post(...) para a rota /posts
                        .contentType(MediaType.APPLICATION_JSON) // define header Content-Type: application/json
                        .content(objectMapper.writeValueAsString(postModel)) // serializa postModel para o corpo JSON
        )
                .andExpect(status().isCreated()) // espera HTTP 201 Created
                .andExpect(jsonPath("$.id").value(1L)) // valida que o JSON de resposta tem id = 1
                .andExpect(jsonPath("$.titulo").value("Título")); // valida que o título veio correto
    }

    @Test
    void deveListarTodos() throws Exception { // teste que verifica o endpoint GET /posts
        // quando postService.listarTodos() for chamado, retorna uma lista com dois PostModel
        when(postService.buscarTodosPosts()).thenReturn(List.of(
              new PostModel(1L, "titulo1", "conteudo1","imagem1","video1","RASCUNHO",
                "dataCriacao1","dataAtualizacao1","dataPublicacao1","categoria1"),
            new PostModel(2L, "titulo2", "conteudo2","imagem2","video2","PUBLICADO",
                "dataCriacao2","dataAtualizacao2","dataPublicacao2","categoria2")
        ));

        // executa a requisição GET /posts
        mockMvc.perform(get("/posts")) // faz GET na rota /posts
                .andExpect(status().isOk()) // espera HTTP 200 OK
                .andExpect(jsonPath("$.length()").value(2)); // espera que o array JSON tenha tamanho 2
    }

    @Test
    void deveBuscarPorIdExistente() throws Exception { // teste GET /posts/{id} quando existe
        // cria objeto PostModel de exemplo
        PostModel postModel = new PostModel(1L, "Título", "Conteúdo");
        // mock: ao chamar buscarPorId(1L), retorna Optional.of(postModel)
        when(postService.buscarPorId(1L)).thenReturn(Optional.of(postModel));

        // executa GET /posts/1
        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk()) // espera 200 OK
                .andExpect(jsonPath("$.id").value(1L)); // valida id no JSON de resposta
    }

    @Test
    void deveRetornar404AoBuscarPorIdInexistente() throws Exception { // teste GET /posts/{id} quando não existe
        // mock: ao chamar buscarPorId(99L), retorna Optional.empty()
        when(postService.buscarPorId(99L)).thenReturn(Optional.empty());

        // executa GET /posts/99 e espera 404 Not Found
        mockMvc.perform(get("/posts/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarPostExistente() throws Exception { // teste PUT /posts/{id} quando existe
        // prepara objeto com dados atualizados (retorno esperado)
        PostModel atualizado = new PostModel(1L, "Novo Título", "Novo Conteúdo");
        // mock: quando service.atualizar(1L, atualizado) for chamado, retorna o objeto atualizado
        when(postService.atualizarPost(1L, atualizado)).thenReturn(atualizado);

        // executa PUT /posts/1 com o JSON do objeto atualizado
        mockMvc.perform(put("/posts/1")
                .contentType(MediaType.APPLICATION_JSON) // Content-Type JSON
                .content(objectMapper.writeValueAsString(atualizado)) // corpo com JSON do objeto atualizado
        )
                .andExpect(status().isOk()) // espera 200 OK
                .andExpect(jsonPath("$.titulo").value("Novo Título")); // valida que o título foi atualizado na resposta
    }

    @Test
    void deveRetornar404AoAtualizarInexistente() throws Exception { // teste PUT /posts/{id} quando não existe
        // prepara objeto com dados a atualizar (não possui id porque é corpo de atualização)
        PostModel atualizado = new PostModel(null, "Novo Título", "Novo Conteúdo");
        // mock: quando tentar atualizar id 99 lança ResourceNotFoundException
        when(postService.atualizarPost(99L, atualizado)).thenThrow(new ResourceNotFoundException("not found"));

        // executa PUT /posts/99 e espera 404 Not Found
        mockMvc.perform(put("/posts/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atualizado))
        )
                .andExpect(status().isNotFound());
    }

    @Test
    void deveDeletarPostExistente() throws Exception { // teste DELETE /posts/{id} quando existe
        // executa DELETE /posts/1 (não precisa mockar nada se o service não lançar)
        mockMvc.perform(delete("/posts/1"))
                .andExpect(status().isNoContent()); // espera 204 No Content
    }

    @Test
    void deveRetornar404AoDeletarInexistente() throws Exception { // teste DELETE /posts/{id} quando não existe
        // mock: ao chamar postService.deletar(99L) vamos forçar uma exceção
        doThrow(new ResourceNotFoundException("not found")).when(postService).deletarPorId(99L);

        // executa DELETE /posts/99 e espera 404 Not Found
        mockMvc.perform(delete("/posts/99"))
                .andExpect(status().isNotFound());
    }
}
