package org.acme.resources.Pessoa;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dtos.Pessoa.SearchPassageiroDto;
import org.acme.entities.Pessoa.Passageiro;
import org.acme.repositories.PessoaRepository.PassageiroRepository;
import org.acme.services.PessoaService.PassageiroService;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;
import java.util.Optional;

@Path("/passageiro")
public class PassageiroResource {
    public static final int PAGE_SIZE = 3;
    private PassageiroRepository passageiroRepository = new PassageiroRepository();
    private final PassageiroService passageiroService = new PassageiroService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Passageiro> getPassageiros() { return passageiroRepository.listarTodos(); }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchPassageiroDto searchPassageiros(
            @QueryParam("page") int page,
            @QueryParam("name") Optional<String> name,
            @QueryParam("text") Optional<String> text,
            @QueryParam("direction") Optional<String> direction)
    {
        List<Passageiro> passageiros = passageiroRepository.listarTodos();

        var filteredPassageiros = passageiros.stream()
                .filter(e -> e.getNome().contains(name.orElse("")))
                .filter(e -> e.getEmail().contains(text.orElse("")))
                .filter(e -> e.getSenha().contains(text.orElse("")))
                .sorted(direction.orElse("asc").equals("desc") ?
                        (c1, c2) -> c2.getNome().compareToIgnoreCase(c1.getNome()):
                        (c1, c2) -> c1.getEmail().compareToIgnoreCase(c2.getEmail())
                )
                .toList();

        if(filteredPassageiros.isEmpty())
            return null;

        var start = page > 1 ? (page - 1) * PAGE_SIZE : 0;
        var end = Math.min(start + PAGE_SIZE, filteredPassageiros.size());

        return start > filteredPassageiros.size() ?
                null : new SearchPassageiroDto(page,
                direction.orElse("asc"),
                PAGE_SIZE,
                filteredPassageiros.size(),
                filteredPassageiros.subList(start,end));

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPassageiro(@PathParam("id") int id){
        var passageiro = passageiroRepository.buscarPorId(id);

        return passageiro.map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPassageiro(Passageiro passageiro){
        if(!passageiroService.validatePassageiro(passageiro))
            return Response.status(Response.Status.BAD_REQUEST).build();

        passageiroRepository.adicionar(passageiro);
        return Response.status(Response.Status.CREATED)
                .entity(passageiro)
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePassageiro(@PathParam("id") int id){
        var passageiro = passageiroRepository.buscarPorId(id);

        if (passageiro == null)
            return Response.status(RestResponse.Status.NOT_FOUND).build();

        passageiroRepository.deleteById(id);
        return Response.noContent().build();
    }
}
