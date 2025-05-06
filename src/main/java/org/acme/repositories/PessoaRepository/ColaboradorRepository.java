package org.acme.repositories.PessoaRepository;

import org.acme.entities.Pessoa.Colaborador;
import org.acme.infrastructure.DatabaseConfig;
import org.acme.repositories.CrudRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ColaboradorRepository implements CrudRepository<Colaborador> {
    public static Logger logger = LogManager.getLogger(ColaboradorRepository.class);
    private List<Colaborador> colaboradores = new ArrayList<>(List.of());

    @Override
    public void adicionar(Colaborador object) {
        var query = "INSERT INTO COLABORADORACESSI (DELETED, NOME, EMAIL, SENHA, CARGO) VALUES (?,?,?,?,?)";
        try(var conn = DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setBoolean(1, false);
            stmt.setString(2, object.getNome());
            stmt.setString(3, object.getEmail());
            stmt.setString(4, object.getSenha());
            stmt.setString(5, object.getCargo());
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Colaborador adicionado com sucesso!");
        }
        catch(SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public void atualizar(int id, Colaborador object) {
        for (Colaborador c: colaboradores)
            if (c.getId() == id)
                c = object;
    }

    @Override
    public void remover(Colaborador object) {
        colaboradores.remove(object);
    }

    @Override
    public void remover(int id) {
        colaboradores.removeIf(c -> c.getId() == id);
    }

    @Override
    public void delete(Colaborador object) {
        object.setDeleted(true);
    }

    @Override
    public void deleteById(int id) {
        var query = "UPDATE COLABORADORACESSI SET DELETED = 1 WHERE ID = ?";
        try(var conn =  DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Colaborador removido com sucesso!");
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public List<Colaborador> listarTodos() {
        var colaboradoresDb = new ArrayList<Colaborador>();
        var query = "SELECT * FROM COLABORADORACESSI";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var colaborador = new Colaborador();
                colaborador.setId(result.getInt("id"));
                colaborador.setDeleted(result.getBoolean("deleted"));
                colaborador.setNome(result.getString("nome"));
                colaborador.setEmail(result.getString("email"));
                colaborador.setSenha(result.getString("senha"));
                colaborador.setCargo(result.getString("cargo"));
                colaboradoresDb.add(colaborador);
            }
            connection.close();
            return colaboradoresDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public List<Colaborador> listar() {
            var colaboradoresDb = new ArrayList<Colaborador>();
        var query = "SELECT * FROM COLABORADORACESSI WHERE DELETED = 0";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var colaborador = new Colaborador();
                colaborador.setId(result.getInt("id"));
                colaborador.setDeleted(result.getBoolean("deleted"));
                colaborador.setNome(result.getString("nome"));
                colaborador.setEmail(result.getString("email"));
                colaborador.setSenha(result.getString("senha"));
                colaborador.setCargo(result.getString("cargo"));
                colaboradoresDb.add(colaborador);
            }
            return colaboradoresDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public Optional<Colaborador> buscarPorId(int id) {
        var query = "SELECT * from COLABORADORACESSI WHERE ID = ?";
        try(var connection = DatabaseConfig.getConnection()){
            var stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeQuery();
            if(result.next()){
                var colaborador = new Colaborador();
                colaborador.setId(result.getInt("id"));
                colaborador.setDeleted(result.getBoolean("deleted"));
                colaborador.setNome(result.getString("nome"));
                colaborador.setEmail(result.getString("email"));
                colaborador.setSenha(result.getString("senha"));
                colaborador.setCargo(result.getString("cargo"));

                return Optional.of(colaborador);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return Optional.empty();
    }
}
