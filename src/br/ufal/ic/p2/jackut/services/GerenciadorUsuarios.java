package br.ufal.ic.p2.jackut.services;


import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.*;
import java.io.Serializable;
import java.util.*;
import br.ufal.ic.p2.jackut.models.Recado;

/**
 * Gerencia todas as opera��es relacionadas a usu�rios, incluindo cria��o, armazenamento,
 * relacionamentos de amizade e persist�ncia de dados.
 * <p>
 * Respons�vel por manter a integridade das rela��es de amizade e convites entre usu�rios.
 * </p>
 *
 * @author IuryNogueira
 * @version 1.0
 * @see Usuario
 */
public class GerenciadorUsuarios implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<String, Usuario> usuarios = new HashMap<>();


    /**
     * Cria e armazena um novo usu�rio no sistema.
     *
     * @param login Identificador �nico do usu�rio
     * @param senha Senha de acesso
     * @param nome Nome completo do usu�rio
     * @throws UsuarioJaExisteException Se o login j� estiver em uso
     */
    public void criarUsuario(String login, String senha, String nome) {
        if (usuarios.containsKey(login)) {
            throw new UsuarioJaExisteException();
        }
        usuarios.put(login, new Usuario(login, senha, nome));
    }

    /**
     * Recupera um usu�rio pelo seu login.
     *
     * @param login Identificador do usu�rio
     * @return Inst�ncia do usu�rio correspondente
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o existir
     */
    public Usuario getUsuario(String login) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();
        return usuario;
    }

    /**
     * Estabelece rela��o de amizade entre dois usu�rios.
     * <p>
     * Fluxo de opera��o:
     * 1. Verifica auto-amizade
     * 2. Checa amizade existente
     * 3. Verifica convites pendentes
     * 4. Cria nova solicita��o ou confirma amizade
     * </p>
     *
     * @param loginUsuario Login do usu�rio iniciador
     * @param loginAmigo Login do usu�rio alvo
     * @throws AutoAmizadeException Se tentar adicionar a si mesmo
     * @throws UsuarioNaoEncontradoException Se algum usu�rio n�o existir
     * @throws AmigoJaAdicionadoException Se j� forem amigos ou houver solicita��o pendente
     */
    public void adicionarAmigo(String loginUsuario, String loginAmigo) {
        Usuario usuario = getUsuario(loginUsuario);
        Usuario amigo = getUsuario(loginAmigo);

        // Verifica se o amigo � um inimigo
        if (usuario.getInimigos().contains(loginAmigo)) {
            String nomeInimigo = amigo.getPerfil().getAtributo("nome");
            throw new InimigoException(nomeInimigo);
        }

        if (loginUsuario.equals(loginAmigo)) {
            throw new AutoAmizadeException();
        }

        if (usuario.getAmigos().contains(loginAmigo)) {
            throw new AmigoJaAdicionadoException("Usu�rio j� est� adicionado como amigo.");
        }

        if (usuario.temConvitePara(loginAmigo)) {
            throw new AmigoJaAdicionadoException("Usu�rio j� est� adicionado como amigo, esperando aceita��o do convite.");
        }

        if (usuario.temConviteDe(loginAmigo)) {
            // Confirma��o m�tua de amizade
            usuario.adicionarAmigo(loginAmigo);
            amigo.adicionarAmigo(loginUsuario);
            usuario.removerConvite(loginAmigo);
            amigo.removerConvite(loginUsuario);
        } else {
            // Cria novo convite
            usuario.adicionarConvite(loginAmigo, Usuario.ConviteStatus.ENVIADO);
            amigo.adicionarConvite(loginUsuario, Usuario.ConviteStatus.RECEBIDO);
        }
    }

    /**
     * Verifica se dois usu�rios t�m amizade m�tua.
     *
     * @param login1 Primeiro usu�rio
     * @param login2 Segundo usu�rio
     * @return true se forem amigos m�tuos, false caso contr�rio
     * @throws UsuarioNaoEncontradoException Se algum usu�rio n�o existir
     */
    public boolean saoAmigos(String login1, String login2) {
        Usuario u1 = getUsuario(login1);
        Usuario u2 = getUsuario(login2);
        return u1.getAmigos().contains(login2) && u2.getAmigos().contains(login1);
    }

    /**
     * Gera lista formatada de amigos de um usu�rio.
     *
     * @param login Usu�rio alvo
     * @return String no formato "{amigo1,amigo2}" com amigos em ordem alfab�tica
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o existir
     */
    public String listarAmigos(String login) {
        Usuario usuario = getUsuario(login);
        return "{" + String.join(",", usuario.getAmigosOrdenados()) + "}";
    }

    /**
     * Reinicia o gerenciador removendo todos os usu�rios.
     */
    public void zerar() {
        usuarios.clear();
    }


    // M�todos para novas rela��es
    public void adicionarIdolo(String fa, String idolo) {
        if (fa.equals(idolo)) {
            throw new AutoRelacaoException("Usu�rio n�o pode ser f� de si mesmo.");
        }

        Usuario usuarioFa = getUsuario(fa);
        Usuario usuarioIdolo = getUsuario(idolo);

        // Verifica se o �dolo � inimigo
        if (usuarioFa.getInimigos().contains(idolo)) {
            String nomeInimigo = usuarioIdolo.getPerfil().getAtributo("nome");
            throw new InimigoException(nomeInimigo);
        }

        usuarioFa.adicionarIdolo(idolo);
        usuarioIdolo.adicionarFa(fa);
    }

    public void adicionarPaquera(String usuario, String paquera) {
        if (usuario.equals(paquera)) {
            throw new AutoRelacaoException("Usu�rio n�o pode ser paquera de si mesmo.");
        }

        Usuario u = getUsuario(usuario);
        Usuario p = getUsuario(paquera);

        if (u.getInimigos().contains(paquera)) {
            throw new InimigoException(p.getPerfil().getAtributo("nome"));
        }

        u.adicionarPaquera(paquera);

        // Verifica paquera m�tua
        if (p.getPaqueras().contains(usuario)) {
            enviarRecadoSistema(usuario, paquera);
        }
    }

    private void enviarRecadoSistema(String usuario1, String usuario2) {
        Usuario u1 = getUsuario(usuario1);
        Usuario u2 = getUsuario(usuario2);

        // Verifica se h� inimizade antes de enviar recado
        if (u1.getInimigos().contains(usuario2) || u2.getInimigos().contains(usuario1)) {
            throw new InimigoException(u2.getPerfil().getAtributo("nome"));
        }


        String nome1 = u1.getPerfil().getAtributo("nome");
        String nome2 = u2.getPerfil().getAtributo("nome");

        u1.adicionarRecado(new Recado("Sistema", nome2 + " � seu paquera - Recado do Jackut."));
        u2.adicionarRecado(new Recado("Sistema", nome1 + " � seu paquera - Recado do Jackut."));
    }

    public void adicionarInimigo(String usuario, String inimigo) {
        if (usuario.equals(inimigo)) {
            throw new AutoRelacaoException("Usu�rio n�o pode ser inimigo de si mesmo.");
        }
        Usuario u = getUsuario(usuario);
        Usuario i = getUsuario(inimigo);

        if (u.getInimigos().contains(inimigo)) {
            throw new RelacaoExistenteException("Usu�rio j� est� adicionado como inimigo.");
        }
        u.adicionarInimigo(inimigo);
        i.adicionarInimigo(usuario); // Rela��o m�tua
    }

    public boolean ehFa(String loginFa, String loginIdolo) {
        Usuario fa = getUsuario(loginFa);
        return fa.getIdolos().contains(loginIdolo);
    }

    public String getFas(String loginIdolo) {
        Usuario idolo = getUsuario(loginIdolo);
        return "{" + String.join(",", idolo.getFas()) + "}";
    }

    public boolean ehPaquera(String loginUsuario, String loginPaquera) {
        Usuario usuario = getUsuario(loginUsuario);
        return usuario.getPaqueras().contains(loginPaquera);
    }

    public String getPaqueras(String loginUsuario) {
        Usuario usuario = getUsuario(loginUsuario);
        return "{" + String.join(",", usuario.getPaqueras()) + "}";
    }

    public void enviarRecado(String remetente, String destinatario, String mensagem) {
        Usuario rem = getUsuario(remetente);
        Usuario dest = getUsuario(destinatario);

        // Verifica se o destinat�rio � inimigo
        if (rem.getInimigos().contains(destinatario)) {
            String nomeInimigo = dest.getPerfil().getAtributo("nome");
            throw new InimigoException(nomeInimigo);
        }

        dest.adicionarRecado(new Recado(rem.getLogin(), mensagem));
    }


}
