package br.com.ufrpeuag.gastromaster.dados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.ufrpeuag.gastromaster.dados.interfaces.IGerenteDao;
import br.com.ufrpeuag.gastromaster.negocio.modelo.classes.Data;
import br.com.ufrpeuag.gastromaster.negocio.modelo.classes.Endereco;
import br.com.ufrpeuag.gastromaster.negocio.modelo.classes.Gerente;

public class RepositorioGerente implements IGerenteDao {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet result;
	private Statement stmt;

	public RepositorioGerente() throws SQLException {
		conn = ConfiguracoesBanco.getSingleton().getConnection();
	}

	@Override
	public void inserir(Gerente gerente) {
		String inserirSql = "INSERT INTO Gerente (nome, cpf, dataNasc, telefone, email, salario,senha,identificador,cod_endereco)"
				+ " VALUES(?,?,?,?,?,?,?,?,?)";
		try {

			pstmt = conn.prepareStatement(inserirSql);

			pstmt.setString(1, gerente.getNome());
			pstmt.setString(2, gerente.getCpf());
			pstmt.setString(3, Data.mudarDataParaString(gerente.getDataNasc()));
			pstmt.setString(4, gerente.getTelefone());
			pstmt.setString(5, gerente.getEmail());
			pstmt.setDouble(6, gerente.getSalario());
			pstmt.setString(7, gerente.getSenha());
			pstmt.setString(8, gerente.getIdentificador());
			pstmt.setInt(9, gerente.getEndereco().getId_endereco());
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
	public Gerente recuperar(Integer codigo) {
		Gerente g = null;
		Endereco e = null;
		String sqlRecuperar = "SELECT * from Gerente g join Endereco e on g.cod_endereco=e.id_endereco where id_gerente = ? ;";

		try {

			pstmt = conn.prepareStatement(sqlRecuperar);
			pstmt.setInt(1, codigo);
			result = pstmt.executeQuery();

			if (result.next()) {

				g = new Gerente();
				e = new Endereco();
				g.setId_gerente(result.getInt("id_gerente"));
				g.setNome(result.getString("nome"));
				g.setCpf(result.getString("cpf"));
				g.setDataNasc(Data.mudarDataParaLocalDate(result.getString("dataNasc")));
				g.setTelefone(result.getString("telefone"));
				g.setEmail(result.getString("email"));
				g.setSalario(result.getDouble("salario"));
				g.setSenha(result.getString("senha"));
				e.setId_endereco(result.getInt("id_endereco"));
				e.setCidade(result.getString("cidade"));
				e.setBairro(result.getString("Bairro"));
				e.setRua(result.getString("rua"));
				e.setNumero(result.getInt("numero"));
				e.setCep(result.getString("cep"));
				g.setIdentificador(result.getString("identificador"));
				g.setEndereco(e);
				return g;
			}

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
	public void alterar(Gerente gerente) {
		String alterarSql = "UPDATE Gerente SET " + "nome = ? , " + "cpf = ?, " + "dataNasc = ?," + "telefone = ?,"
				+ "email = ?," + "salario = ?," + "senha = ?, identificador = ? " + " WHERE id_gerente = ?";
		try {

			pstmt = conn.prepareStatement(alterarSql);

			pstmt.setString(1, gerente.getNome());
			pstmt.setString(2, gerente.getCpf());
			pstmt.setString(3, Data.mudarDataParaString(gerente.getDataNasc()));
			pstmt.setString(4, gerente.getTelefone());
			pstmt.setString(5, gerente.getEmail());
			pstmt.setDouble(6, gerente.getSalario());
			pstmt.setString(7, gerente.getSenha());
			pstmt.setString(8, gerente.getIdentificador());
			pstmt.setInt(9, gerente.getId_gerente());

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
	public void deletar(Gerente gerente) {
		String deletarSql = "DELETE FROM Gerente WHERE id_gerente = ?";

		try {

			pstmt = conn.prepareStatement(deletarSql);

			pstmt.setInt(1, gerente.getId_gerente());
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
	public List<Gerente> listarTodos() {
		Gerente g = null;
		Endereco e = null;
		List<Gerente> lista = new ArrayList<>();
		String listarTodosSql = "Select * FROM Gerente join endereco  on cod_endereco = id_endereco ";

		try {
			stmt = conn.createStatement();
			result = stmt.executeQuery(listarTodosSql);

			while (result.next()) {

				g = new Gerente();
				e = new Endereco();

				g.setId_gerente(result.getInt("id_gerente"));
				g.setNome(result.getString("nome"));
				g.setCpf(result.getString("cpf"));
				g.setDataNasc(Data.mudarDataParaLocalDate(result.getString("dataNasc")));
				g.setTelefone(result.getString("telefone"));
				g.setEmail(result.getString("email"));
				g.setSalario(result.getDouble("salario"));
				g.setSenha(result.getString("senha"));
				e.setId_endereco(result.getInt("id_endereco"));
				e.setCidade(result.getString("cidade"));
				e.setBairro(result.getString("Bairro"));
				e.setRua(result.getString("rua"));
				e.setNumero(result.getInt("numero"));
				e.setCep(result.getString("cep"));
				g.setIdentificador(result.getString("identificador"));
				g.setEndereco(e);

				lista.add(g);

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
	public Gerente recuperarCPF(String cpf) {
		Gerente g = null;
		Endereco e = null;
		String sqlRecuperarCpf = "SELECT * from Gerente g join Endereco e on g.cod_endereco=e.id_endereco where cpf = ? ;";
		try {

			pstmt = conn.prepareStatement(sqlRecuperarCpf);
			pstmt.setString(1, cpf);

			result = pstmt.executeQuery();
			if (result.next()) {

				g = new Gerente();
				e = new Endereco();
				g.setId_gerente(result.getInt("id_gerente"));
				g.setNome(result.getString("nome"));
				g.setCpf(result.getString("cpf"));
				g.setDataNasc(Data.mudarDataParaLocalDate(result.getString("dataNasc")));
				g.setTelefone(result.getString("telefone"));
				g.setEmail(result.getString("email"));
				g.setSalario(result.getDouble("salario"));
				g.setSenha(result.getString("senha"));
				e.setId_endereco(result.getInt("id_endereco"));
				e.setCidade(result.getString("cidade"));
				e.setBairro(result.getString("Bairro"));
				e.setRua(result.getString("rua"));
				e.setNumero(result.getInt("numero"));
				e.setCep(result.getString("cep"));
				g.setIdentificador(result.getString("identificador"));
				g.setEndereco(e);
				return g;
			}

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
	public Gerente verificarIdentificador(String identificador) {

		Gerente g = null;
		Endereco e = null;
		String verificarIdentificador = "SELECT * from Gerente g join Endereco e on g.cod_endereco=e.id_endereco where identificador = ?;";

		try {

			pstmt = conn.prepareStatement(verificarIdentificador);
			pstmt.setString(1, identificador);
			result = pstmt.executeQuery();

			if (result.next()) {

				g = new Gerente();
				e = new Endereco();
				g.setId_gerente(result.getInt("id_gerente"));
				g.setNome(result.getString("nome"));
				g.setCpf(result.getString("cpf"));
				g.setDataNasc(Data.mudarDataParaLocalDate(result.getString("dataNasc")));
				g.setTelefone(result.getString("telefone"));
				g.setEmail(result.getString("email"));
				g.setSalario(result.getDouble("salario"));
				g.setSenha(result.getString("senha"));
				e.setId_endereco(result.getInt("id_endereco"));
				e.setCidade(result.getString("cidade"));
				e.setBairro(result.getString("Bairro"));
				e.setRua(result.getString("rua"));
				e.setNumero(result.getInt("numero"));
				e.setCep(result.getString("cep"));
				g.setIdentificador(result.getString("identificador"));
				g.setEndereco(e);
				return g;
			}

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
	public Gerente logar(String senha) {
		Gerente g = null;
		Endereco e = null;
		String sqlLogar = "SELECT * from Gerente g join Endereco e on g.cod_endereco=e.id_endereco where senha = ? ; ";

		try {

			pstmt = conn.prepareStatement(sqlLogar);
			pstmt.setString(1, senha);

			result = pstmt.executeQuery();

			if (result.next()) {

				g = new Gerente();
				e = new Endereco();
				g.setId_gerente(result.getInt("id_gerente"));
				g.setNome(result.getString("nome"));
				g.setCpf(result.getString("cpf"));
				g.setDataNasc(Data.mudarDataParaLocalDate(result.getString("dataNasc")));
				g.setTelefone(result.getString("telefone"));
				g.setEmail(result.getString("email"));
				g.setSalario(result.getDouble("salario"));
				g.setSenha(result.getString("senha"));
				e.setId_endereco(result.getInt("id_endereco"));
				e.setCidade(result.getString("cidade"));
				e.setBairro(result.getString("Bairro"));
				e.setRua(result.getString("rua"));
				e.setNumero(result.getInt("numero"));
				e.setCep(result.getString("cep"));
				g.setIdentificador(result.getString("identificador"));
				g.setEndereco(e);
				return g;
			}

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
}
