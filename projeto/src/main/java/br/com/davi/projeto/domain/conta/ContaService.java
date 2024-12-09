package br.com.davi.projeto.domain.conta;

import br.com.davi.projeto.domain.RegraDeNegocioException;
import br.com.davi.projeto.domain.cliente.DadosCadastroCliente;
import java.math.BigDecimal;
import java.util.Scanner;

public class ContaService {
    private static Scanner sc = new Scanner(System.in).useDelimiter("\n");
    private static ContaDAO daoService = new ContaDAO();
    public ContaService(){
    }

    public void listarContasAbertas() {
        var contas = daoService.read();
        System.out.println("Contas cadastradas:");
        contas.stream().forEach(System.out::println);
    }

    public void abrir() {
        var numeroDaConta = pegarNumeroConta();
        if(numeroDaConta != 0L) {
            System.out.println("Digite o nome do cliente:");
            var nome = sc.next();

            System.out.println("Digite o cpf do cliente:");
            var cpf = sc.next();

            System.out.println("Digite o email do cliente:");
            var email = sc.next();

            DadosAberturaConta dadosAberturaConta = new DadosAberturaConta(numeroDaConta, new DadosCadastroCliente(nome, cpf, email));
            daoService.create(dadosAberturaConta);
            System.out.println("Abertura de conta concluída!");
        }else {
            System.out.println("Digite um número válido!");
        }
    }

    public void consultarSaldo() {
        var numeroDaConta = pegarNumeroConta();
        if(numeroDaConta != 0){
            if (daoService.readOne(numeroDaConta) != null) {
                var conta = daoService.readOne(numeroDaConta);
                if(conta.getAtiva()) {
                    System.out.println("Saldo da conta: " + conta.getSaldo());
                }
                else {
                    System.out.println("Conta inativa, por favor reative-a para ver o saldo");
                }
            }
            else{
                System.out.println("Conta inexistente");
            }
        }
        else {
            System.out.println("Digite um número válido!");
        }
    }

    public void encerrar() {
        var numeroDaConta = pegarNumeroConta();
        if(numeroDaConta != 0){
            var Conta = daoService.readOne(numeroDaConta);
            if (Conta != null) {
                if (!Conta.possuiSaldo()) {
                    daoService.delete(numeroDaConta);
                    System.out.println("Conta encerrada!");
                } else {
                    throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo!");
                }
            } else {
                System.out.println("Conta não existe");
            }
        }else{
            System.out.println("Digite um número válido");
        }
    }

    public void realizarSaque() {
        var numeroDaConta = pegarNumeroConta();
        if(numeroDaConta != 0) {
            var Conta = daoService.readOne(numeroDaConta);
            if(Conta != null) {
                if (Conta.possuiSaldo()) {
                    System.out.println("Valor a sacar");
                    var saq = new BigDecimal(sc.next());
                    if (compararSaldo(Conta, saq)) {
                        if (saq.compareTo(BigDecimal.ZERO) > 0) {
                            saq = saq.multiply(new BigDecimal(-1));
                            daoService.updateSaldo(numeroDaConta, saq);
                            System.out.println("Saque realizado");
                        } else {
                            throw new RegraDeNegocioException("Valor do saque deve ser superior a zero!");
                        }
                    }else{
                        System.out.println("Saldo insuficiente");
                    }
                }
                else {
                    System.out.println("Conta sem saldo para retirada");
                }
            }
            else {
                System.out.println("A conta não existe");
            }
        }
        else {
            System.out.println("Digite um número válido");
        }
    }

    public void realizarDeposito() {
        var numeroDaConta = pegarNumeroConta();
        if(numeroDaConta != 0){
            if(daoService.readOne(numeroDaConta) != null) {
                System.out.println("Digite o valor do depósito:");
                var dep = new BigDecimal(sc.next());
                daoService.updateSaldo(numeroDaConta, dep);
                System.out.println("Deposito realizado!");
            }else {
                System.out.println("Conta não existe!");
            }
        }else {
            System.out.println("Digite um número válido");
        }
    }

    public void realizarTranferencia(){
        System.out.print("Remetende -> ");
        var reme = daoService.readOne(pegarNumeroConta());
        System.out.print("Destino -> ");
        var dest = daoService.readOne(pegarNumeroConta());
        if(reme != null && dest!= null) {
            if(reme.possuiSaldo()) {
                System.out.println("Digite o valor para ser transferido");
                var valor = new BigDecimal(sc.next());
                if(compararSaldo(reme, valor)){
                    if(valor.compareTo(BigDecimal.ZERO) > 0) {
                        daoService.updateSaldo(reme.getNumero(), valor.multiply(new BigDecimal("-1")));
                        daoService.updateSaldo(dest.getNumero(), valor);
                        System.out.println("Transferência realizada!");
                    } else {
                        System.out.println("Valor inválido, digite um valor maior que 0");
                    }
                }else{
                    System.out.println("Saldo insuficiente para transferência");
                }
            }
            else {
                System.out.println("Conta sem saldo");
            }
        }
        else{
            System.out.println("Uma das contas não existe");
        }
    }

    private Long pegarNumeroConta(){
        System.out.println("Digite o número da conta");
        if(sc.hasNextLong()){
            return sc.nextLong();
        }else {
            return 0L;
        }
    }

    private boolean compararSaldo(Conta conta, BigDecimal valor){
        return conta.getSaldo().compareTo(valor) > 0;
    }

    public void desativarConta(){
        var numeroDaConta = pegarNumeroConta();
        if(numeroDaConta != 0){
            var Conta = daoService.readOne(numeroDaConta);
            if (Conta != null) {
                if (!Conta.possuiSaldo()) {
                    daoService.updateDesativar(numeroDaConta);
                    System.out.println("Conta desativada");
                } else {
                    throw new RegraDeNegocioException("Conta não pode ser desativada pois ainda possui saldo!");
                }
            } else {
                System.out.println("Conta não existe");
            }
        }else{
            System.out.println("Digite um número válido");
        }
    }
}
