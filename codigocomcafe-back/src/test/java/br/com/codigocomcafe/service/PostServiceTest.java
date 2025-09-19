package br.com.codigocomcafe.service;

// Importações estáticas do Mockito para facilitar o uso de any(), anyLong(), verify()
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.List;
import java.util.Optional;

// Importações do JUnit 5
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

// Importações do Mockito
import org.mockito.InjectMocks; // injeta os mocks no objeto de teste
import org.mockito.Mock;        // cria mocks
import org.mockito.junit.jupiter.MockitoExtension; // habilita integração Mockito + JUnit5

// Importações de classes do projeto
import br.com.codigocomcafe.exception.ResourceNotFoundException;
import br.com.codigocomcafe.model.PostModel;
import br.com.codigocomcafe.repository.PostRepository;

// AssertJ e JUnit Assertions
import static org.assertj.core.api.Assertions.*; // assertThat, assertThatThrownBy
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*; // verify, when, times

// Configura a classe para usar Mockito com JUnit5
@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock // Cria um mock do PostRepository (não acessa BD real)
    private PostRepository postRepository;

    @InjectMocks // Injeta o mock dentro do PostService
    private PostService postService;

    // ============================
    // TESTE: SALVAR POST
    // ============================
    @Test
    void deveSalvarPost() {
        // Cria um post "novo" sem id, simulando entrada do usuário
        PostModel novoPost = new PostModel(null, 
            "titulo", "conteudo","imagem","video","RASCUNHO",
            "2025-09-19T20:23:19.613Z","2025-09-19T20:23:19.613Z","2025-09-19T20:23:19.613Z",
            "categoria");

        // Simula o retorno do repository após salvar, agora com id preenchido
        PostModel salvoComId = new PostModel(1L, 
            "titulo", "conteudo","imagem","video","RASCUNHO",
            "2025-09-19T20:23:19.613Z","2025-09-19T20:23:19.613Z","2025-09-19T20:23:19.613Z",
            "categoria");

        // Configura o comportamento do mock: sempre que save() for chamado, retorna salvoComId
        when(postRepository.save(any(PostModel.class))).thenReturn(salvoComId);

        // EXECUÇÃO: chama o método do service que queremos testar
        PostModel resultado = postService.cadastraPost(novoPost);

        // VALIDAÇÃO: garante que os campos esperados estão corretos
        assertThat(resultado.getId()).isEqualTo(1L); // id gerado
        assertThat(resultado.getTitulo()).isEqualTo("titulo"); // título correto
        assertThat(resultado.getCategoria()).isEqualTo("categoria"); // categoria correta

        // Verifica se o repository.save() foi chamado exatamente 1 vez
        verify(postRepository, times(1)).save(any(PostModel.class));
    }

    // ============================
    // TESTE: LISTAR TODOS OS POSTS
    // ============================
    @Test
    void deveListarPosts() {
        // Mock: retorna 2 posts simulados
        when(postRepository.findAll()).thenReturn(List.of(
            new PostModel(1L, "titulo1", "conteudo1","imagem1","video1","RASCUNHO",
                "dataCriacao1","dataAtualizacao1","dataPublicacao1","categoria1"),
            new PostModel(2L, "titulo2", "conteudo2","imagem2","video2","PUBLICADO",
                "dataCriacao2","dataAtualizacao2","dataPublicacao2","categoria2")
        ));

        // EXECUÇÃO
        List<PostModel> lista = postService.buscarTodosPosts();

        // VALIDAÇÃO: lista tem 2 elementos e repository.findAll() foi chamado
        assertThat(lista).hasSize(2);
        verify(postRepository, times(1)).findAll();
    }

    // ============================
    // TESTE: BUSCAR POST POR ID EXISTENTE
    // ============================
    @Test
    void deveBuscarPorIdQuandoExistir() {
        // Cenário: post existente
        PostModel p = new PostModel(1L, "titulo1", "conteudo1","imagem1","video1","RASCUNHO",
                "dataCriacao1","dataAtualizacao1","dataPublicacao1","categoria1");
        when(postRepository.findById(1L)).thenReturn(Optional.of(p));

        // EXECUÇÃO
        Optional<PostModel> encontrado = postService.buscarPorId(1L);

        // VALIDAÇÃO
        assertThat(encontrado).isPresent(); // Optional não está vazio
        assertThat(encontrado.get().getTitulo()).isEqualTo("titulo1"); // título correto
        verify(postRepository, times(1)).findById(1L); // método do repo chamado 1 vez
    }

    // ============================
    // TESTE: BUSCAR POST POR ID NÃO EXISTENTE
    // ============================
    @Test
    void deveRetornarVazioQuandoNaoExistir() {
        // Cenário: post não existe
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        // EXECUÇÃO
        Optional<PostModel> encontrado = postService.buscarPorId(99L);

        // VALIDAÇÃO: Optional vazio
        assertThat(encontrado).isEmpty();
        verify(postRepository, times(1)).findById(99L);
    }

    // ============================
    // TESTE: ATUALIZAR POST EXISTENTE
    // ============================
    @Test
    void deveAtualizarQuandoExistir() {
        // Cenário: post existente no BD
        PostModel existente = new PostModel(1L, "Old", "Old content","img","vid","RASCUNHO",
                "dataCriacao","dataAtualizacao","dataPublicacao","categoria");
        PostModel atualizado = new PostModel(null, "New Title", "New content","img2","vid2","PUBLICADO",
                "dataCriacao","dataAtualizacao","dataPublicacao","categoria");

        // Mock do findById: retorna o existente
        when(postRepository.findById(1L)).thenReturn(Optional.of(existente));
        // Mock do save: retorna o objeto que foi passado para salvar
        when(postRepository.save(any(PostModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // EXECUÇÃO
        PostModel resultado = postService.atualizarPost(1L, atualizado);

        // VALIDAÇÃO: campos atualizados corretamente
        assertThat(resultado.getTitulo()).isEqualTo("New Title");
        assertThat(resultado.getConteudo()).isEqualTo("New content");
        assertThat(resultado.getStatus()).isEqualTo("PUBLICADO");
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).save(any(PostModel.class));
    }

    // ============================
    // TESTE: ATUALIZAR POST NÃO EXISTENTE
    // ============================
    @Test
    void deveLancarExceptionAoAtualizarQuandoNaoExistir() {
        // Cenário: post não existe
        when(postRepository.findById(5L)).thenReturn(Optional.empty());

        // EXECUÇÃO & VALIDAÇÃO: espera ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, 
            () -> postService.atualizarPost(5L, new PostModel(null, "X","Y","","","","","","","")));

        verify(postRepository, times(1)).findById(5L);
        verify(postRepository, never()).save(any(PostModel.class)); // nunca chamou save
    }

    // ============================
    // TESTE: DELETAR POST EXISTENTE
    // ============================
    @Test
    void deveDeletarQuandoExistir() {
        // Cenário: post existe
        when(postRepository.existsById(1L)).thenReturn(true);
        doNothing().when(postRepository).deleteById(1L);

        // EXECUÇÃO
        postService.deletarPorId(1L);

        // VALIDAÇÃO: métodos do repository chamados corretamente
        verify(postRepository, times(1)).existsById(1L);
        verify(postRepository, times(1)).deleteById(1L);
    }

    // ============================
    // TESTE: DELETAR POST NÃO EXISTENTE
    // ============================
    @Test
    void deveLancarExceptionAoDeletarQuandoNaoExistir() {
        // Cenário: post não existe
        when(postRepository.existsById(3L)).thenReturn(false);

        // EXECUÇÃO & VALIDAÇÃO: espera exceção
        assertThrows(ResourceNotFoundException.class, 
            () -> postService.deletarPorId(3L));

        verify(postRepository, times(1)).existsById(3L);
        verify(postRepository, never()).deleteById(anyLong());
    }

}
