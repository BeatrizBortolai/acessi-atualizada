package org.acme.repositories.ProblemaRepository;

import org.acme.entities.Pessoa.Usuario;
import org.acme.entities.Problema.Conversa;
import org.acme.infrastructure.DatabaseConfig;
import org.acme.repositories.CrudRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConversaRepository implements CrudRepository<Conversa> {
    public static Logger logger = LogManager.getLogger(ConversaRepository.class);
    private List<Conversa> conversas = new ArrayList<>(List.of());

    @Override
    public void adicionar(Conversa object) {
        var query = "INSERT INTO CONVERSAACESSI (DELETED, USUARIO, ASSUNTO, MODELOLLM, MENSAGEM, ENCERRADA) VALUES (?,?,?,?,?,?)";
        try(var conn = DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setBoolean(1, false);
            stmt.setObject(2, object.getUsuario());
            stmt.setString(3, object.getAssunto());
            stmt.setString(4, object.getModeloLLM());
            stmt.setString(5, object.getMensagem());
            stmt.setBoolean(6, false);
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Conversa adicionada com sucesso!");
        }
        catch(SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public void atualizar(int id, Conversa object) {
        for (Conversa c: conversas)
            if (c.getId() == id)
                c = object;
    }

    @Override
    public void remover(Conversa object) {
        conversas.remove(object);
    }

    @Override
    public void remover(int id) {
        conversas.removeIf(c -> c.getId() == id);
    }

    @Override
    public void delete(Conversa object) {
        object.setDeleted(true);
    }

    @Override
    public void deleteById(int id) {
        var query = "UPDATE CONVERSAACESSI SET DELETED = 1 WHERE ID = ?";
        try(var conn =  DatabaseConfig.getConnection())
        {
            var stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeUpdate();
            if(result > 0)
                logger.info("Conversa removida com sucesso!");
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public List<Conversa> listarTodos() {
        var conversasDb = new ArrayList<Conversa>();
        var query = "SELECT * FROM CONVERSAACESSI";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var conversa = new Conversa();
                conversa.setId(result.getInt("id"));
                conversa.setDeleted(result.getBoolean("deleted"));
                Usuario usuario = new Usuario();
                usuario.setId(result.getInt("usuario"));
                conversa.setUsuario(usuario);
                conversa.setAssunto(result.getString("assunto"));
                conversa.setModeloLLM(result.getString("modeloLLM"));
                conversa.setMensagem(result.getString("mensagem"));
                conversa.setEncerrada(result.getBoolean("encerrada"));
                conversasDb.add(conversa);
            }
            return conversasDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public List<Conversa> listar() {
        var conversasDb = new ArrayList<Conversa>();
        var query = "SELECT * FROM CONVERSAACESSI WHERE DELETED = 0";
        try (var connection = DatabaseConfig.getConnection())
        {
            var statement = connection.prepareStatement(query);
            var result = statement.executeQuery();
            while (result.next())
            {
                var conversa = new Conversa();
                conversa.setId(result.getInt("id"));
                conversa.setDeleted(result.getBoolean("deleted"));
                Usuario usuario = new Usuario();
                usuario.setId(result.getInt("usuario"));
                conversa.setUsuario(usuario);
                conversa.setAssunto(result.getString("assunto"));
                conversa.setModeloLLM(result.getString("modeloLLM"));
                conversa.setMensagem(result.getString("mensagem"));
                conversa.setEncerrada(result.getBoolean("encerrada"));

            }
            return conversasDb;
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    @Override
    public Optional<Conversa> buscarPorId(int id) {

        var query = "SELECT * from CONVERSAACESSI WHERE ID = ?";
        try(var connection = DatabaseConfig.getConnection()){
            var stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            var result = stmt.executeQuery();
            if(result.next()){
                var conversa = new Conversa();
                conversa.setId(result.getInt("id"));
                conversa.setDeleted(result.getBoolean("deleted"));
                Usuario usuario = new Usuario();
                usuario.setId(result.getInt("usuario"));
                conversa.setUsuario(usuario);
                conversa.setAssunto(result.getString("assunto"));
                conversa.setModeloLLM(result.getString("modeloLLM"));
                conversa.setMensagem(result.getString("mensagem"));
                conversa.setEncerrada(result.getBoolean("encerrada"));
                return Optional.of(conversa);
            }        }

        catch(SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        return Optional.empty();
    }

}
