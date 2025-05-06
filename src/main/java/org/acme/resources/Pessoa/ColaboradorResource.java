package org.acme.resources.Pessoa;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dtos.Pessoa.SearchColaboradorDto;
import org.acme.dtos.Pessoa.SearchUsuarioDto;
import org.acme.entities.Pessoa.Colaborador;
import org.acme.entities.Pessoa.Usuario;
import org.acme.repositories.PessoaRepository.ColaboradorRepository;
import org.acme.repositories.PessoaRepository.UsuarioRepository;
import org.acme.services.PessoaService.ColaboradorService;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Path("/colaborador")
public class ColaboradorResource {
    public static final int PAGE_SIZE = 3;
    private ColaboradorRepository colaboradorRepository = new ColaboradorRepository();
    private final ColaboradorService colaboradorService = new ColaboradorService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Colaborador> getColaboradores() { return colaboradorRepository.listarTodos(); }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchColaboradorDto searchColaboradores(
            @QueryParam("page") int page,
            @QueryParam("name") Optional<String> name,
            @QueryParam("text") Optional<String> text,
            @QueryParam("direction") Optional<String> direction)
    {
        List<Colaborador> colaboradores = colaboradorRepository.listarTodos();

        var filteredColaboradores = colaboradores.stream()
                .filter(e -> e.getNome().contains(name.orElse("")))
                .filter(e -> e.getEmail().contains(text.orElse("")))
                .filter(e -> e.getSenha().contains(text.orElse("")))
                .sorted(direction.orElse("asc").equals("desc") ?
                        (c1, c2) -> c2.getNome().compareToIgnoreCase(c1.getNome()):
                        (c1, c2) -> c1.getEmail().compareToIgnoreCase(c2.getEmail())
                )
                .toList();

        if(filteredColaboradores.isEmpty())
            return null;

        var start = page > 1 ? (page - 1) * PAGE_SIZE : 0;
        var end = Math.min(start + PAGE_SIZE, filteredColaboradores.size());

        return start > filteredColaboradores.size() ?
                null : new SearchColaboradorDto(page,
                direction.orElse("asc"),
                PAGE_SIZE,
                filteredColaboradores.size(),
                filteredColaboradores.subList(start,end));

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getColaborador(@PathParam("id") int id){
        var colaborador = colaboradorRepository.buscarPorId(id);

        return colaborador.map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addColaborador(Colaborador colaborador){
        if(!colaboradorService.validateColaborador(colaborador))
            return Response.status(Response.Status.BAD_REQUEST).build();

        colaboradorRepository.adicionar(colaborador);
        return Response.status(Response.Status.CREATED)
                .entity(colaborador)
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteColaborador(@PathParam("id") int id){
        var colaborador = colaboradorRepository.buscarPorId(id);

        if (colaborador == null)
            return Response.status(RestResponse.Status.NOT_FOUND).build();

        colaboradorRepository.deleteById(id);
        return Response.noContent().build();
    }
}
