package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando ocorre um erro durante operações de persistência de dados.
 * <p>
 * Pode ser causada por falhas de I/O ou problemas de serialização/desserialização.
 * </p>
 *
 * @author IuryNogueira
 */
public class PersistenciaException extends RuntimeException {
    /**
     * Constrói a exceção com mensagem padrão sobre falha na persistência
     */
    public PersistenciaException() {
        super("Erro na persistência de dados.");
    }
}