package br.com.todoit.api.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("API Rest todo.it")
                        .description("""
                        Bem-vindo à API Rest do todo.it — o back-end de um projeto full-stack para o gerenciamento de tarefas.
                        
                        Esta aplicação oferece autenticação com JWT, cadastro e login de usuários, além de uma gestão prática e eficiente de tarefas semanais.
        
                        Funcionalidades:
                        - Cadastro e autenticação de usuários
                        - Criação, atualização, visualização e remoção de tarefas
                        - Definição de prioridades e horários das tarefas
                        - Organização semanal das atividades
        
                        Desenvolvido com Java e Spring Boot, utilizando boas práticas de arquitetura, segurança e documentação.
                        """)
                        .contact(new Contact()
                                .name("Back-End Developer")
                                .email("lucasgoncalvesjava@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://todo.it/api/licenca")));
    }
}
