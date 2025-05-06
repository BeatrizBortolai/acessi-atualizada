package org.acme.repositories.PessoaRepository;

import org.acme.entities.Pessoa.Passageiro;
import org.acme.infrastructure.DatabaseConfig;
import org.acme.repositories.CrudRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PassageiroRepository implements CrudRepository<Passageiro> {
    public static Logger logger = LogManager.getLogger(PassageiroRepository.class);
    private List<Passageiro> passageiros = new ArrayList<>(List.of());

    @Override
    public void adicionar(Passageiro object) {
        var query = "INSERT INTO PASSAGEIROACESSI (DELETED, NOME, EMAIL, SENHA) VALUES (?,?,?,?)";
        try(var conn = DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setBoolean(1, false);
            stmt.setString(2, object.getNome());
            stmt.setString(3, object.getEmail());
            stmt.setString(4, object.getSenha());
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Passageiro adicionado com sucesso!");
        }
        catch(SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public void atualizar(int id, Passageiro object) {
        for (Passageiro p: passageiros)
            if (p.getId() == id)
                p = object;
    }

    @Override
    public void remover(Passageiro object) {
        passageiros.remove(object);
    }

    @Override
    public void remover(int id) {
        passageiros.removeIf(p -> p.getId() == id);
    }

    @Override
    public void delete(Passageiro object) {
        object.setDeleted(true);
    }

    @Override
    public void deleteById(int id) {
        var query = "UPDATE PASSAGEIROACESSI SET DELETED = 1 WHERE ID = ?";
        try(var conn =  DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Passageiro removido com sucesso!");
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public List<Passageiro> listarTodos() {
        var passageirosDb = new ArrayList<Passageiro>();
        var query = "SELECT * FROM PASSAGEIROACESSI";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var passageiro = new Passageiro();
                passageiro.setId(result.getInt("id"));
                passageiro.setDeleted(result.getBoolean("deleted"));
                passageiro.setNome(result.getString("nome"));
                passageiro.setEmail(result.getString("email"));
                passageiro.setSenha(result.getString("senha"));
                passageirosDb.add(passageiro);
            }
            return passageirosDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public List<Passageiro> listar() {
        var passageirosDb = new ArrayList<Passageiro>();
        var query = "SELECT * FROM PASSAGEIROACESSI WHERE DELETED = 0";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var passageiro = new Passageiro();
                passageiro.setId(result.getInt("id"));
                passageiro.setDeleted(result.getBoolean("deleted"));
                passageiro.setNome(result.getString("nome"));
                passageiro.setEmail(result.getString("email"));
                passageiro.setSenha(result.getString("senha"));
                passageirosDb.add(passageiro);
            }
            return passageirosDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public Optional<Passageiro> buscarPorId(int id) {
        var query = "SELECT * from PASSAGEIROACESSI WHERE ID = ?";
        try(var connection = DatabaseConfig.getConnection()){
            var stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeQuery();
            if(result.next()){
                var passageiro = new Passageiro();
                passageiro.setId(result.getInt("id"));
                passageiro.setDeleted(result.getBoolean("deleted"));
                passageiro.setNome(result.getString("nome"));
                passageiro.setEmail(result.getString("email"));
                passageiro.setSenha(result.getString("senha"));

                return Optional.of(passageiro);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return Optional.empty();
    }
}
