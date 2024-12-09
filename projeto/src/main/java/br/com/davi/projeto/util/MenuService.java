package br.com.davi.projeto.util;

import br.com.davi.projeto.domain.RegraDeNegocioException;
import br.com.davi.projeto.domain.conta.ContaService;

import java.util.Scanner;

public class MenuService {
    private static ContaService service = new ContaService();

    private static Scanner teclado = new Scanner(System.in).useDelimiter("\n");
    public void iniciar(){
        var opcao = 0;
        while (opcao != 9) {
            opcao = exibirMenu();
            try {
                switch (opcao) {
                    case 1 -> service.listarContasAbertas();
                    case 2 -> service.abrir();
                    case 3 -> service.encerrar();
                    case 4 -> service.consultarSaldo();
                    case 5 -> service.realizarSaque();
                    case 6 -> service.realizarDeposito();
                    case 7 -> service.realizarTranferencia();
                    case 8 -> service.desativarConta();
                    case 9 -> finalizarAplic();
                    default -> System.out.println("Digite um número entre 1 e 9");
                }
            } catch (RegraDeNegocioException e) {
                System.out.println("Erro: " +e.getMessage());
                System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu");
                teclado.next();
            }
            System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu");
            teclado.next();
        }
    }

    private static int exibirMenu() {
        System.out.println("""
                BYTEBANK - ESCOLHA UMA OPÇÃO:
                1 - Listar contas
                2 - Abrir uma conta
                3 - Encerrar conta
                4 - Consultar saldo
                5 - Realizar saque
                6 - Realizar depósito
                7 - Transferir
                8 - Desativar conta
                9 - Sair
                """);
        if(teclado.hasNextInt()){
            return teclado.nextInt();
        }else {
            return 0;
        }
    }
    private void finalizarAplic(){
        System.out.println("Finalizando a aplicação!");
        System.exit(0);
    }
}
