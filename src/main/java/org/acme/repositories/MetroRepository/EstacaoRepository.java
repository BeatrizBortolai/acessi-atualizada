package org.acme.repositories.MetroRepository;

import org.acme.entities.Metro.Estacao;
import org.acme.infrastructure.DatabaseConfig;
import org.acme.repositories.CrudRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EstacaoRepository implements CrudRepository<Estacao> {
    public static Logger logger = LogManager.getLogger(EstacaoRepository.class);
    private List<Estacao> estacoes = new ArrayList<>(List.of());

    @Override
    public void adicionar(Estacao object) {
        var query = "INSERT INTO ESTACAOACESSI (DELETED, NOME, HORARIOABERTURA, HORARIOFECHAMENTO, LOCALIZACAO) VALUES (?,?,?,?,?)";
        try(var conn = DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setBoolean(1, false);
            stmt.setString(2, object.getNome());
            stmt.setString(3, object.getHorarioAbertura());
            stmt.setString(4, object.getHorarioFechamento());
            stmt.setString(5, object.getLocalizacao());
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Estação adicionada com sucesso!");
        }
        catch(SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public void atualizar(int id, Estacao object) {
        for (Estacao e: estacoes)
            if (e.getId() == id)
                e = object;
    }

    @Override
    public void remover(Estacao object) {
        estacoes.remove(object);
    }

    @Override
    public void remover(int id) {
        estacoes.removeIf(e -> e.getId() == id);
    }

    @Override
    public void delete(Estacao object) {
        object.setDeleted(true);
    }

    @Override
    public void deleteById(int id) {
        var query = "UPDATE ESTACAOACESSI SET DELETED = 1 WHERE ID = ?";
        try(var conn =  DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Estação removida com sucesso!");
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public List<Estacao> listarTodos() {
        var estacoesDb = new ArrayList<Estacao>();
        var query = "SELECT * FROM ESTACAOACESSI";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var estacao = new Estacao();
                estacao.setId(result.getInt("id"));
                estacao.setDeleted(result.getBoolean("deleted"));
                estacao.setNome(result.getString("nome"));
                estacao.setHorarioAbertura(result.getString("horarioAbertura"));
                estacao.setHorarioFechamento(result.getString("horarioFechamento"));
                estacao.setLocalizacao(result.getString("localizacao"));
                estacoesDb.add(estacao);
            }
            connection.close();
            return estacoesDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public List<Estacao> listar() {
        var estacoesDb = new ArrayList<Estacao>();
        var query = "SELECT * FROM ESTACAOACESSI WHERE DELETED = 0";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var estacao = new Estacao();
                estacao.setId(result.getInt("id"));
                estacao.setDeleted(result.getBoolean("deleted"));
                estacao.setNome(result.getString("nome"));
                estacao.setHorarioAbertura(result.getString("horarioAbertura"));
                estacao.setHorarioFechamento(result.getString("horarioFechamento"));
                estacao.setLocalizacao(result.getString("localizacao"));
                estacoesDb.add(estacao);
            }
            return estacoesDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public Optional<Estacao> buscarPorId(int id) {
        var query = "SELECT * from ESTACAOACESSI WHERE ID = ?";
        try(var connection = DatabaseConfig.getConnection()){
            var stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeQuery();
            if(result.next()){
                var estacao = new Estacao();
                estacao.setId(result.getInt("id"));
                estacao.setDeleted(result.getBoolean("deleted"));
                estacao.setNome(result.getString("nome"));
                estacao.setHorarioAbertura(result.getString("horarioAbertura"));
                estacao.setHorarioFechamento(result.getString("horarioFechamento"));
                estacao.setLocalizacao(result.getString("localizacao"));
                return Optional.of(estacao);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return Optional.empty();
    }
}
