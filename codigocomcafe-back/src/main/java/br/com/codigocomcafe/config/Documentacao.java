package br.com.codigocomcafe.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Documentacao {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API do Blog Código com Café") // Título
                .description("API para gerenciamento de posts, categorias e conteúdos do blog Código com Café, permitindo criar, atualizar, listar e deletar informações de forma segura e organizada.") // Descrição
                .version("1.0")
                .contact(new Contact()
                    .name("Carolina Mesquita")               // Nome do responsável
                    .email("carolti2013@gmail.com")         // E-mail de contato
                    .url("https://www.codigocomcafe.com")   // Site ou link do blog
                )
            );
    }
}
