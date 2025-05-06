package org.acme.repositories;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    void adicionar(T object);
    void atualizar(int id, T object);
    void remover(T object);
    void remover(int id);

    void delete(T object);
    void deleteById(int id);

    List<T> listarTodos();
    List<T> listar();
    Optional<T> buscarPorId(int id);
}
