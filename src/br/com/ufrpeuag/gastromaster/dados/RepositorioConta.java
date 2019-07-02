package br.com.ufrpeuag.gastromaster.dados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.ufrpeuag.gastromaster.dados.interfaces.IContaDao;
import br.com.ufrpeuag.gastromaster.negocio.modelo.classes.Cardapio;
import br.com.ufrpeuag.gastromaster.negocio.modelo.classes.Conta;
import br.com.ufrpeuag.gastromaster.negocio.modelo.classes.Data;
import br.com.ufrpeuag.gastromaster.negocio.modelo.classes.Endereco;
import br.com.ufrpeuag.gastromaster.negocio.modelo.classes.Garcom;
import br.com.ufrpeuag.gastromaster.negocio.modelo.classes.Mesa;
import br.com.ufrpeuag.gastromaster.negocio.modelo.classes.Pedido;
import br.com.ufrpeuag.gastromaster.negocio.modelo.classes.Produto;

public class RepositorioConta implements IContaDao {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet result;
	private Statement stmt;

	public RepositorioConta() throws SQLException {
		conn = ConfiguracoesBanco.getSingleton().getConnection();
	}

	@Override
	public void inserir(Conta conta) {
		String inserirSql = "INSERT INTO  Conta(data, cod_pedido , cod_garcom , cod_mesa, valor) VALUES(?,?,?,?,?)";

		try {
			pstmt = this.conn.prepareStatement(inserirSql);

			pstmt.setString(1, Data.mudarDataParaString(conta.getData()));
			pstmt.setInt(2, conta.getPedido().getId_pedido());
			pstmt.setInt(3, conta.getGarcom().getId_garcom());
			pstmt.setInt(4, conta.getMesa().getId_mesa());
			pstmt.setDouble(5, conta.getValor());
			pstmt.executeUpdate();

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {

			try {
				pstmt.close();
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	@Override
	public Conta recuperar(Integer codigo) {
		
		String sql = "SELECT * from conta c join Pedido p on c.cod_pedido = p.id_pedido where c.id_conta = ?;";

		Conta c = new Conta();

		try {

			pstmt = this.conn.prepareStatement(sql);
			pstmt.setInt(1, codigo);

			result = pstmt.executeQuery();

			if (result.next()) {

				c.setId_conta(result.getInt("id_conta"));
				c.setValor(0);
				c.setData(null);
				c.setGarcom(null);
				c.setMesa(null);
				c.setPedido(null);

			}
			return c;
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {

			try {
				result.close();
				pstmt.close();
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}

		}
		return null;
	}

	@Override
	public void alterar(Conta conta) {
		String alterarSql = "UPDATE Conta SET data= ? , " + "cod_pedido= ? , cod_garcom= ? , " + "cod_mesa= ?,"
				+ " valor= ?  WHERE id_conta = ?";

		try {

			pstmt = this.conn.prepareStatement(alterarSql);

			pstmt.setString(1, Data.mudarDataParaString(conta.getData()));
			pstmt.setInt(2, conta.getPedido().getId_pedido());
			pstmt.setInt(3, conta.getGarcom().getId_garcom());
			pstmt.setInt(4, conta.getMesa().getId_mesa());
			pstmt.setDouble(5, conta.getValor());
			pstmt.setDouble(6, conta.getId_conta());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {

			try {
				pstmt.close();
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}

		}
	}

	@Override
	public void deletar(Conta conta) {
		String deletarSql = "DELETE FROM Conta WHERE id_conta = ?";
		try {

			pstmt = this.conn.prepareStatement(deletarSql);

			pstmt.setInt(1, conta.getId_conta());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {

			try {
				pstmt.close();
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	@Override
	public List<Conta> listarTodos() {
		Conta c = null;
		Mesa m = null;
		Pedido pedido = null;
		Garcom g = null;
		Produto prod = null;
		Cardapio card = null;
		Endereco e = null;
		List<Conta> lista = new ArrayList<>();

		String listarTodosSql = "SELECT *\r\n" + "FROM Conta c join pedido p on (c.cod_pedido=p.id_pedido) \r\n"
				+ "JOIN Cardapio card on (p.cod_cardapio=card.id_cardapio)  \r\n"
				+ "JOIN Produto prod on(p.cod_produto =Prod.id_produto) \r\n"
				+ "JOIN Garcom g on (c.cod_garcom = g.id_garcom)\r\n"
				+ "JOIN Endereco e on (g.cod_endereco=e.id_endereco)\r\n"
				+ "JOIN Mesa m on ( c.cod_mesa =m.id_mesa )\r\n";

		try {

			stmt = this.conn.createStatement();
			result = stmt.executeQuery(listarTodosSql);

			while (result.next()) {

				c = new Conta();
				m = new Mesa();
				pedido = new Pedido();
				g = new Garcom();
				card = new Cardapio();
				prod = new Produto();
				e = new Endereco();

				c.setId_conta(result.getInt("id_conta"));
				c.setData(Data.mudarDataParaLocalDate(result.getString("data")));

				// Pedido
				pedido.setId_pedido(result.getInt("id_pedido"));

				card.setId_cardapio(result.getInt("Id_cardapio"));
				card.setPrato(result.getString("prato"));
				card.setPreco(result.getDouble("preco"));
				pedido.setCardapio(card);

				prod.setId_produto(result.getInt("id_produto"));
				prod.setNome(result.getString("nome"));
				prod.setQuantidade(result.getInt("quantidade"));
				prod.setPreco(result.getDouble("preco"));
				pedido.setProduto(prod);
				pedido.setValor(result.getDouble("valor"));
				c.setPedido(pedido);

				// Garcom
				g.setId_garcom(result.getInt("id_garcom"));
				g.setNome(result.getString("nome"));
				g.setCpf(result.getString("cpf"));
				g.setDataNasc(Data.mudarDataParaLocalDate(result.getString("dataNasc")));
				g.setTelefone(result.getString("telefone"));
				g.setEmail(result.getString("email"));
				g.setSalario(result.getDouble("salario"));
				e.setId_endereco(result.getInt("id_endereco"));
				e.setCidade(result.getString("cidade"));
				e.setBairro(result.getString("Bairro"));
				e.setRua(result.getString("rua"));
				e.setNumero(result.getInt("numero"));
				e.setCep(result.getString("cep"));
				g.setIdentificador(result.getString("identificador"));
				g.setEndereco(e);

				c.setGarcom(g);

				// Mesa
				m.setId_mesa(result.getInt("id_mesa"));
				m.setNumero(result.getInt("numero"));
				m.setDisponibilidade(result.getInt("disponibilidade"));

				c.setMesa(m);

				c.setValor(result.getDouble("valor"));
				lista.add(c);

			}
			return lista;
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {

			try {
				result.close();
				stmt.close();
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}

		}
		return null;
	}

	@Override
	public double mostrarValorConta(Conta conta) {
		String sql = "SELECT * FROM Conta WHERE cod_mesa = ?;";

		double valor = 0;
		try {

			pstmt = this.conn.prepareStatement(sql);
			pstmt.setInt(1, conta.getMesa().getId_mesa());

			result = pstmt.executeQuery();
			while (result.next()) {
				valor += (result.getDouble("valor"));

			}
			return valor;

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {

			try {
				result.close();
				pstmt.close();
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}

		}
		return 0;
	}

	@Override
	public List<Conta> recuperarPorMesa(Integer codigo) {
		Conta c = null;
		Mesa m = null;
		Pedido pedido = null;
		Garcom g = null; 
		List<Conta> lista = new ArrayList<>();

		String sql = "SELECT *\r\n" + 
				"from conta \r\n" + 
				"where cod_mesa = ?";

		try {

			pstmt = this.conn.prepareStatement(sql);
			pstmt.setInt(1, codigo);

			result = pstmt.executeQuery();

			while (result.next()) {
				c = new Conta();
				m = new Mesa();
				pedido = new Pedido();
				g = new Garcom();

				c.setId_conta(result.getInt("id_conta"));
				c.setData(Data.mudarDataParaLocalDate(result.getString("data")));

				// Pedido
				pedido.setId_pedido(result.getInt("cod_pedido"));
				c.setPedido(pedido);		
				
				// Garcom
				g.setId_garcom(result.getInt("cod_garcom"));
				c.setGarcom(g);

				// Mesa
				m.setId_mesa(result.getInt("cod_mesa"));
				c.setMesa(m);

				c.setValor(result.getDouble("valor"));
				lista.add(c);
			}
			return lista;

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {

			try {
				result.close();
				pstmt.close();
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}

		}
		return null;
	}

	@Override
	public void deletarTodasContas(Conta conta) {
		String deletarSql = "DELETE FROM Conta WHERE cod_mesa = ?";
		try {

			pstmt = this.conn.prepareStatement(deletarSql);

			pstmt.setInt(1, conta.getMesa().getId_mesa());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {

			try {
				pstmt.close();
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

}
