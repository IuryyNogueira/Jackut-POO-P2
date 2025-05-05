package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando uma tentativa de adicionar uma paquera falha
 * porque o usuário já está na lista de paqueras.
 *
 * <p>Esta é uma exceção não verificada (unchecked) que estende {@link RuntimeException},
 * indicando que a relação de paquera já existe entre os usuários.</p>
 *
 * @author [Seu Nome ou IuryNogueira]
 * @version 1.0
 * @since 1.0
 * @see RuntimeException
 */
public class PaqueraExistenteException extends RuntimeException {

    /**
     * Constrói uma nova PaqueraExistenteException com a mensagem padrão
     * indicando que o usuário já está na lista de paqueras.
     */
    public PaqueraExistenteException() {
        super("Usuário já está adicionado como paquera.");
    }
}