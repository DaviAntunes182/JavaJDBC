package br.com.davi.projeto.domain.conta;

import br.com.davi.projeto.conexao.ConnectionFactory;
import br.com.davi.projeto.domain.cliente.Cliente;
import br.com.davi.projeto.domain.cliente.DadosCadastroCliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaDAO {

    private ConnectionFactory connF;
    private Connection conn;
    public ContaDAO() {
        this.connF = new ConnectionFactory();
    }

    public Set<Conta> read(){
        Set<Conta> contas = new HashSet<>();
        try {
            conn = connF.openConnection();
            String sql = "SELECT * FROM CONTA WHERE ATIVO = 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                // Posições das colunas que vem no resultset
                Cliente cliente = new Cliente(new DadosCadastroCliente(rs.getString(3), rs.getString(4), rs.getString(5)));
                Conta conta = new Conta(rs.getLong(1),rs.getBigDecimal(2), cliente);
                contas.add(conta);
            }
            ps.close();
            rs.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return contas;
    }

    public Conta readOne(Long numero){
        try {
            conn = connF.openConnection();
            String sql = "SELECT * FROM CONTA WHERE NUMERO = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, numero);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Cliente cliente = new Cliente(new DadosCadastroCliente(rs.getString(3), rs.getString(4), rs.getString(5)));
                Conta conta = new Conta(rs.getLong(1),rs.getBigDecimal(2), cliente, rs.getBoolean(6));
                ps.close();
                rs.close();
                conn.close();
                return conta;
            }
            else {
                conn.close();
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void create(DadosAberturaConta dadosAberturaConta){
        var cliente = new Cliente(dadosAberturaConta.dadosCliente());
        var conta = new Conta(dadosAberturaConta.numero(), BigDecimal.ZERO ,cliente);

        try {
            conn = connF.openConnection();
            String sql = "INSERT INTO CONTA(NUMERO, SALDO, CLIENTE_NOME, CLIENTE_CPF, CLIENTE_EMAIL) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1,conta.getNumero());
            ps.setBigDecimal(2, new BigDecimal("0.0"));
            ps.setString(3, cliente.getNome());
            ps.setString(4, cliente.getCpf());
            ps.setString(5, cliente.getEmail());
            ps.execute();
            ps.close();
            conn.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


    public void delete(Long numero){
        try {
            conn = connF.openConnection();
            String sql = "DELETE FROM CONTA WHERE NUMERO = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, numero);
            ps.execute();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateSaldo(Long numero, BigDecimal acrescimo){
        try {
            Conta conta = readOne(numero);
                BigDecimal novoSaldo = conta.getSaldo().add(acrescimo);
                this.conn = connF.openConnection();
                String sql = "UPDATE CONTA SET SALDO = ? WHERE NUMERO = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setBigDecimal(1, novoSaldo);
                ps.setLong(2, conta.getNumero());
                ps.execute();
                ps.close();
                conn.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void updateDesativar(Long numero){
        try {
            Conta conta = readOne(numero);
            this.conn = connF.openConnection();
            String sql = "UPDATE CONTA SET ATIVO = 0 WHERE NUMERO = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, conta.getNumero());
            ps.execute();
            ps.close();
            conn.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
