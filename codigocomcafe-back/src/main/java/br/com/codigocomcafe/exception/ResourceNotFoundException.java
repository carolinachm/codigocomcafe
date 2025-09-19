package br.com.codigocomcafe.exception; // pacote para exceções

public class ResourceNotFoundException extends RuntimeException { // runtime para não obrigar try/catch
    public ResourceNotFoundException(String message) { // construtor que recebe mensagem
        super(message); // passa mensagem para RuntimeException
    }
}
