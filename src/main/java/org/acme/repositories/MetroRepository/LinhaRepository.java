package org.acme.repositories.MetroRepository;

import org.acme.entities.Metro.Estacao;
import org.acme.entities.Metro.Linha;
import org.acme.infrastructure.DatabaseConfig;
import org.acme.repositories.CrudRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LinhaRepository implements CrudRepository<Linha> {
    public static Logger logger = LogManager.getLogger(LinhaRepository.class);
    private List<Linha> linhas = new ArrayList<>(List.of());

    @Override
    public void adicionar(Linha object) {
        var query = "INSERT INTO LINHAACESSI (DELETED, NOME, ESTACOES) VALUES (?,?,?)";
        try(var conn = DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setBoolean(1, false);
            stmt.setString(2, object.getNome());
            stmt.setObject(3, object.getEstacoes());
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Linha adicionada com sucesso!");
        }
        catch(SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public void atualizar(int id, Linha object) {
        for (Linha l: linhas)
            if (l.getId() == id)
                l = object;
    }

    @Override
    public void remover(Linha object) {
        linhas.remove(object);
    }

    @Override
    public void remover(int id) {
        linhas.removeIf(l -> l.getId() == id);
    }

    @Override
    public void delete(Linha object) {
        object.setDeleted(true);
    }

    @Override
    public void deleteById(int id) {
        var query = "UPDATE LINHAACESSI SET DELETED = 1 WHERE ID = ?";
        try(var conn =  DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Linha removida com sucesso!");
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public List<Linha> listarTodos() {
        var linhasDb = new ArrayList<Linha>();
        var query = "SELECT * FROM LINHAACESSI";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var linha = new Linha();
                linha.setId(result.getInt("id"));
                linha.setDeleted(result.getBoolean("deleted"));
                linha.setNome(result.getString("nome"));
                String estacoesString = result.getString("estacao");
                List<Estacao> estacoes = Arrays.stream(estacoesString.split(","))
                        .map(nome -> {
                            Estacao estacao = new Estacao();
                            estacao.setNome(nome.trim());
                            return estacao;
                        })
                        .collect(Collectors.toList());
                linha.setEstacoes(estacoes);
                linhasDb.add(linha);
            }
            connection.close();
            return linhasDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public List<Linha> listar() {

        var linhasDb = new ArrayList<Linha>();
        var query = "SELECT * FROM LINHAACESSI WHERE DELETED = 0";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var linha = new Linha();
                linha.setId(result.getInt("id"));
                linha.setDeleted(result.getBoolean("deleted"));
                linha.setNome(result.getString("nome"));
                String estacoesString = result.getString("estacao");
                List<Estacao> estacoes = Arrays.stream(estacoesString.split(","))
                        .map(nome -> {
                            Estacao estacao = new Estacao();
                            estacao.setNome(nome.trim());
                            return estacao;
                        })
                        .collect(Collectors.toList());
                linha.setEstacoes(estacoes);
                linhasDb.add(linha);
            }
            return linhasDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public Optional<Linha> buscarPorId(int id) {
        var query = "SELECT * from LINHAACESSI WHERE ID = ?";
        try(var connection = DatabaseConfig.getConnection()){
            var stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeQuery();
            if(result.next()){
                var linha = new Linha();
                linha.setId(result.getInt("id"));
                linha.setDeleted(result.getBoolean("deleted"));
                linha.setNome(result.getString("nome"));
                String estacoesString = result.getString("estacao");
                List<Estacao> estacoes = Arrays.stream(estacoesString.split(","))
                        .map(nome -> {
                            Estacao estacao = new Estacao();
                            estacao.setNome(nome.trim());
                            return estacao;
                        })
                        .collect(Collectors.toList());
                linha.setEstacoes(estacoes);
                return Optional.of(linha);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return Optional.empty();
    }
}
