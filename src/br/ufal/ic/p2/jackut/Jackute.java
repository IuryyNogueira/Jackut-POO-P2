package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.*;
import br.ufal.ic.p2.jackut.services.*;
import java.io.*;

/**
 * Implementa o n�cleo de neg�cios do sistema Jackut, gerenciando usu�rios, sess�es,
 * relacionamentos, recados e comunidades, al�m de persist�ncia de estado.
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
     * Reseta completamente o sistema, removendo todos os usu�rios, sess�es
     * e comunidades existentes.
     */
    public void zerar() {
        usuarios.zerar();
        sessoes.zerar();
        comunidades.zerar();
    }

    /**
     * Cria um novo usu�rio, validando credenciais e delegando � camada de servi�o.
     *
     * @param login  login �nico do usu�rio
     * @param senha  senha de acesso (n�o vazia)
     * @param nome   nome completo do usu�rio
     * @throws LoginInvalidoException      se o login for nulo ou vazio
     * @throws SenhaInvalidaException      se a senha for nula ou vazia
     * @throws UsuarioJaExisteException    se j� existir usu�rio com mesmo login
     */
    public void criarUsuario(String login, String senha, String nome) {
        validarCredenciais(login, senha);
        usuarios.criarUsuario(login, senha, nome);
    }

    /**
     * Autentica o usu�rio e inicia sess�o.
     *
     * @param login  login do usu�rio
     * @param senha  senha do usu�rio
     * @return ID da sess�o criada
     * @throws LoginOuSenhaInvalidosException se credenciais inv�lidas
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
     * Retorna o valor de um atributo de perfil de usu�rio.
     *
     * @param login     login do usu�rio
     * @param atributo  nome do atributo
     * @return valor do atributo
     * @throws UsuarioNaoEncontradoException  se o usu�rio n�o existir
     * @throws AtributoNaoPreenchidoException se atributo n�o definido
     */
    public String getAtributoUsuario(String login, String atributo) {
        return usuarios.getUsuario(login).getPerfil().getAtributo(atributo);
    }

    /**
     * Atualiza um atributo do perfil do usu�rio da sess�o.
     *
     * @param idSessao  ID da sess�o ativa
     * @param atributo  nome do atributo
     * @param valor      novo valor
     * @throws UsuarioNaoEncontradoException se sess�o inv�lida
     */
    public void editarPerfil(String idSessao, String atributo, String valor) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        usuario.getPerfil().setAtributo(atributo, valor);
    }

    /**
     * Envia ou confirma convite de amizade entre usu�rios.
     *
     * @param idSessao    ID da sess�o do solicitante
     * @param amigoLogin  login do usu�rio a ser adicionado
     * @throws AutoAmizadeException        se tentar adicionar a si mesmo
     * @throws UsuarioNaoEncontradoException se login do amigo n�o existir
     * @throws AmigoJaAdicionadoException  se j� houver rela��o de amizade
     */
    public void adicionarAmigo(String idSessao, String amigoLogin) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        usuarios.adicionarAmigo(usuario.getLogin(), amigoLogin);
    }

    /**
     * Verifica se dois usu�rios s�o amigos m�tuos.
     *
     * @param login1  login do primeiro usu�rio
     * @param login2  login do segundo usu�rio
     * @return true se forem amigos, false caso contr�rio
     * @throws UsuarioNaoEncontradoException se algum login n�o existir
     */
    public boolean ehAmigo(String login1, String login2) {
        return usuarios.saoAmigos(login1, login2);
    }

    /**
     * Lista todos os amigos de um usu�rio em formato string.
     *
     * @param login login do usu�rio
     * @return lista formatada como "{amigo1,amigo2, ...}"
     * @throws UsuarioNaoEncontradoException se usu�rio n�o existir
     */
    public String getAmigos(String login) {
        return usuarios.listarAmigos(login);
    }

    /**
     * Envia um recado privado de um usu�rio para outro.
     *
     * @param idSessao      ID da sess�o do remetente
     * @param destinatario  login do destinat�rio
     * @param mensagem      conte�do da mensagem
     * @throws AutoMensagemException       se enviar recado para si mesmo
     * @throws UsuarioNaoEncontradoException se destinat�rio n�o existir
     * @throws InimigoException            se destinat�rio for inimigo
     */
    public void enviarRecado(String idSessao, String destinatario, String mensagem) {
        Usuario remetente = getUsuarioPorSessao(idSessao);
        Usuario dest = usuarios.getUsuario(destinatario);
        if (remetente.getInimigos().contains(destinatario)) {
            String nomeInimigo = dest.getPerfil().getAtributo("nome");
            throw new InimigoException(nomeInimigo);
        }
        if (remetente.getLogin().equals(destinatario)) {
            throw new AutoMensagemException("Usu�rio n�o pode enviar recado para si mesmo.");
        }
        dest.adicionarRecado(new Recado(remetente.getLogin(), mensagem));
    }

    /**
     * L� o pr�ximo recado dispon�vel do usu�rio da sess�o.
     *
     * @param idSessao ID da sess�o ativa
     * @return texto do recado
     * @throws SemRecadosException         se n�o houver recados
     * @throws UsuarioNaoEncontradoException se sess�o inv�lida
     */
    public String lerRecado(String idSessao) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        return usuario.lerRecado();
    }

    /**
     * Cria uma comunidade e registra o usu�rio como dono e membro inicial.
     *
     * @param idSessao  ID da sess�o do criador
     * @param nome      nome �nico da comunidade
     * @param descricao descri��o da comunidade
     * @throws ComunidadeJaExisteException    se comunidade j� existir
     * @throws UsuarioNaoEncontradoException  se sess�o inv�lida
     */
    public void criarComunidade(String idSessao, String nome, String descricao) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        comunidades.criarComunidade(nome, descricao, usuario.getLogin());
        usuario.adicionarComunidade(nome);
    }

    /**
     * Retorna a descri��o de uma comunidade existente.
     *
     * @param nome nome da comunidade
     * @return descri��o cadastrada
     * @throws ComunidadeNaoEncontradaException se comunidade n�o existir
     */
    public String getDescricaoComunidade(String nome) {
        return comunidades.getDescricao(nome);
    }

    /**
     * Retorna o login do dono de determinada comunidade.
     *
     * @param nome nome da comunidade
     * @return login do propriet�rio
     * @throws ComunidadeNaoEncontradaException se comunidade n�o existir
     */
    public String getDonoComunidade(String nome) {
        return comunidades.getDono(nome);
    }

    /**
     * Lista membros de uma comunidade em formato string.
     *
     * @param nome nome da comunidade
     * @return lista formatada como "{membro1,membro2...}"
     * @throws ComunidadeNaoEncontradaException se comunidade n�o existir
     */
    public String getMembrosComunidade(String nome) {
        Community c = comunidades.getComunidade(nome);
        return "{" + String.join(",", c.getMembers()) + "}";
    }

    /**
     * Adiciona o usu�rio da sess�o a uma comunidade existente.
     *
     * @param idSessao       ID da sess�o
     * @param nomeComunidade nome da comunidade
     * @throws UsuarioNaoEncontradoException if sess�o inv�lida
     * @throws ComunidadeNaoEncontradaException if comunidade n�o existir
     */
    public void adicionarComunidade(String idSessao, String nomeComunidade) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        comunidades.adicionarMembro(nomeComunidade, usuario.getLogin());
        usuario.adicionarComunidade(nomeComunidade);
    }

    /**
     * Retorna comunidades �s quais o usu�rio pertence.
     *
     * @param login login do usu�rio
     * @return string formatada "{comun1,comun2,...}"
     */
    public String getComunidades(String login) {
        Usuario usuario = usuarios.getUsuario(login);
        return "{" + String.join(",", usuario.getComunidades()) + "}";
    }

    /**
     * Obt�m usu�rio associado � sess�o.
     *
     * @param idSessao ID da sess�o
     * @return objeto Usuario
     * @throws UsuarioNaoEncontradoException se sess�o inv�lida
     */
    private Usuario getUsuarioPorSessao(String idSessao) {
        String login = sessoes.getLogin(idSessao);
        if (login == null) {
            throw new UsuarioNaoEncontradoException();
        }
        return usuarios.getUsuario(login);
    }

    /**
     * Adiciona um �dolo (f�) para o usu�rio da sess�o.
     *
     * @param sessao ID da sess�o
     * @param idolo  login do usu�rio a ser idolatrado
     */
    public void adicionarIdolo(String sessao, String idolo) {
        Usuario usuario = getUsuarioPorSessao(sessao);
        usuarios.adicionarIdolo(usuario.getLogin(), idolo);
    }

    /**
     * Adiciona algu�m como paquera (privado).
     *
     * @param sessao  ID da sess�o
     * @param paquera login da paquera
     */
    public void adicionarPaquera(String sessao, String paquera) {
        Usuario usuario = getUsuarioPorSessao(sessao);
        usuarios.adicionarPaquera(usuario.getLogin(), paquera);
    }

    /**
     * Verifica rela��o de f�-�dolo.
     *
     * @param login login do f�
     * @param idolo login do �dolo
     * @return true se for f�, false caso contr�rio
     */
    public boolean ehFa(String login, String idolo) {
        return usuarios.getUsuario(login).getIdolos().contains(idolo);
    }

    /**
     * Marca algu�m como inimigo do usu�rio da sess�o.
     *
     * @param sessao  ID da sess�o
     * @param inimigo login do inimigo
     */
    public void adicionarInimigo(String sessao, String inimigo) {
        Usuario usuario = getUsuarioPorSessao(sessao);
        usuarios.adicionarInimigo(usuario.getLogin(), inimigo);
    }

    /**
     * Retorna f�s do usu�rio informado.
     *
     * @param login login do usu�rio
     * @return string "{fa1,fa2,...}" dos f�s
     */
    public String getFas(String login) {
        Usuario usuario = usuarios.getUsuario(login);
        return "{" + String.join(",", usuario.getFas()) + "}";
    }

    /**
     * Verifica rela��o de paquera.
     *
     * @param sessao  ID da sess�o do usu�rio
     * @param paquera login da paquera
     * @return true se existir a rela��o
     */
    public boolean ehPaquera(String sessao, String paquera) {
        Usuario usuario = getUsuarioPorSessao(sessao);
        return usuario.getPaqueras().contains(paquera);
    }

    /**
     * Retorna lista de paqueras do usu�rio da sess�o.
     *
     * @param sessao ID da sess�o
     * @return string formatada "{p1,p2,...}" das paqueras
     */
    public String getPaqueras(String sessao) {
        Usuario usuario = getUsuarioPorSessao(sessao);
        return "{" + String.join(",", usuario.getPaqueras()) + "}";
    }

    /**
     * Remove um usu�rio e limpa todas as refer�ncias a ele.
     *
     * @param idSessao ID da sess�o do usu�rio a ser removido
     * @throws UsuarioNaoEncontradoException se sess�o inv�lida
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
     * Valida login e senha n�o nulos/vazios.
     *
     * @param login login a validar
     * @param senha senha a validar
     * @throws LoginInvalidoException se login inv�lido
     * @throws SenhaInvalidaException se senha inv�lida
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
     * @return inst�ncia do sistema restaurada
     * @throws IOException            se falha de I/O ocorrer
     * @throws ClassNotFoundException se classe n�o for encontrada
     */
    public static Jackute carregarEstado(String arquivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (Jackute) ois.readObject();
        }
    }

    /**
     * Hook para garantir compatibilidade ao desserializar vers�es antigas.
     *
     * @param ois fluxo de entrada de objetos
     * @throws IOException            se falha de I/O
     * @throws ClassNotFoundException se classe n�o for encontrada
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
     * @param idSessao        ID da sess�o do remetente
     * @param nomeComunidade  nome da comunidade
     * @param mensagem        conte�do da mensagem
     */
    public void enviarMensagem(String idSessao, String nomeComunidade, String mensagem) {
        Usuario remetente = getUsuarioPorSessao(idSessao);
        comunidades.enviarMensagem(nomeComunidade, mensagem);
    }

    /**
     * L� pr�xima mensagem de comunidade do usu�rio da sess�o.
     *
     * @param idSessao ID da sess�o
     * @return texto da mensagem
     */
    public String lerMensagem(String idSessao) {
        Usuario usuario = getUsuarioPorSessao(idSessao);
        return usuario.lerMensagem();
    }
}
