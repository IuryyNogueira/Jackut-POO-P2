package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.exceptions.*;
import java.io.Serializable;
import java.util.*;
import br.ufal.ic.p2.jackut.exceptions.RelacaoExistenteException;

/**
 * Representa um usu�rio do sistema Jackut, contendo informa��es de perfil,
 * relacionamentos de amizade, convites, recados e outros tipos de relacionamentos.
 *
 * <p>Esta classe implementa {@link Serializable} para permitir persist�ncia dos dados.</p>
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
     * Enumera��o que representa o status de um convite de amizade
     */
    public enum ConviteStatus {
        /** Convite enviado pelo usu�rio atual */
        ENVIADO,
        /** Convite recebido de outro usu�rio */
        RECEBIDO
    }

    /**
     * Constr�i um novo usu�rio com informa��es b�sicas
     *
     * @param login Identificador �nico do usu�rio
     * @param senha Senha de acesso
     * @param nome Nome completo do usu�rio (ser� armazenado no perfil)
     */
    public Usuario(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.perfil = new Perfil();
        this.perfil.setAtributo("nome", nome);
    }

    /**
     * @return Login do usu�rio (imut�vel)
     */
    public String getLogin() { return login; }

    /**
     * @return Senha do usu�rio (imut�vel)
     */
    public String getSenha() { return senha; }

    /**
     * @return Perfil p�blico do usu�rio
     */
    public Perfil getPerfil() { return perfil; }

    /**
     * Retorna uma vis�o imut�vel da lista de amigos
     *
     * @return Conjunto n�o modific�vel de logins de amigos
     */
    public Set<String> getAmigos() {
        return Collections.unmodifiableSet(amigos);
    }

    /**
     * Adiciona um usu�rio � lista de amigos
     *
     * @param loginAmigo Login do usu�rio a ser adicionado como amigo
     */
    public void adicionarAmigo(String loginAmigo) {
        amigos.add(loginAmigo);
    }

    /**
     * Verifica se existe um convite enviado para outro usu�rio
     *
     * @param loginAmigo Login do usu�rio alvo
     * @return true se houver convite ativo enviado para este usu�rio
     */
    public boolean temConvitePara(String loginAmigo) {
        return convites.containsKey(loginAmigo) && convites.get(loginAmigo) == ConviteStatus.ENVIADO;
    }

    /**
     * Verifica se existe um convite recebido de outro usu�rio
     *
     * @param loginAmigo Login do usu�rio remetente
     * @return true se houver convite recebido deste usu�rio
     */
    public boolean temConviteDe(String loginAmigo) {
        return convites.containsKey(loginAmigo) && convites.get(loginAmigo) == ConviteStatus.RECEBIDO;
    }

    /**
     * Registra um novo convite de amizade
     *
     * @param loginAmigo Login do usu�rio relacionado ao convite
     * @param status Status do convite ({@link ConviteStatus})
     */
    public void adicionarConvite(String loginAmigo, ConviteStatus status) {
        convites.put(loginAmigo, status);
    }

    /**
     * Remove um convite existente
     *
     * @param loginAmigo Login do usu�rio relacionado ao convite
     */
    public void removerConvite(String loginAmigo) {
        convites.remove(loginAmigo);
    }

    /**
     * Adiciona um novo recado � fila de mensagens
     *
     * @param recado Recado a ser armazenado
     */
    public void adicionarRecado(Recado recado) {
        recados.add(recado);
    }

    /**
     * L� e remove o pr�ximo recado da fila
     *
     * @return Conte�do do recado no formato "mensagem"
     * @throws SemRecadosException Se n�o houver recados dispon�veis
     */
    public String lerRecado() {
        Recado recado = recados.poll();
        if (recado == null) throw new SemRecadosException();
        return recado.toString();
    }

    /**
     * Retorna a lista de amigos em ordem de adi��o
     *
     * @return Lista ordenada de logins de amigos
     */
    public List<String> getAmigosOrdenados() {
        return new ArrayList<>(amigos);
    }

    /**
     * Retorna uma vis�o imut�vel dos convites
     *
     * @return Mapa n�o modific�vel de convites (chave: login, valor: status)
     */
    public Map<String, ConviteStatus> getConvites() {
        return Collections.unmodifiableMap(convites);
    }

    private LinkedHashSet<String> comunidades = new LinkedHashSet<>();

    /**
     * Adiciona o usu�rio a uma comunidade
     *
     * @param nomeComunidade Nome da comunidade a ser adicionada
     */
    public void adicionarComunidade(String nomeComunidade) {
        comunidades.add(nomeComunidade);
    }

    /**
     * Retorna as comunidades do usu�rio
     *
     * @return Conjunto de nomes de comunidades
     */
    public Set<String> getComunidades() {
        return this.comunidades;
    }

    private Queue<String> mensagens = new LinkedList<>();

    /**
     * Recebe uma mensagem para o usu�rio
     *
     * @param mensagem Mensagem a ser armazenada
     */
    public void receberMensagem(String mensagem) {
        mensagens.add(mensagem);
    }

    /**
     * L� e remove a pr�xima mensagem da fila
     *
     * @return Conte�do da mensagem
     * @throws SemMensagensException Se n�o houver mensagens dispon�veis
     */
    public String lerMensagem() {
        if (mensagens.isEmpty()) {
            throw new SemMensagensException();
        }
        return mensagens.poll();
    }

    /**
     * Verifica se existem mensagens n�o lidas
     *
     * @return true se houver mensagens n�o lidas, false caso contr�rio
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
     * Adiciona um �dolo ao usu�rio
     *
     * @param idolo Login do usu�rio a ser adicionado como �dolo
     * @throws RelacaoExistenteException Se o usu�rio j� for um �dolo
     */
    public void adicionarIdolo(String idolo) {
        if (idolos.contains(idolo)) {
            throw new RelacaoExistenteException("Usu�rio j� est� adicionado como �dolo.");
        }
        idolos.add(idolo);
    }

    /**
     * Adiciona um f� ao usu�rio
     *
     * @param fa Login do usu�rio que � f� deste usu�rio
     */
    public void adicionarFa(String fa) {
        fas.add(fa);
    }

    /**
     * Adiciona uma paquera ao usu�rio
     *
     * @param paquera Login do usu�rio a ser adicionado como paquera
     * @throws PaqueraExistenteException Se o usu�rio j� for uma paquera
     */
    public void adicionarPaquera(String paquera) {
        if (paqueras.contains(paquera)) {
            throw new PaqueraExistenteException();
        }
        paqueras.add(paquera);
    }

    /**
     * Adiciona um inimigo ao usu�rio
     *
     * @param inimigo Login do usu�rio a ser adicionado como inimigo
     */
    public void adicionarInimigo(String inimigo) {
        inimigos.add(inimigo);
    }

    /**
     * Retorna os �dolos do usu�rio
     *
     * @return Conjunto imut�vel de logins de �dolos
     */
    public Set<String> getIdolos() { return Collections.unmodifiableSet(idolos); }

    /**
     * Retorna os f�s do usu�rio
     *
     * @return Conjunto imut�vel de logins de f�s
     */
    public Set<String> getFas() { return Collections.unmodifiableSet(fas); }

    /**
     * Retorna as paqueras do usu�rio
     *
     * @return Conjunto imut�vel de logins de paqueras
     */
    public Set<String> getPaqueras() { return Collections.unmodifiableSet(paqueras); }

    /**
     * Retorna os inimigos do usu�rio
     *
     * @return Conjunto imut�vel de logins de inimigos
     */
    public Set<String> getInimigos() { return Collections.unmodifiableSet(inimigos); }

    /**
     * Remove todos os relacionamentos com um usu�rio espec�fico
     *
     * @param login Login do usu�rio a ter os relacionamentos removidos
     */
    public void removerRelacionamentos(String login) {
        amigos.remove(login);
        fas.remove(login);
        idolos.remove(login);
        paqueras.remove(login);
        inimigos.remove(login);
    }

    /**
     * Remove todos os recados enviados por um usu�rio espec�fico
     *
     * @param remetente Login do usu�rio remetente dos recados a serem removidos
     */
    public void removerRecadosDoUsuario(String remetente) {
        recados.removeIf(r -> r.getRemetente().equals(remetente));
    }
}