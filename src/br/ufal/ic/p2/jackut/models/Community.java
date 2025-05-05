package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.exceptions.UsuarioJaMembroException;
import java.io.Serializable;
import java.util.*;

/**
 * Representa uma comunidade no sistema Jackut, contendo informa��es b�sicas,
 * membros e um propriet�rio.
 *
 * <p>Esta classe implementa {@link Serializable} para permitir persist�ncia dos dados.</p>
 *
 * <p>Mant�m a ordem de inser��o dos membros atrav�s de {@link LinkedHashSet}.</p>
 *
 * @author [Seu Nome ou IuryNogueira]
 * @version 1.0
 */
public class Community implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String name;
    private String description;
    private final String owner;
    private final LinkedHashSet<String> members = new LinkedHashSet<>(); // Mant�m ordem de inser��o

    /**
     * Constr�i uma nova comunidade com informa��es b�sicas
     *
     * @param name Nome da comunidade
     * @param description Descri��o da comunidade
     * @param owner Login do usu�rio propriet�rio da comunidade
     */
    public Community(String name, String description, String owner) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.members.add(owner);
    }

    /**
     * Retorna o nome da comunidade
     *
     * @return Nome da comunidade (imut�vel)
     */
    public String getName() { return name; }

    /**
     * Define a descri��o da comunidade
     *
     * @param description Nova descri��o para a comunidade
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Adiciona um novo membro � comunidade
     *
     * @param login Login do usu�rio a ser adicionado
     * @throws UsuarioJaMembroException Se o usu�rio j� for membro da comunidade
     */
    public void addMember(String login) {
        if (!members.add(login)) {
            throw new UsuarioJaMembroException();
        }
    }

    /**
     * Retorna a descri��o atual da comunidade
     *
     * @return Descri��o da comunidade
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Retorna o propriet�rio da comunidade
     *
     * @return Login do usu�rio propriet�rio (imut�vel)
     */
    public String getOwner() {
        return this.owner;
    }

    /**
     * Retorna a lista de membros da comunidade em ordem de inser��o
     *
     * @return Lista de logins dos membros
     */
    public List<String> getMembers() {
        return new ArrayList<>(this.members);
    }

    /**
     * Retorna a lista de membros da comunidade em ordem de inser��o
     * (M�todo alternativo em portugu�s - considerar padroniza��o)
     *
     * @return Lista de logins dos membros
     */
    public List<String> getMembros() {
        return new ArrayList<>(members);
    }
}