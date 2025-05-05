package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.exceptions.*;
import java.io.Serializable;
import java.util.*;
import br.ufal.ic.p2.jackut.exceptions.RelacaoExistenteException;

/**
 * Representa um usuário do sistema Jackut, contendo informações de perfil,
 * relacionamentos de amizade, convites, recados e outros tipos de relacionamentos.
 *
 * <p>Esta classe implementa {@link Serializable} para permitir persistência dos dados.</p>
 *
 * @author IuryNogueira
 * @version 1.1
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

    private LinkedHashSet<String> comunidades = new LinkedHashSet<>();

    /**
     * Adiciona o usuário a uma comunidade
     *
     * @param nomeComunidade Nome da comunidade a ser adicionada
     */
    public void adicionarComunidade(String nomeComunidade) {
        comunidades.add(nomeComunidade);
    }

    /**
     * Retorna as comunidades do usuário
     *
     * @return Conjunto de nomes de comunidades
     */
    public Set<String> getComunidades() {
        return this.comunidades;
    }

    private Queue<String> mensagens = new LinkedList<>();

    /**
     * Recebe uma mensagem para o usuário
     *
     * @param mensagem Mensagem a ser armazenada
     */
    public void receberMensagem(String mensagem) {
        mensagens.add(mensagem);
    }

    /**
     * Lê e remove a próxima mensagem da fila
     *
     * @return Conteúdo da mensagem
     * @throws SemMensagensException Se não houver mensagens disponíveis
     */
    public String lerMensagem() {
        if (mensagens.isEmpty()) {
            throw new SemMensagensException();
        }
        return mensagens.poll();
    }

    /**
     * Verifica se existem mensagens não lidas
     *
     * @return true se houver mensagens não lidas, false caso contrário
     */
    public boolean temMensagens() {
        return !mensagens.isEmpty();
    }

    // Novos campos
    private Set<String> idolos = new HashSet<>();
    private Set<String> fas = new HashSet<>();
    private Set<String> paqueras = new LinkedHashSet<>();
    private Set<String> inimigos = new HashSet<>();

    /**
     * Adiciona um ídolo ao usuário
     *
     * @param idolo Login do usuário a ser adicionado como ídolo
     * @throws RelacaoExistenteException Se o usuário já for um ídolo
     */
    public void adicionarIdolo(String idolo) {
        if (idolos.contains(idolo)) {
            throw new RelacaoExistenteException("Usuário já está adicionado como ídolo.");
        }
        idolos.add(idolo);
    }

    /**
     * Adiciona um fã ao usuário
     *
     * @param fa Login do usuário que é fã deste usuário
     */
    public void adicionarFa(String fa) {
        fas.add(fa);
    }

    /**
     * Adiciona uma paquera ao usuário
     *
     * @param paquera Login do usuário a ser adicionado como paquera
     * @throws PaqueraExistenteException Se o usuário já for uma paquera
     */
    public void adicionarPaquera(String paquera) {
        if (paqueras.contains(paquera)) {
            throw new PaqueraExistenteException();
        }
        paqueras.add(paquera);
    }

    /**
     * Adiciona um inimigo ao usuário
     *
     * @param inimigo Login do usuário a ser adicionado como inimigo
     */
    public void adicionarInimigo(String inimigo) {
        inimigos.add(inimigo);
    }

    /**
     * Retorna os ídolos do usuário
     *
     * @return Conjunto imutável de logins de ídolos
     */
    public Set<String> getIdolos() { return Collections.unmodifiableSet(idolos); }

    /**
     * Retorna os fãs do usuário
     *
     * @return Conjunto imutável de logins de fãs
     */
    public Set<String> getFas() { return Collections.unmodifiableSet(fas); }

    /**
     * Retorna as paqueras do usuário
     *
     * @return Conjunto imutável de logins de paqueras
     */
    public Set<String> getPaqueras() { return Collections.unmodifiableSet(paqueras); }

    /**
     * Retorna os inimigos do usuário
     *
     * @return Conjunto imutável de logins de inimigos
     */
    public Set<String> getInimigos() { return Collections.unmodifiableSet(inimigos); }

    /**
     * Remove todos os relacionamentos com um usuário específico
     *
     * @param login Login do usuário a ter os relacionamentos removidos
     */
    public void removerRelacionamentos(String login) {
        amigos.remove(login);
        fas.remove(login);
        idolos.remove(login);
        paqueras.remove(login);
        inimigos.remove(login);
    }

    /**
     * Remove todos os recados enviados por um usuário específico
     *
     * @param remetente Login do usuário remetente dos recados a serem removidos
     */
    public void removerRecadosDoUsuario(String remetente) {
        recados.removeIf(r -> r.getRemetente().equals(remetente));
    }
}