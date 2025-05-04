package br.ufal.ic.p2.jackut.services;


import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.*;
import java.io.Serializable;
import java.util.*;
import br.ufal.ic.p2.jackut.models.Recado;

/**
 * Gerencia todas as operações relacionadas a usuários, incluindo criação, armazenamento,
 * relacionamentos de amizade e persistência de dados.
 * <p>
 * Responsável por manter a integridade das relações de amizade e convites entre usuários.
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
     * Cria e armazena um novo usuário no sistema.
     *
     * @param login Identificador único do usuário
     * @param senha Senha de acesso
     * @param nome Nome completo do usuário
     * @throws UsuarioJaExisteException Se o login já estiver em uso
     */
    public void criarUsuario(String login, String senha, String nome) {
        if (usuarios.containsKey(login)) {
            throw new UsuarioJaExisteException();
        }
        usuarios.put(login, new Usuario(login, senha, nome));
    }

    /**
     * Recupera um usuário pelo seu login.
     *
     * @param login Identificador do usuário
     * @return Instância do usuário correspondente
     * @throws UsuarioNaoEncontradoException Se o usuário não existir
     */
    public Usuario getUsuario(String login) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();
        return usuario;
    }

    /**
     * Estabelece relação de amizade entre dois usuários.
     * <p>
     * Fluxo de operação:
     * 1. Verifica auto-amizade
     * 2. Checa amizade existente
     * 3. Verifica convites pendentes
     * 4. Cria nova solicitação ou confirma amizade
     * </p>
     *
     * @param loginUsuario Login do usuário iniciador
     * @param loginAmigo Login do usuário alvo
     * @throws AutoAmizadeException Se tentar adicionar a si mesmo
     * @throws UsuarioNaoEncontradoException Se algum usuário não existir
     * @throws AmigoJaAdicionadoException Se já forem amigos ou houver solicitação pendente
     */
    public void adicionarAmigo(String loginUsuario, String loginAmigo) {
        Usuario usuario = getUsuario(loginUsuario);
        Usuario amigo = getUsuario(loginAmigo);

        // Verifica se o amigo é um inimigo
        if (usuario.getInimigos().contains(loginAmigo)) {
            String nomeInimigo = amigo.getPerfil().getAtributo("nome");
            throw new InimigoException(nomeInimigo);
        }

        if (loginUsuario.equals(loginAmigo)) {
            throw new AutoAmizadeException();
        }

        if (usuario.getAmigos().contains(loginAmigo)) {
            throw new AmigoJaAdicionadoException("Usuário já está adicionado como amigo.");
        }

        if (usuario.temConvitePara(loginAmigo)) {
            throw new AmigoJaAdicionadoException("Usuário já está adicionado como amigo, esperando aceitação do convite.");
        }

        if (usuario.temConviteDe(loginAmigo)) {
            // Confirmação mútua de amizade
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
     * Verifica se dois usuários têm amizade mútua.
     *
     * @param login1 Primeiro usuário
     * @param login2 Segundo usuário
     * @return true se forem amigos mútuos, false caso contrário
     * @throws UsuarioNaoEncontradoException Se algum usuário não existir
     */
    public boolean saoAmigos(String login1, String login2) {
        Usuario u1 = getUsuario(login1);
        Usuario u2 = getUsuario(login2);
        return u1.getAmigos().contains(login2) && u2.getAmigos().contains(login1);
    }

    /**
     * Gera lista formatada de amigos de um usuário.
     *
     * @param login Usuário alvo
     * @return String no formato "{amigo1,amigo2}" com amigos em ordem alfabética
     * @throws UsuarioNaoEncontradoException Se o usuário não existir
     */
    public String listarAmigos(String login) {
        Usuario usuario = getUsuario(login);
        return "{" + String.join(",", usuario.getAmigosOrdenados()) + "}";
    }

    /**
     * Reinicia o gerenciador removendo todos os usuários.
     */
    public void zerar() {
        usuarios.clear();
    }


    // Métodos para novas relações
    public void adicionarIdolo(String fa, String idolo) {
        if (fa.equals(idolo)) {
            throw new AutoRelacaoException("Usuário não pode ser fã de si mesmo.");
        }

        Usuario usuarioFa = getUsuario(fa);
        Usuario usuarioIdolo = getUsuario(idolo);

        // Verifica se o ídolo é inimigo
        if (usuarioFa.getInimigos().contains(idolo)) {
            String nomeInimigo = usuarioIdolo.getPerfil().getAtributo("nome");
            throw new InimigoException(nomeInimigo);
        }

        usuarioFa.adicionarIdolo(idolo);
        usuarioIdolo.adicionarFa(fa);
    }

    public void adicionarPaquera(String usuario, String paquera) {
        if (usuario.equals(paquera)) {
            throw new AutoRelacaoException("Usuário não pode ser paquera de si mesmo.");
        }

        Usuario u = getUsuario(usuario);
        Usuario p = getUsuario(paquera);

        if (u.getInimigos().contains(paquera)) {
            throw new InimigoException(p.getPerfil().getAtributo("nome"));
        }

        u.adicionarPaquera(paquera);

        // Verifica paquera mútua
        if (p.getPaqueras().contains(usuario)) {
            enviarRecadoSistema(usuario, paquera);
        }
    }

    private void enviarRecadoSistema(String usuario1, String usuario2) {
        Usuario u1 = getUsuario(usuario1);
        Usuario u2 = getUsuario(usuario2);

        // Verifica se há inimizade antes de enviar recado
        if (u1.getInimigos().contains(usuario2) || u2.getInimigos().contains(usuario1)) {
            throw new InimigoException(u2.getPerfil().getAtributo("nome"));
        }


        String nome1 = u1.getPerfil().getAtributo("nome");
        String nome2 = u2.getPerfil().getAtributo("nome");

        u1.adicionarRecado(new Recado("Sistema", nome2 + " é seu paquera - Recado do Jackut."));
        u2.adicionarRecado(new Recado("Sistema", nome1 + " é seu paquera - Recado do Jackut."));
    }

    public void adicionarInimigo(String usuario, String inimigo) {
        if (usuario.equals(inimigo)) {
            throw new AutoRelacaoException("Usuário não pode ser inimigo de si mesmo.");
        }
        Usuario u = getUsuario(usuario);
        Usuario i = getUsuario(inimigo);

        if (u.getInimigos().contains(inimigo)) {
            throw new RelacaoExistenteException("Usuário já está adicionado como inimigo.");
        }
        u.adicionarInimigo(inimigo);
        i.adicionarInimigo(usuario); // Relação mútua
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

        // Verifica se o destinatário é inimigo
        if (rem.getInimigos().contains(destinatario)) {
            String nomeInimigo = dest.getPerfil().getAtributo("nome");
            throw new InimigoException(nomeInimigo);
        }

        dest.adicionarRecado(new Recado(rem.getLogin(), mensagem));
    }


}
