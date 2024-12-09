package br.com.davi.projeto.domain.conta;

import br.com.davi.projeto.domain.cliente.DadosCadastroCliente;

public record DadosAberturaConta(Long numero, DadosCadastroCliente dadosCliente) {
}