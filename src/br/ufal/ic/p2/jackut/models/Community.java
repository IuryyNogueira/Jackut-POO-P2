package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.exceptions.UsuarioJaMembroException;
import java.io.Serializable;
import java.util.*;

/**
 * Representa uma comunidade no sistema Jackut, contendo informações básicas,
 * membros e um proprietário.
 *
 * <p>Esta classe implementa {@link Serializable} para permitir persistência dos dados.</p>
 *
 * <p>Mantém a ordem de inserção dos membros através de {@link LinkedHashSet}.</p>
 *
 * @author [Seu Nome ou IuryNogueira]
 * @version 1.0
 */
public class Community implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String name;
    private String description;
    private final String owner;
    private final LinkedHashSet<String> members = new LinkedHashSet<>(); // Mantém ordem de inserção

    /**
     * Constrói uma nova comunidade com informações básicas
     *
     * @param name Nome da comunidade
     * @param description Descrição da comunidade
     * @param owner Login do usuário proprietário da comunidade
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
     * @return Nome da comunidade (imutável)
     */
    public String getName() { return name; }

    /**
     * Define a descrição da comunidade
     *
     * @param description Nova descrição para a comunidade
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Adiciona um novo membro à comunidade
     *
     * @param login Login do usuário a ser adicionado
     * @throws UsuarioJaMembroException Se o usuário já for membro da comunidade
     */
    public void addMember(String login) {
        if (!members.add(login)) {
            throw new UsuarioJaMembroException();
        }
    }

    /**
     * Retorna a descrição atual da comunidade
     *
     * @return Descrição da comunidade
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Retorna o proprietário da comunidade
     *
     * @return Login do usuário proprietário (imutável)
     */
    public String getOwner() {
        return this.owner;
    }

    /**
     * Retorna a lista de membros da comunidade em ordem de inserção
     *
     * @return Lista de logins dos membros
     */
    public List<String> getMembers() {
        return new ArrayList<>(this.members);
    }

    /**
     * Retorna a lista de membros da comunidade em ordem de inserção
     * (Método alternativo em português - considerar padronização)
     *
     * @return Lista de logins dos membros
     */
    public List<String> getMembros() {
        return new ArrayList<>(members);
    }
}