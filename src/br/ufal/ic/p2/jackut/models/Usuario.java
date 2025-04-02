package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.exceptions.*;
import java.io.Serializable;
import java.util.*;

/**
 * Representa um usuário do sistema Jackut, contendo informações de perfil,
 * relacionamentos de amizade, convites e recados.
 *
 * <p>Esta classe implementa {@link Serializable} para permitir persistência dos dados.</p>
 *
 * @author IuryNogueira
 * @version 1.0
 * @see Perfil
 * @see Recado
 */
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String login;
    private final String senha;
    private final Perfil perfil;
    private final Set<String> amigos = new LinkedHashSet<>();
    private final Map<String, ConviteStatus> convites = new HashMap<>();
    private final Queue<Recado> recados = new LinkedList<>();

    /**
     * Enumeração que representa o status de um convite de amizade
     */
    public enum ConviteStatus {
        /** Convite enviado pelo usuário atual */
        ENVIADO,
        /** Convite recebido de outro usuário */
        RECEBIDO
    }

    /**
     * Constrói um novo usuário com informações básicas
     *
     * @param login Identificador único do usuário
     * @param senha Senha de acesso
     * @param nome Nome completo do usuário (será armazenado no perfil)
     */
    public Usuario(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.perfil = new Perfil();
        this.perfil.setAtributo("nome", nome);
    }

    /**
     * @return Login do usuário (imutável)
     */
    public String getLogin() { return login; }

    /**
     * @return Senha do usuário (imutável)
     */
    public String getSenha() { return senha; }

    /**
     * @return Perfil público do usuário
     */
    public Perfil getPerfil() { return perfil; }

    /**
     * Retorna uma visão imutável da lista de amigos
     *
     * @return Conjunto não modificável de logins de amigos
     */
    public Set<String> getAmigos() {
        return Collections.unmodifiableSet(amigos);
    }

    /**
     * Adiciona um usuário à lista de amigos
     *
     * @param loginAmigo Login do usuário a ser adicionado como amigo
     */
    public void adicionarAmigo(String loginAmigo) {
        amigos.add(loginAmigo);
    }

    /**
     * Verifica se existe um convite enviado para outro usuário
     *
     * @param loginAmigo Login do usuário alvo
     * @return true se houver convite ativo enviado para este usuário
     */
    public boolean temConvitePara(String loginAmigo) {
        return convites.containsKey(loginAmigo) && convites.get(loginAmigo) == ConviteStatus.ENVIADO;
    }

    /**
     * Verifica se existe um convite recebido de outro usuário
     *
     * @param loginAmigo Login do usuário remetente
     * @return true se houver convite recebido deste usuário
     */
    public boolean temConviteDe(String loginAmigo) {
        return convites.containsKey(loginAmigo) && convites.get(loginAmigo) == ConviteStatus.RECEBIDO;
    }

    /**
     * Registra um novo convite de amizade
     *
     * @param loginAmigo Login do usuário relacionado ao convite
     * @param status Status do convite ({@link ConviteStatus})
     */
    public void adicionarConvite(String loginAmigo, ConviteStatus status) {
        convites.put(loginAmigo, status);
    }

    /**
     * Remove um convite existente
     *
     * @param loginAmigo Login do usuário relacionado ao convite
     */
    public void removerConvite(String loginAmigo) {
        convites.remove(loginAmigo);
    }

    /**
     * Adiciona um novo recado à fila de mensagens
     *
     * @param recado Recado a ser armazenado
     */
    public void adicionarRecado(Recado recado) {
        recados.add(recado);
    }

    /**
     * Lê e remove o próximo recado da fila
     *
     * @return Conteúdo do recado no formato "mensagem"
     * @throws SemRecadosException Se não houver recados disponíveis
     */
    public String lerRecado() {
        Recado recado = recados.poll();
        if (recado == null) throw new SemRecadosException();
        return recado.toString();
    }

    /**
     * Retorna a lista de amigos em ordem de adição
     *
     * @return Lista ordenada de logins de amigos
     */
    public List<String> getAmigosOrdenados() {
        return new ArrayList<>(amigos);
    }

    /**
     * Retorna uma visão imutável dos convites
     *
     * @return Mapa não modificável de convites (chave: login, valor: status)
     */
    public Map<String, ConviteStatus> getConvites() {
        return Collections.unmodifiableMap(convites);
    }
}