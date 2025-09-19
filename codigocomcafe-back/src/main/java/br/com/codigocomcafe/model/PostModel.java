package br.com.codigocomcafe.model;

import br.com.codigocomcafe.enumerador.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "tb_post")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(columnDefinition = "TEXT") // permite salvar textos longos
    private String conteudo;

    @Lob
    @Column(name = "imagem")
    private byte[] imagem;

    @Lob
    @Column(name = "video")
    private byte[] video;

    @Enumerated(EnumType.STRING) // salva o enum como texto
    private Status status;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private LocalDateTime dataPublicacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false) // chave estrangeira
    private CategoriaModel categoria;
}
