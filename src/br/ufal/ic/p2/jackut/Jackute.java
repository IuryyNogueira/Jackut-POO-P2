package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.*;
import br.ufal.ic.p2.jackut.services.*;
import java.io.*;

/**
 * Classe principal do sistema Jackut que implementa toda a lógica de negócio e estado do sistema.
 *
 * @author IuryNogueira
 */
public class Jackute implements Serializable {
    private static final long serialVersionUID = 1L;

    private final GerenciadorUsuarios usuarios = new GerenciadorUsuarios();
    private final GerenciadorSessoes sessoes = new GerenciadorSessoes();

    /**
     * Cria um novo usuário no sistema.
     *
     * @param login Login único do usuário
     * @param senha Senha de acesso
     * @param nome Nome completo do usuário
     * @throws LoginInvalidoException Se o login for inválido
     * @throws SenhaInvalidaException Se a senha for inválida
     * @throws UsuarioJaExisteException Se o login já estiver em uso
     */
    public void criarUsuario(String login, String senha, String nome) {
        validarCredenciais(login, senha);
        usuarios.criarUsuario(login, senha, nome);
    }

    /**
     * Autentica um usuário e inicia uma nova sessão.
     *
     * @param login Login do usuário
     * @param senha Senha do usuário
     * @return ID da sessão criada
     * @throws LoginOuSenhaInvalidosException Se as credenciais forem inválidas
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
     * Obtém um atributo do perfil de um usuário.
     *
     * @param login Login do usuário
     * @param atributo Nome do atributo
     * @return Valor do atributo
     * @throws UsuarioNaoEncontradoException Se o usuário não existir
     * @throws AtributoNaoPreenchidoException Se o atributo não existir
     */
    public String getAtributoUsuario(String login, String atributo) {
        return usuarios.getUsuario(login).getPerfil().getAtributo(atributo);
    }

    /**
     * Edita um atributo do perfil do usuário logado.
     *
     * @param idSessao ID da sessão ativa
     * @param atributo Nome do atributo
     * @param valor Novo valor do atributo
     * @throws UsuarioNaoEncontradoException Se a sessão for inválida
     */
    public void editarPerfil(String idSessao, String atributo, String valor) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        usuario.getPerfil().setAtributo(atributo, valor);
    }

    /**
     * Adiciona um amigo enviando/confirmando convite.
     *
     * @param idSessao ID da sessão
     * @param amigoLogin Login do amigo
     * @throws AutoAmizadeException Se tentar adicionar a si mesmo
     * @throws UsuarioNaoEncontradoException Se usuário não existir
     * @throws AmigoJaAdicionadoException Se já existir amizade ou convite
     */
    public void adicionarAmigo(String idSessao, String amigoLogin) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        usuarios.adicionarAmigo(usuario.getLogin(), amigoLogin);
    }

    /**
     * Verifica se dois usuários são amigos.
     *
     * @param login1 Primeiro usuário
     * @param login2 Segundo usuário
     * @return true se forem amigos mútuos
     * @throws UsuarioNaoEncontradoException Se algum usuário não existir
     */
    public boolean ehAmigo(String login1, String login2) {
        return usuarios.saoAmigos(login1, login2);
    }

    /**
     * Lista os amigos de um usuário.
     *
     * @param login Login do usuário
     * @return String formatada com lista de amigos
     * @throws UsuarioNaoEncontradoException Se o usuário não existir
     */
    public String getAmigos(String login) {
        return usuarios.listarAmigos(login);
    }

    /**
     * Envia um recado para outro usuário.
     *
     * @param idSessao ID da sessão
     * @param destinatario Login do destinatário
     * @param mensagem Conteúdo do recado
     * @throws AutoMensagemException Se enviar para si mesmo
     * @throws UsuarioNaoEncontradoException Se destinatário não existir
     */
    public void enviarRecado(String idSessao, String destinatario, String mensagem) {
        Usuario remetente = getUsuarioPorSessao(idSessao);
        Usuario dest = usuarios.getUsuario(destinatario);

        if (remetente.getLogin().equals(destinatario)) {
            throw new AutoMensagemException("Usuário não pode enviar recado para si mesmo.");
        }

        dest.adicionarRecado(new Recado(remetente.getLogin(), mensagem));
    }

    /**
     * Lê o próximo recado do usuário.
     *
     * @param idSessao ID da sessão
     * @return Conteúdo do recado
     * @throws SemRecadosException Se não houver recados
     * @throws UsuarioNaoEncontradoException Se sessão inválida
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

    // Métodos auxiliares internos
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
     * @return Instância do sistema carregada
     * @throws IOException Se ocorrer erro de I/O
     * @throws ClassNotFoundException Se classe não for encontrada
     */
    public static Jackute carregarEstado(String arquivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (Jackute) ois.readObject();
        }
    }
}