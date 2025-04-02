package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.*;
import br.ufal.ic.p2.jackut.services.*;
import java.io.*;

/**
 * Classe principal do sistema Jackut que implementa toda a l�gica de neg�cio e estado do sistema.
 *
 * @author IuryNogueira
 */
public class Jackute implements Serializable {
    private static final long serialVersionUID = 1L;

    private final GerenciadorUsuarios usuarios = new GerenciadorUsuarios();
    private final GerenciadorSessoes sessoes = new GerenciadorSessoes();

    /**
     * Cria um novo usu�rio no sistema.
     *
     * @param login Login �nico do usu�rio
     * @param senha Senha de acesso
     * @param nome Nome completo do usu�rio
     * @throws LoginInvalidoException Se o login for inv�lido
     * @throws SenhaInvalidaException Se a senha for inv�lida
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
     * @throws AtributoNaoPreenchidoException Se o atributo n�o existir
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
     * @throws UsuarioNaoEncontradoException Se usu�rio n�o existir
     * @throws AmigoJaAdicionadoException Se j� existir amizade ou convite
     */
    public void adicionarAmigo(String idSessao, String amigoLogin) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        usuarios.adicionarAmigo(usuario.getLogin(), amigoLogin);
    }

    /**
     * Verifica se dois usu�rios s�o amigos.
     *
     * @param login1 Primeiro usu�rio
     * @param login2 Segundo usu�rio
     * @return true se forem amigos m�tuos
     * @throws UsuarioNaoEncontradoException Se algum usu�rio n�o existir
     */
    public boolean ehAmigo(String login1, String login2) {
        return usuarios.saoAmigos(login1, login2);
    }

    /**
     * Lista os amigos de um usu�rio.
     *
     * @param login Login do usu�rio
     * @return String formatada com lista de amigos
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o existir
     */
    public String getAmigos(String login) {
        return usuarios.listarAmigos(login);
    }

    /**
     * Envia um recado para outro usu�rio.
     *
     * @param idSessao ID da sess�o
     * @param destinatario Login do destinat�rio
     * @param mensagem Conte�do do recado
     * @throws AutoMensagemException Se enviar para si mesmo
     * @throws UsuarioNaoEncontradoException Se destinat�rio n�o existir
     */
    public void enviarRecado(String idSessao, String destinatario, String mensagem) {
        Usuario remetente = getUsuarioPorSessao(idSessao);
        Usuario dest = usuarios.getUsuario(destinatario);

        if (remetente.getLogin().equals(destinatario)) {
            throw new AutoMensagemException("Usu�rio n�o pode enviar recado para si mesmo.");
        }

        dest.adicionarRecado(new Recado(remetente.getLogin(), mensagem));
    }

    /**
     * L� o pr�ximo recado do usu�rio.
     *
     * @param idSessao ID da sess�o
     * @return Conte�do do recado
     * @throws SemRecadosException Se n�o houver recados
     * @throws UsuarioNaoEncontradoException Se sess�o inv�lida
     */
    public String lerRecado(String idSessao) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        return usuario.lerRecado();
    }

    /**
     * Reinicia todo o sistema.
     */
    public void zerar() {
        usuarios.zerar();
        sessoes.zerar();
    }

    // M�todos auxiliares internos
    private Usuario getUsuarioPorSessao(String idSessao) {
        String login = sessoes.getLogin(idSessao);
        if (login == null) {
            throw new UsuarioNaoEncontradoException();
        }
        return usuarios.getUsuario(login);
    }

    private void validarCredenciais(String login, String senha) {
        if (login == null || login.isBlank())
            throw new LoginInvalidoException();
        if (senha == null || senha.isBlank())
            throw new SenhaInvalidaException();
    }

    /**
     * Salva o estado atual do sistema em arquivo.
     *
     * @param arquivo Caminho do arquivo
     * @throws IOException Se ocorrer erro de I/O
     */
    public void salvarEstado(String arquivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(this);
        }
    }

    /**
     * Carrega o estado do sistema de um arquivo.
     *
     * @param arquivo Caminho do arquivo
     * @return Inst�ncia do sistema carregada
     * @throws IOException Se ocorrer erro de I/O
     * @throws ClassNotFoundException Se classe n�o for encontrada
     */
    public static Jackute carregarEstado(String arquivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (Jackute) ois.readObject();
        }
    }
}