package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.exceptions.*;
import java.io.*;

/**
 * Fachada principal do sistema Jackut que fornece uma interface simplificada para as operações do sistema.
 *
 * @author IuryNogueira
 */
public class Facade {
    private Jackute sistema;
    private static final String ARQUIVO_DADOS = "dados_jackut.dat";

    /**
     * Constrói uma nova fachada e carrega os dados persistidos do sistema.
     */
    public Facade() {
        carregarDados();
    }

    /**
     * Cria um novo usuário no sistema.
     *
     * @param login Identificador único do usuário
     * @param senha Senha de acesso do usuário
     * @param nome Nome completo do usuário
     * @throws LoginInvalidoException Se o login for inválido
     * @throws SenhaInvalidaException Se a senha for inválida
     * @throws UsuarioJaExisteException Se o login já estiver em uso
     */
    public void criarUsuario(String login, String senha, String nome) {
        sistema.criarUsuario(login, senha, nome);
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
        return sistema.abrirSessao(login, senha);
    }

    /**
     * Obtém o valor de um atributo do perfil de um usuário.
     *
     * @param login Login do usuário
     * @param atributo Nome do atributo
     * @return Valor do atributo solicitado
     * @throws UsuarioNaoEncontradoException Se o usuário não existir
     * @throws AtributoNaoPreenchidoException Se o atributo não estiver definido
     */
    public String getAtributoUsuario(String login, String atributo) {
        return sistema.getAtributoUsuario(login, atributo);
    }

    /**
     * Edita um atributo do perfil do usuário logado.
     *
     * @param idSessao ID da sessão ativa
     * @param atributo Nome do atributo a ser modificado
     * @param valor Novo valor para o atributo
     * @throws UsuarioNaoEncontradoException Se a sessão for inválida
     */
    public void editarPerfil(String idSessao, String atributo, String valor) {
        sistema.editarPerfil(idSessao, atributo, valor);
    }

    /**
     * Adiciona um usuário como amigo, enviando ou confirmando um convite.
     *
     * @param idSessao ID da sessão do usuário solicitante
     * @param amigoLogin Login do usuário a ser adicionado como amigo
     * @throws AutoAmizadeException Se o usuário tentar adicionar a si mesmo
     * @throws UsuarioNaoEncontradoException Se o amigo não existir
     * @throws AmigoJaAdicionadoException Se já existir amizade ou convite pendente
     */
    public void adicionarAmigo(String idSessao, String amigoLogin) {
        sistema.adicionarAmigo(idSessao, amigoLogin);
    }

    /**
     * Verifica se dois usuários são amigos mútuos.
     *
     * @param login1 Login do primeiro usuário
     * @param login2 Login do segundo usuário
     * @return true se os usuários forem amigos mútuos, false caso contrário
     * @throws UsuarioNaoEncontradoException Se algum usuário não existir
     */
    public boolean ehAmigo(String login1, String login2) {
        return sistema.ehAmigo(login1, login2);
    }

    /**
     * Lista os amigos de um usuário.
     *
     * @param login Login do usuário
     * @return String formatada com a lista de amigos no formato "{amigo1,amigo2}"
     * @throws UsuarioNaoEncontradoException Se o usuário não existir
     */
    public String getAmigos(String login) {
        return sistema.getAmigos(login);
    }

    /**
     * Envia um recado para outro usuário.
     *
     * @param idSessao ID da sessão do remetente
     * @param destinatario Login do usuário destinatário
     * @param mensagem Conteúdo do recado
     * @throws AutoMensagemException Se o remetente for o mesmo que o destinatário
     * @throws UsuarioNaoEncontradoException Se o destinatário não existir
     */
    public void enviarRecado(String idSessao, String destinatario, String mensagem) {
        sistema.enviarRecado(idSessao, destinatario, mensagem);
    }

    /**
     * Lê o próximo recado não lido do usuário logado.
     *
     * @param idSessao ID da sessão ativa
     * @return Conteúdo do recado
     * @throws SemRecadosException Se não houver recados disponíveis
     * @throws UsuarioNaoEncontradoException Se a sessão for inválida
     */
    public String lerRecado(String idSessao) {
        return sistema.lerRecado(idSessao);
    }

    /**
     * Reinicia o sistema, removendo todos os usuários e sessões.
     */
    public void zerarSistema() {
        sistema.zerar();
    }

    /**
     * Encerra o sistema, salvando os dados persistentes.
     */
    public void encerrarSistema() {
        salvarDados();
    }

    // Métodos auxiliares de persistência
    private void salvarDados() {
        try {
            sistema.salvarEstado(ARQUIVO_DADOS);
        } catch (IOException e) {
            throw new PersistenciaException();
        }
    }

    private void carregarDados() {
        try {
            sistema = Jackute.carregarEstado(ARQUIVO_DADOS);
        } catch (FileNotFoundException e) {
            sistema = new Jackute();
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException();
        }
    }
}