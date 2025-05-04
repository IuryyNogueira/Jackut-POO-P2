package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.*;
import br.ufal.ic.p2.jackut.services.*;
import java.io.*;

/**
 * Classe principal do sistema Jackut que implementa toda a lógica de negócio e estado do sistema.
 * <p>
 * Gerencia usuários, sessões, relacionamentos, recados e comunidades.
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
     * Cria um novo usuário no sistema.
     *
     * @param login Login único do usuário
     * @param senha Senha de acesso
     * @param nome Nome completo do usuário
     * @throws LoginInvalidoException Se o login for inválido (vazio ou nulo)
     * @throws SenhaInvalidaException Se a senha for inválida (vazia ou nula)
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
     * @throws AtributoNaoPreenchidoException Se o atributo não existir no perfil
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
     * @throws UsuarioNaoEncontradoException Se o amigo não existir
     * @throws AmigoJaAdicionadoException Se já existir amizade ou convite
     */
    public void adicionarAmigo(String idSessao, String amigoLogin) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        usuarios.adicionarAmigo(usuario.getLogin(), amigoLogin);
    }

    /**
     * Verifica se dois usuários são amigos mútuos.
     *
     * @param login1 Primeiro usuário
     * @param login2 Segundo usuário
     * @return true se forem amigos mútuos, false caso contrário
     * @throws UsuarioNaoEncontradoException Se algum usuário não existir
     */
    public boolean ehAmigo(String login1, String login2) {
        return usuarios.saoAmigos(login1, login2);
    }

    /**
     * Lista os amigos de um usuário em ordem alfabética.
     *
     * @param login Login do usuário
     * @return String formatada com lista de amigos no formato "{amigo1,amigo2}"
     * @throws UsuarioNaoEncontradoException Se o usuário não existir
     */
    public String getAmigos(String login) {
        return usuarios.listarAmigos(login);
    }

    /**
     * Envia um recado para outro usuário.
     *
     * @param idSessao ID da sessão do remetente
     * @param destinatario Login do destinatário
     * @param mensagem Conteúdo do recado
     * @throws AutoMensagemException Se enviar para si mesmo
     * @throws UsuarioNaoEncontradoException Se o destinatário não existir
     */
    public void enviarRecado(String idSessao, String destinatario, String mensagem) {
        Usuario remetente = getUsuarioPorSessao(idSessao);
        Usuario dest = usuarios.getUsuario(destinatario);

        // 1. Verifica se o destinatário é inimigo
        if (remetente.getInimigos().contains(destinatario)) {
            String nomeInimigo = dest.getPerfil().getAtributo("nome");
            throw new InimigoException(nomeInimigo);
        }

        // 2. Verifica auto-mensagem
        if (remetente.getLogin().equals(destinatario)) {
            throw new AutoMensagemException("Usuário não pode enviar recado para si mesmo.");
        }

        dest.adicionarRecado(new Recado(remetente.getLogin(), mensagem));
    }

    /**
     * Lê o próximo recado não lido do usuário logado.
     *
     * @param idSessao ID da sessão ativa
     * @return Conteúdo do recado no formato "mensagem"
     * @throws SemRecadosException Se não houver recados disponíveis
     * @throws UsuarioNaoEncontradoException Se a sessão for inválida
     */
    public String lerRecado(String idSessao) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        return usuario.lerRecado();
    }

    /**
     * Cria uma nova comunidade no sistema.
     *
     * @param idSessao ID da sessão do usuário criador
     * @param nome Nome único da comunidade
     * @param descricao Descrição da comunidade
     * @throws ComunidadeJaExisteException Se já existir comunidade com o mesmo nome
     * @throws UsuarioNaoEncontradoException Se a sessão for inválida
     */
    public void criarComunidade(String idSessao, String nome, String descricao) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        comunidades.criarComunidade(nome, descricao, usuario.getLogin());
        usuario.adicionarComunidade(nome); // Adiciona comunidade ao usuário criador
    }


    /**
     * Obtém a descrição de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Descrição da comunidade
     * @throws ComunidadeNaoEncontradaException Se a comunidade não existir
     */
    public String getDescricaoComunidade(String nome) {
        return comunidades.getDescricao(nome);
    }

    /**
     * Obtém o dono de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Login do dono da comunidade
     * @throws ComunidadeNaoEncontradaException Se a comunidade não existir
     */
    public String getDonoComunidade(String nome) {
        return comunidades.getDono(nome);
    }

    /**
     * Lista os membros de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return String formatada com lista de membros no formato "{membro1,membro2}"
     * @throws ComunidadeNaoEncontradaException Se a comunidade não existir
     */
    public String getMembrosComunidade(String nome) {
        Community c = comunidades.getComunidade(nome);
        return "{" + String.join(",", c.getMembers()) + "}";
    }

    // Métodos auxiliares internos

    /**
     * Obtém o usuário associado a uma sessão.
     *
     * @param idSessao ID da sessão
     * @return Instância do usuário
     * @throws UsuarioNaoEncontradoException Se a sessão for inválida
     */
    private Usuario getUsuarioPorSessao(String idSessao) {
        String login = sessoes.getLogin(idSessao);
        if (login == null) {
            throw new UsuarioNaoEncontradoException();
        }
        return usuarios.getUsuario(login);
    }

    // Novos métodos para US6
    public void adicionarComunidade(String idSessao, String nomeComunidade) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        comunidades.adicionarMembro(nomeComunidade, usuario.getLogin());
        usuario.adicionarComunidade(nomeComunidade); // Adiciona à lista do usuário
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
     * @throws LoginInvalidoException Se o login for inválido
     * @throws SenhaInvalidaException Se a senha for inválida
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
     * @return Instância do sistema carregada
     * @throws IOException Se ocorrer erro de I/O
     * @throws ClassNotFoundException Se a classe do objeto serializado não for encontrada
     */
    public static Jackute carregarEstado(String arquivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (Jackute) ois.readObject();
        }
    }

    // Método para garantir compatibilidade de versões serializadas
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        if (this.comunidades == null) {
            this.comunidades = new GerenciadorComunidades(this.usuarios); // Passa a referência existente
        }
    }

    // Implementação dos novos métodos
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