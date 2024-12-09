package br.com.davi.projeto.domain.conta;

import br.com.davi.projeto.domain.cliente.Cliente;

import java.math.BigDecimal;
import java.util.Objects;

public class Conta {

    private Long numero;
    private BigDecimal saldo;
    private Cliente titular;
    private Boolean ativa;

    public Conta(Long numero, BigDecimal saldo, Cliente titular) {
        this.numero = numero;
        this.titular = titular;
        this.saldo = saldo;
        this.ativa = true;
    }
    public Conta(Long numero, BigDecimal saldo, Cliente titular, Boolean ativa) {
        this.numero = numero;
        this.titular = titular;
        this.saldo = saldo;
        this.ativa = ativa;
    }

    public boolean possuiSaldo() {
        return this.saldo.compareTo(BigDecimal.ZERO) > 0;
    }

    public void sacar(BigDecimal valor) {
        this.saldo = this.saldo.subtract(valor);
    }

    public void depositar(BigDecimal valor) {
        this.saldo = this.saldo.add(valor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conta conta = (Conta) o;
        return numero.equals(conta.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    @Override
    public String toString() {
        return "Conta{" +
                "numero='" + numero + '\'' +
                ", saldo=" + saldo +
                ", titular=" + titular +
                '}';
    }

    public Long getNumero() {
        return numero;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public Cliente getTitular() {
        return titular;
    }
}