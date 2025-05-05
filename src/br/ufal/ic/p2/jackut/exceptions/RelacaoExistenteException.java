package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando uma tentativa de criar uma relação entre usuários falha
 * porque a relação específica já existe.
 *
 * <p>Esta é uma exceção não verificada (unchecked) que estende {@link RuntimeException},
 * podendo ser utilizada para diversos tipos de relações existentes entre usuários.</p>
 *
 * <p>A mensagem específica do erro pode ser personalizada conforme o tipo de relação.</p>
 *
 * @author [Seu Nome ou IuryNogueira]
 * @version 1.0
 * @since 1.0
 * @see RuntimeException
 */
public class RelacaoExistenteException extends RuntimeException {

    /**
     * Constrói uma nova RelacaoExistenteException com uma mensagem personalizada.
     *
     * @param mensagem a mensagem detalhada que descreve qual relação específica já existe.
     *                 A mensagem é salva para posterior recuperação pelo método {@link #getMessage()}.
     */
    public RelacaoExistenteException(String mensagem) {
        super(mensagem);
    }
}