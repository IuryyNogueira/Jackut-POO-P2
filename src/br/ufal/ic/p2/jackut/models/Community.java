package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.exceptions.UsuarioJaMembroException;
import java.io.Serializable;
import java.util.*;

public class Community implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String name;
    private String description;
    private final String owner;
    private final LinkedHashSet<String> members = new LinkedHashSet<>(); // Mantém ordem de inserção

    public Community(String name, String description, String owner) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.members.add(owner);
    }

    // Getters
    public String getName() { return name; }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addMember(String login) {
        if (!members.add(login)) {
            throw new UsuarioJaMembroException();
        }
    }

    public String getDescription() {
        return this.description;
    }

    public String getOwner() {
        return this.owner;
    }

    public List<String> getMembers() {
        return new ArrayList<>(this.members);
    }
}