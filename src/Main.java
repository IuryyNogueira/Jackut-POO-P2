import easyaccept.EasyAccept;

/**
 * Classe principal respons�vel por executar os testes de aceita��o do sistema Jackut.
 * <p>
 * Esta classe utiliza o framework EasyAccept para validar as funcionalidades implementadas
 * conforme as user stories definidas.
 * </p>
 *
 * @author IuryNogueira
 * @version 1.0
 */
public class Main {
    /**
     * Ponto de entrada do programa. Executa sequencialmente todos os testes de aceita��o
     * para as user stories US1 a US4, incluindo testes de persist�ncia.
     *
     * @param args Argumentos de linha de comando (n�o utilizados neste contexto)
     */
    public static void main(String[] args) {
        // Configura��o dos par�metros para cada conjunto de testes
        String[] argsUS1_1 = {"br.ufal.ic.p2.jackut.Facade", "tests/us1_1.txt"};
        String[] argsUS1_2 = {"br.ufal.ic.p2.jackut.Facade", "tests/us1_2.txt"};
        String[] argsUS2_1 = {"br.ufal.ic.p2.jackut.Facade", "tests/us2_1.txt"};
        String[] argsUS2_2 = {"br.ufal.ic.p2.jackut.Facade", "tests/us2_2.txt"};
        String[] argsUS3_1 = {"br.ufal.ic.p2.jackut.Facade", "tests/us3_1.txt"};
        String[] argsUS3_2 = {"br.ufal.ic.p2.jackut.Facade", "tests/us3_2.txt"};
        String[] argsUS4_1 = {"br.ufal.ic.p2.jackut.Facade", "tests/us4_1.txt"};
        String[] argsUS4_2 = {"br.ufal.ic.p2.jackut.Facade", "tests/us4_2.txt"};

        // Execu��o sequencial dos testes
        EasyAccept.main(argsUS1_1);  // Testes US1 - Cria��o de conta (casos b�sicos)
        EasyAccept.main(argsUS1_2);  // Testes US1 - Valida��o de persist�ncia
        EasyAccept.main(argsUS2_1);  // Testes US2 - Perfil (opera��es b�sicas)
        EasyAccept.main(argsUS2_2);  // Testes US2 - Valida��o de persist�ncia
        EasyAccept.main(argsUS3_1);  // Testes US3 - Amizades (casos b�sicos)
        EasyAccept.main(argsUS3_2);  // Testes US3 - Valida��o de persist�ncia
        EasyAccept.main(argsUS4_1);  // Testes US4 - Recados (opera��es b�sicas)
        EasyAccept.main(argsUS4_2);  // Testes US4 - Valida��o de persist�ncia
    }
}