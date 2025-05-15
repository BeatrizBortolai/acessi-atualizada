package org.acme.repositories.ProblemaRepository;

import org.acme.entities.Problema.Notificacao;
import org.acme.infrastructure.DatabaseConfig;
import org.acme.repositories.CrudRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NotificacaoRepository implements CrudRepository<Notificacao> {
    public static Logger logger = LogManager.getLogger(NotificacaoRepository.class);
    private List<Notificacao> notificacoes = new ArrayList<>(List.of());

    @Override
    public void adicionar(Notificacao object) {
        var query = "INSERT INTO NOTIFICACAOACESSI (DELETED, TITULO, CONTEUDO, LINHA, TIPOOCORRENCIA) VALUES (?,?,?,?,?)";
        try(var conn = DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setBoolean(1, false);
            stmt.setString(2, object.getTitulo());
            stmt.setString(3, object.getConteudo());
            stmt.setString(4, object.getLinha());
            stmt.setString(5, object.getTipoOcorrencia());
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Notificação adicionada com sucesso!");
        }
        catch(SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public void atualizar(int id, Notificacao notificacao) {
        var query = "UPDATE NOTIFICACAOACESSI SET titulo = ?, conteudo = ?, linha = ?, tipoOcorrencia = ? WHERE id = ?";

        try (var conn = DatabaseConfig.getConnection()) {
            var stmt = conn.prepareStatement(query);
            // Definindo os parâmetros para o PreparedStatement
            stmt.setString(1, notificacao.getTitulo());
            stmt.setString(2, notificacao.getConteudo());
            stmt.setString(3, notificacao.getLinha());
            stmt.setString(4, notificacao.getTipoOcorrencia());
            stmt.setInt(5, id);

            // Executando o update e verificando quantas linhas foram afetadas
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Notificação atualizada com sucesso!");
            } else {
                System.out.println("Nenhuma notificação com o ID fornecido.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remover(Notificacao object) {
        notificacoes.remove(object);
    }

    @Override
    public void remover(int id) {
        notificacoes.removeIf(n -> n.getId() == id);
    }

    @Override
    public void delete(Notificacao object) {
        object.setDeleted(true);
    }

    @Override
    public void deleteById(int id) {
        var query = "UPDATE NOTIFICACAOACESSI SET DELETED = 1 WHERE ID = ?";
        try(var conn =  DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Notificação removida com sucesso!");
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public List<Notificacao> listarTodos() {
        var notificacoesDb = new ArrayList<Notificacao>();
        var query = "SELECT * FROM NOTIFICACAOACESSI";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var notificacao = new Notificacao();
                notificacao.setId(result.getInt("id"));
                notificacao.setDeleted(result.getBoolean("deleted"));
                notificacao.setTitulo(result.getString("titulo"));
                notificacao.setConteudo(result.getString("conteudo"));
                notificacao.setLinha(result.getString("linha"));
                notificacao.setTipoOcorrencia(result.getString("tipoOcorrencia"));
                notificacoesDb.add(notificacao);
            }
            connection.close();
            return notificacoesDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public List<Notificacao> listar() {
        var notificacoesDb = new ArrayList<Notificacao>();
        var query = "SELECT * FROM NOTIFICACAOACESSI WHERE DELETED = 0";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var notificacao = new Notificacao();
                notificacao.setId(result.getInt("id"));
                notificacao.setDeleted(result.getBoolean("deleted"));
                notificacao.setTitulo(result.getString("titulo"));
                notificacao.setConteudo(result.getString("conteudo"));
                notificacao.setLinha(result.getString("linha"));
                notificacao.setTipoOcorrencia(result.getString("tipoOcorrencia"));
                notificacoesDb.add(notificacao);
            }
            return notificacoesDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public Optional<Notificacao> buscarPorId(int id) {
        var query = "SELECT * from NOTIFICACAOACESSI WHERE ID = ?";
        try(var connection = DatabaseConfig.getConnection()){
            var stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeQuery();
            if(result.next()){
                var notificacao = new Notificacao();
                notificacao.setId(result.getInt("id"));
                notificacao.setDeleted(result.getBoolean("deleted"));
                notificacao.setTitulo(result.getString("titulo"));
                notificacao.setConteudo(result.getString("conteudo"));
                notificacao.setLinha(result.getString("linha"));
                notificacao.setTipoOcorrencia(result.getString("tipoOcorrencia"));
                return Optional.of(notificacao);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return Optional.empty();
    }

}
