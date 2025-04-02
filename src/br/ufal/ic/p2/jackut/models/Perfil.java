package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.exceptions.AtributoNaoPreenchidoException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Representa o perfil de um usuário com atributos personalizáveis.
 * <p>
 * Permite armazenar e recuperar informações adicionais do usuário em formato chave-valor.
 * </p>
 *
 * @author IuryNogueira
 * @version 1.0
 * @see Serializable
 */
public class Perfil implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<String, String> atributos = new HashMap<>();

    /**
     * Recupera o valor de um atributo do perfil.
     *
     * @param chave Nome do atributo a ser recuperado
     * @return Valor associado à chave
     * @throws AtributoNaoPreenchidoException Se o atributo não existir
     */
    public String getAtributo(String chave) {
        if (!atributos.containsKey(chave)) {
            throw new AtributoNaoPreenchidoException();
        }
        return atributos.get(chave);
    }

    /**
     * Define ou atualiza um atributo no perfil.
     *
     * @param chave Nome do atributo
     * @param valor Novo valor do atributo
     */
    public void setAtributo(String chave, String valor) {
        atributos.put(chave, valor);
    }
}