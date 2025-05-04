package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.*;
import br.ufal.ic.p2.jackut.services.*;
import java.io.*;

/**
 * Classe principal do sistema Jackut que implementa toda a l�gica de neg�cio e estado do sistema.
 * <p>
 * Gerencia usu�rios, sess�es, relacionamentos, recados e comunidades.
 * </p>
 *
 * @author IuryNogueira
 */
public class Jackute implements Serializable {
    private static final long serialVersionUID = 1L;

    private final GerenciadorUsuarios usuarios = new GerenciadorUsuarios();
    private final GerenciadorSessoes sessoes = new GerenciadorSessoes();
    private  GerenciadorComunidades comunidades = new GerenciadorComunidades(usuarios);

    /**
     * Reinicia todo o sistema, limpando todos os dados armazenados.
     */
    public void zerar() {
        usuarios.zerar();
        sessoes.zerar();
        comunidades.zerar();
    }

    /**
     * Cria um novo usu�rio no sistema.
     *
     * @param login Login �nico do usu�rio
     * @param senha Senha de acesso
     * @param nome Nome completo do usu�rio
     * @throws LoginInvalidoException Se o login for inv�lido (vazio ou nulo)
     * @throws SenhaInvalidaException Se a senha for inv�lida (vazia ou nula)
     * @throws UsuarioJaExisteException Se o login j� estiver em uso
     */
    public void criarUsuario(String login, String senha, String nome) {
        validarCredenciais(login, senha);
        usuarios.criarUsuario(login, senha, nome);
    }

    /**
     * Autentica um usu�rio e inicia uma nova sess�o.
     *
     * @param login Login do usu�rio
     * @param senha Senha do usu�rio
     * @return ID da sess�o criada
     * @throws LoginOuSenhaInvalidosException Se as credenciais forem inv�lidas
     */
    public String abrirSessao(String login, String senha) {
        try {
            Usuario usuario = usuarios.getUsuario(login);
            if (!usuario.getSenha().equals(senha)) {
                throw new LoginOuSenhaInvalidosException();
            }
            return sessoes.criarSessao(login);
        } catch (UsuarioNaoEncontradoException e) {
            throw new LoginOuSenhaInvalidosException();
        }
    }

    /**
     * Obt�m um atributo do perfil de um usu�rio.
     *
     * @param login Login do usu�rio
     * @param atributo Nome do atributo
     * @return Valor do atributo
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o existir
     * @throws AtributoNaoPreenchidoException Se o atributo n�o existir no perfil
     */
    public String getAtributoUsuario(String login, String atributo) {
        return usuarios.getUsuario(login).getPerfil().getAtributo(atributo);
    }

    /**
     * Edita um atributo do perfil do usu�rio logado.
     *
     * @param idSessao ID da sess�o ativa
     * @param atributo Nome do atributo
     * @param valor Novo valor do atributo
     * @throws UsuarioNaoEncontradoException Se a sess�o for inv�lida
     */
    public void editarPerfil(String idSessao, String atributo, String valor) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        usuario.getPerfil().setAtributo(atributo, valor);
    }

    /**
     * Adiciona um amigo enviando/confirmando convite.
     *
     * @param idSessao ID da sess�o
     * @param amigoLogin Login do amigo
     * @throws AutoAmizadeException Se tentar adicionar a si mesmo
     * @throws UsuarioNaoEncontradoException Se o amigo n�o existir
     * @throws AmigoJaAdicionadoException Se j� existir amizade ou convite
     */
    public void adicionarAmigo(String idSessao, String amigoLogin) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        usuarios.adicionarAmigo(usuario.getLogin(), amigoLogin);
    }

    /**
     * Verifica se dois usu�rios s�o amigos m�tuos.
     *
     * @param login1 Primeiro usu�rio
     * @param login2 Segundo usu�rio
     * @return true se forem amigos m�tuos, false caso contr�rio
     * @throws UsuarioNaoEncontradoException Se algum usu�rio n�o existir
     */
    public boolean ehAmigo(String login1, String login2) {
        return usuarios.saoAmigos(login1, login2);
    }

    /**
     * Lista os amigos de um usu�rio em ordem alfab�tica.
     *
     * @param login Login do usu�rio
     * @return String formatada com lista de amigos no formato "{amigo1,amigo2}"
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o existir
     */
    public String getAmigos(String login) {
        return usuarios.listarAmigos(login);
    }

    /**
     * Envia um recado para outro usu�rio.
     *
     * @param idSessao ID da sess�o do remetente
     * @param destinatario Login do destinat�rio
     * @param mensagem Conte�do do recado
     * @throws AutoMensagemException Se enviar para si mesmo
     * @throws UsuarioNaoEncontradoException Se o destinat�rio n�o existir
     */
    public void enviarRecado(String idSessao, String destinatario, String mensagem) {
        Usuario remetente = getUsuarioPorSessao(idSessao);
        Usuario dest = usuarios.getUsuario(destinatario);

        // 1. Verifica se o destinat�rio � inimigo
        if (remetente.getInimigos().contains(destinatario)) {
            String nomeInimigo = dest.getPerfil().getAtributo("nome");
            throw new InimigoException(nomeInimigo);
        }

        // 2. Verifica auto-mensagem
        if (remetente.getLogin().equals(destinatario)) {
            throw new AutoMensagemException("Usu�rio n�o pode enviar recado para si mesmo.");
        }

        dest.adicionarRecado(new Recado(remetente.getLogin(), mensagem));
    }

    /**
     * L� o pr�ximo recado n�o lido do usu�rio logado.
     *
     * @param idSessao ID da sess�o ativa
     * @return Conte�do do recado no formato "mensagem"
     * @throws SemRecadosException Se n�o houver recados dispon�veis
     * @throws UsuarioNaoEncontradoException Se a sess�o for inv�lida
     */
    public String lerRecado(String idSessao) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        return usuario.lerRecado();
    }

    /**
     * Cria uma nova comunidade no sistema.
     *
     * @param idSessao ID da sess�o do usu�rio criador
     * @param nome Nome �nico da comunidade
     * @param descricao Descri��o da comunidade
     * @throws ComunidadeJaExisteException Se j� existir comunidade com o mesmo nome
     * @throws UsuarioNaoEncontradoException Se a sess�o for inv�lida
     */
    public void criarComunidade(String idSessao, String nome, String descricao) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        comunidades.criarComunidade(nome, descricao, usuario.getLogin());
        usuario.adicionarComunidade(nome); // Adiciona comunidade ao usu�rio criador
    }


    /**
     * Obt�m a descri��o de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Descri��o da comunidade
     * @throws ComunidadeNaoEncontradaException Se a comunidade n�o existir
     */
    public String getDescricaoComunidade(String nome) {
        return comunidades.getDescricao(nome);
    }

    /**
     * Obt�m o dono de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Login do dono da comunidade
     * @throws ComunidadeNaoEncontradaException Se a comunidade n�o existir
     */
    public String getDonoComunidade(String nome) {
        return comunidades.getDono(nome);
    }

    /**
     * Lista os membros de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return String formatada com lista de membros no formato "{membro1,membro2}"
     * @throws ComunidadeNaoEncontradaException Se a comunidade n�o existir
     */
    public String getMembrosComunidade(String nome) {
        Community c = comunidades.getComunidade(nome);
        return "{" + String.join(",", c.getMembers()) + "}";
    }

    // M�todos auxiliares internos

    /**
     * Obt�m o usu�rio associado a uma sess�o.
     *
     * @param idSessao ID da sess�o
     * @return Inst�ncia do usu�rio
     * @throws UsuarioNaoEncontradoException Se a sess�o for inv�lida
     */
    private Usuario getUsuarioPorSessao(String idSessao) {
        String login = sessoes.getLogin(idSessao);
        if (login == null) {
            throw new UsuarioNaoEncontradoException();
        }
        return usuarios.getUsuario(login);
    }

    // Novos m�todos para US6
    public void adicionarComunidade(String idSessao, String nomeComunidade) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        comunidades.adicionarMembro(nomeComunidade, usuario.getLogin());
        usuario.adicionarComunidade(nomeComunidade); // Adiciona � lista do usu�rio
    }

    public String getComunidades(String login) {
        Usuario usuario = usuarios.getUsuario(login);
        return "{" + String.join(",", usuario.getComunidades()) + "}";
    }

    /**
     * Valida credenciais de login e senha.
     *
     * @param login Login a ser validado
     * @param senha Senha a ser validada
     * @throws LoginInvalidoException Se o login for inv�lido
     * @throws SenhaInvalidaException Se a senha for inv�lida
     */
    private void validarCredenciais(String login, String senha) {
        if (login == null || login.isBlank())
            throw new LoginInvalidoException();
        if (senha == null || senha.isBlank())
            throw new SenhaInvalidaException();
    }

    /**
     * Salva o estado atual do sistema em arquivo.
     *
     * @param arquivo Caminho do arquivo para salvar
     * @throws IOException Se ocorrer erro de I/O
     */
    public void salvarEstado(String arquivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(this);
        }
    }

    public void enviarMensagem(String idSessao, String nomeComunidade, String mensagem) {
        Usuario remetente = getUsuarioPorSessao(idSessao);
        comunidades.enviarMensagem(nomeComunidade, mensagem);
    }

    public String lerMensagem(String idSessao) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        return usuario.lerMensagem();
    }

    /**
     * Carrega o estado do sistema de um arquivo.
     *
     * @param arquivo Caminho do arquivo para carregar
     * @return Inst�ncia do sistema carregada
     * @throws IOException Se ocorrer erro de I/O
     * @throws ClassNotFoundException Se a classe do objeto serializado n�o for encontrada
     */
    public static Jackute carregarEstado(String arquivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (Jackute) ois.readObject();
        }
    }

    // M�todo para garantir compatibilidade de vers�es serializadas
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        if (this.comunidades == null) {
            this.comunidades = new GerenciadorComunidades(this.usuarios); // Passa a refer�ncia existente
        }
    }

    // Implementa��o dos novos m�todos
    public void adicionarIdolo(String sessao, String idolo) {
        Usuario usuario = getUsuarioPorSessao(sessao);
        usuarios.adicionarIdolo(usuario.getLogin(), idolo);
    }

    public void adicionarPaquera(String sessao, String paquera) {
        Usuario usuario = getUsuarioPorSessao(sessao);
        usuarios.adicionarPaquera(usuario.getLogin(), paquera);
    }

    public boolean ehFa(String login, String idolo) {
        return usuarios.getUsuario(login).getIdolos().contains(idolo);
    }

    public void adicionarInimigo(String sessao, String inimigo) {
        Usuario usuario = getUsuarioPorSessao(sessao);
        usuarios.adicionarInimigo(usuario.getLogin(), inimigo);
    }

    public String getFas(String login) {
        Usuario usuario = usuarios.getUsuario(login);
        return "{" + String.join(",", usuario.getFas()) + "}";
    }

    public boolean ehPaquera(String sessao, String paquera) {
        Usuario usuario = getUsuarioPorSessao(sessao);
        return usuario.getPaqueras().contains(paquera);
    }

    public String getPaqueras(String sessao) {
        Usuario usuario = getUsuarioPorSessao(sessao);
        return "{" + String.join(",", usuario.getPaqueras()) + "}";
    }

}