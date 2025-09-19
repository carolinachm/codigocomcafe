package br.com.codigocomcafe.model;

import br.com.codigocomcafe.enumerador.TipoCategoria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "tb_categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(columnDefinition = "TEXT") // deixa descrição mais flexível
    private String descricao;

    @Enumerated(EnumType.STRING) // salva o enum como texto no banco
    private TipoCategoria tipoCategoria;

    
}
