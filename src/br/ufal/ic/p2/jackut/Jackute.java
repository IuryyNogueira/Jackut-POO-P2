package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.*;
import br.ufal.ic.p2.jackut.services.*;
import java.io.*;

/**
 * Implementa o núcleo de negócios do sistema Jackut, gerenciando usuários, sessões,
 * relacionamentos, recados e comunidades, além de persistência de estado.
 *
 * @author Iury
 * @version 1.0
 * @since 2025-05-04
 */
public class Jackute implements Serializable {
    private static final long serialVersionUID = 1L;

    private final GerenciadorUsuarios usuarios = new GerenciadorUsuarios();
    private final GerenciadorSessoes sessoes = new GerenciadorSessoes();
    private GerenciadorComunidades comunidades = new GerenciadorComunidades(usuarios);

    /**
     * Reseta completamente o sistema, removendo todos os usuários, sessões
     * e comunidades existentes.
     */
    public void zerar() {
        usuarios.zerar();
        sessoes.zerar();
        comunidades.zerar();
    }

    /**
     * Cria um novo usuário, validando credenciais e delegando à camada de serviço.
     *
     * @param login  login único do usuário
     * @param senha  senha de acesso (não vazia)
     * @param nome   nome completo do usuário
     * @throws LoginInvalidoException      se o login for nulo ou vazio
     * @throws SenhaInvalidaException      se a senha for nula ou vazia
     * @throws UsuarioJaExisteException    se já existir usuário com mesmo login
     */
    public void criarUsuario(String login, String senha, String nome) {
        validarCredenciais(login, senha);
        usuarios.criarUsuario(login, senha, nome);
    }

    /**
     * Autentica o usuário e inicia sessão.
     *
     * @param login  login do usuário
     * @param senha  senha do usuário
     * @return ID da sessão criada
     * @throws LoginOuSenhaInvalidosException se credenciais inválidas
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
     * Retorna o valor de um atributo de perfil de usuário.
     *
     * @param login     login do usuário
     * @param atributo  nome do atributo
     * @return valor do atributo
     * @throws UsuarioNaoEncontradoException  se o usuário não existir
     * @throws AtributoNaoPreenchidoException se atributo não definido
     */
    public String getAtributoUsuario(String login, String atributo) {
        return usuarios.getUsuario(login).getPerfil().getAtributo(atributo);
    }

    /**
     * Atualiza um atributo do perfil do usuário da sessão.
     *
     * @param idSessao  ID da sessão ativa
     * @param atributo  nome do atributo
     * @param valor      novo valor
     * @throws UsuarioNaoEncontradoException se sessão inválida
     */
    public void editarPerfil(String idSessao, String atributo, String valor) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        usuario.getPerfil().setAtributo(atributo, valor);
    }

    /**
     * Envia ou confirma convite de amizade entre usuários.
     *
     * @param idSessao    ID da sessão do solicitante
     * @param amigoLogin  login do usuário a ser adicionado
     * @throws AutoAmizadeException        se tentar adicionar a si mesmo
     * @throws UsuarioNaoEncontradoException se login do amigo não existir
     * @throws AmigoJaAdicionadoException  se já houver relação de amizade
     */
    public void adicionarAmigo(String idSessao, String amigoLogin) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        usuarios.adicionarAmigo(usuario.getLogin(), amigoLogin);
    }

    /**
     * Verifica se dois usuários são amigos mútuos.
     *
     * @param login1  login do primeiro usuário
     * @param login2  login do segundo usuário
     * @return true se forem amigos, false caso contrário
     * @throws UsuarioNaoEncontradoException se algum login não existir
     */
    public boolean ehAmigo(String login1, String login2) {
        return usuarios.saoAmigos(login1, login2);
    }

    /**
     * Lista todos os amigos de um usuário em formato string.
     *
     * @param login login do usuário
     * @return lista formatada como "{amigo1,amigo2, ...}"
     * @throws UsuarioNaoEncontradoException se usuário não existir
     */
    public String getAmigos(String login) {
        return usuarios.listarAmigos(login);
    }

    /**
     * Envia um recado privado de um usuário para outro.
     *
     * @param idSessao      ID da sessão do remetente
     * @param destinatario  login do destinatário
     * @param mensagem      conteúdo da mensagem
     * @throws AutoMensagemException       se enviar recado para si mesmo
     * @throws UsuarioNaoEncontradoException se destinatário não existir
     * @throws InimigoException            se destinatário for inimigo
     */
    public void enviarRecado(String idSessao, String destinatario, String mensagem) {
        Usuario remetente = getUsuarioPorSessao(idSessao);
        Usuario dest = usuarios.getUsuario(destinatario);
        if (remetente.getInimigos().contains(destinatario)) {
            String nomeInimigo = dest.getPerfil().getAtributo("nome");
            throw new InimigoException(nomeInimigo);
        }
        if (remetente.getLogin().equals(destinatario)) {
            throw new AutoMensagemException("Usuário não pode enviar recado para si mesmo.");
        }
        dest.adicionarRecado(new Recado(remetente.getLogin(), mensagem));
    }

    /**
     * Lê o próximo recado disponível do usuário da sessão.
     *
     * @param idSessao ID da sessão ativa
     * @return texto do recado
     * @throws SemRecadosException         se não houver recados
     * @throws UsuarioNaoEncontradoException se sessão inválida
     */
    public String lerRecado(String idSessao) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        return usuario.lerRecado();
    }

    /**
     * Cria uma comunidade e registra o usuário como dono e membro inicial.
     *
     * @param idSessao  ID da sessão do criador
     * @param nome      nome único da comunidade
     * @param descricao descrição da comunidade
     * @throws ComunidadeJaExisteException    se comunidade já existir
     * @throws UsuarioNaoEncontradoException  se sessão inválida
     */
    public void criarComunidade(String idSessao, String nome, String descricao) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        comunidades.criarComunidade(nome, descricao, usuario.getLogin());
        usuario.adicionarComunidade(nome);
    }

    /**
     * Retorna a descrição de uma comunidade existente.
     *
     * @param nome nome da comunidade
     * @return descrição cadastrada
     * @throws ComunidadeNaoEncontradaException se comunidade não existir
     */
    public String getDescricaoComunidade(String nome) {
        return comunidades.getDescricao(nome);
    }

    /**
     * Retorna o login do dono de determinada comunidade.
     *
     * @param nome nome da comunidade
     * @return login do proprietário
     * @throws ComunidadeNaoEncontradaException se comunidade não existir
     */
    public String getDonoComunidade(String nome) {
        return comunidades.getDono(nome);
    }

    /**
     * Lista membros de uma comunidade em formato string.
     *
     * @param nome nome da comunidade
     * @return lista formatada como "{membro1,membro2...}"
     * @throws ComunidadeNaoEncontradaException se comunidade não existir
     */
    public String getMembrosComunidade(String nome) {
        Community c = comunidades.getComunidade(nome);
        return "{" + String.join(",", c.getMembers()) + "}";
    }

    /**
     * Adiciona o usuário da sessão a uma comunidade existente.
     *
     * @param idSessao       ID da sessão
     * @param nomeComunidade nome da comunidade
     * @throws UsuarioNaoEncontradoException if sessão inválida
     * @throws ComunidadeNaoEncontradaException if comunidade não existir
     */
    public void adicionarComunidade(String idSessao, String nomeComunidade) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        comunidades.adicionarMembro(nomeComunidade, usuario.getLogin());
        usuario.adicionarComunidade(nomeComunidade);
    }

    /**
     * Retorna comunidades às quais o usuário pertence.
     *
     * @param login login do usuário
     * @return string formatada "{comun1,comun2,...}"
     */
    public String getComunidades(String login) {
        Usuario usuario = usuarios.getUsuario(login);
        return "{" + String.join(",", usuario.getComunidades()) + "}";
    }

    /**
     * Obtém usuário associado à sessão.
     *
     * @param idSessao ID da sessão
     * @return objeto Usuario
     * @throws UsuarioNaoEncontradoException se sessão inválida
     */
    private Usuario getUsuarioPorSessao(String idSessao) {
        String login = sessoes.getLogin(idSessao);
        if (login == null) {
            throw new UsuarioNaoEncontradoException();
        }
        return usuarios.getUsuario(login);
    }

    /**
     * Adiciona um ídolo (fã) para o usuário da sessão.
     *
     * @param sessao ID da sessão
     * @param idolo  login do usuário a ser idolatrado
     */
    public void adicionarIdolo(String sessao, String idolo) {
        Usuario usuario = getUsuarioPorSessao(sessao);
        usuarios.adicionarIdolo(usuario.getLogin(), idolo);
    }

    /**
     * Adiciona alguém como paquera (privado).
     *
     * @param sessao  ID da sessão
     * @param paquera login da paquera
     */
    public void adicionarPaquera(String sessao, String paquera) {
        Usuario usuario = getUsuarioPorSessao(sessao);
        usuarios.adicionarPaquera(usuario.getLogin(), paquera);
    }

    /**
     * Verifica relação de fã-ídolo.
     *
     * @param login login do fã
     * @param idolo login do ídolo
     * @return true se for fã, false caso contrário
     */
    public boolean ehFa(String login, String idolo) {
        return usuarios.getUsuario(login).getIdolos().contains(idolo);
    }

    /**
     * Marca alguém como inimigo do usuário da sessão.
     *
     * @param sessao  ID da sessão
     * @param inimigo login do inimigo
     */
    public void adicionarInimigo(String sessao, String inimigo) {
        Usuario usuario = getUsuarioPorSessao(sessao);
        usuarios.adicionarInimigo(usuario.getLogin(), inimigo);
    }

    /**
     * Retorna fãs do usuário informado.
     *
     * @param login login do usuário
     * @return string "{fa1,fa2,...}" dos fãs
     */
    public String getFas(String login) {
        Usuario usuario = usuarios.getUsuario(login);
        return "{" + String.join(",", usuario.getFas()) + "}";
    }

    /**
     * Verifica relação de paquera.
     *
     * @param sessao  ID da sessão do usuário
     * @param paquera login da paquera
     * @return true se existir a relação
     */
    public boolean ehPaquera(String sessao, String paquera) {
        Usuario usuario = getUsuarioPorSessao(sessao);
        return usuario.getPaqueras().contains(paquera);
    }

    /**
     * Retorna lista de paqueras do usuário da sessão.
     *
     * @param sessao ID da sessão
     * @return string formatada "{p1,p2,...}" das paqueras
     */
    public String getPaqueras(String sessao) {
        Usuario usuario = getUsuarioPorSessao(sessao);
        return "{" + String.join(",", usuario.getPaqueras()) + "}";
    }

    /**
     * Remove um usuário e limpa todas as referências a ele.
     *
     * @param idSessao ID da sessão do usuário a ser removido
     * @throws UsuarioNaoEncontradoException se sessão inválida
     */
    public void removerUsuario(String idSessao) {
        String login = sessoes.getLogin(idSessao);
        if (login == null) {
            throw new UsuarioNaoEncontradoException();
        }
        comunidades.removerUsuarioDeTodasComunidades(login);
        sessoes.removerSessoesDoUsuario(login);
        usuarios.removerUsuarioDeRelacionamentos(login);
        usuarios.removerUsuario(login);
    }

    /**
     * Valida login e senha não nulos/vazios.
     *
     * @param login login a validar
     * @param senha senha a validar
     * @throws LoginInvalidoException se login inválido
     * @throws SenhaInvalidaException se senha inválida
     */
    private void validarCredenciais(String login, String senha) {
        if (login == null || login.isBlank()) {
            throw new LoginInvalidoException();
        }
        if (senha == null || senha.isBlank()) {
            throw new SenhaInvalidaException();
        }
    }

    /**
     * Salva o estado do sistema em arquivo.
     *
     * @param arquivo caminho do arquivo de destino
     * @throws IOException se falha de I/O ocorrer
     */
    public void salvarEstado(String arquivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(this);
        }
    }

    /**
     * Carrega estado do sistema de arquivo.
     *
     * @param arquivo caminho do arquivo de origem
     * @return instância do sistema restaurada
     * @throws IOException            se falha de I/O ocorrer
     * @throws ClassNotFoundException se classe não for encontrada
     */
    public static Jackute carregarEstado(String arquivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (Jackute) ois.readObject();
        }
    }

    /**
     * Hook para garantir compatibilidade ao desserializar versões antigas.
     *
     * @param ois fluxo de entrada de objetos
     * @throws IOException            se falha de I/O
     * @throws ClassNotFoundException se classe não for encontrada
     */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        if (this.comunidades == null) {
            this.comunidades = new GerenciadorComunidades(this.usuarios);
        }
    }

    /**
     * Envia mensagem a todos os membros de uma comunidade.
     *
     * @param idSessao        ID da sessão do remetente
     * @param nomeComunidade  nome da comunidade
     * @param mensagem        conteúdo da mensagem
     */
    public void enviarMensagem(String idSessao, String nomeComunidade, String mensagem) {
        Usuario remetente = getUsuarioPorSessao(idSessao);
        comunidades.enviarMensagem(nomeComunidade, mensagem);
    }

    /**
     * Lê próxima mensagem de comunidade do usuário da sessão.
     *
     * @param idSessao ID da sessão
     * @return texto da mensagem
     */
    public String lerMensagem(String idSessao) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        return usuario.lerMensagem();
    }
}
