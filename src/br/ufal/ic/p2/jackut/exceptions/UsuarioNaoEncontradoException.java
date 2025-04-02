package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando uma operação tenta acessar um usuário que não existe no sistema.
 *
 * @author IuryNogueira
 */
public class UsuarioNaoEncontradoException extends RuntimeException {
    /**
     * Constrói uma nova exceção com a mensagem padrão de usuário não encontrado.
     */
    public UsuarioNaoEncontradoException() {
        super("Usuário não cadastrado.");
    }
}