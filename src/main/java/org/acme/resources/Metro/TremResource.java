package org.acme.resources.Metro;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dtos.Metro.SearchTremDto;
import org.acme.dtos.Pessoa.SearchColaboradorDto;
import org.acme.entities.Metro.Trem;
import org.acme.entities.Pessoa.Colaborador;
import org.acme.repositories.MetroRepository.TremRepository;
import org.acme.repositories.PessoaRepository.ColaboradorRepository;
import org.acme.services.MetroService.TremService;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;
import java.util.Optional;

@Path("/trem")
public class TremResource {
    public static final int PAGE_SIZE = 3;
    private TremRepository tremRepository = new TremRepository();
    private final TremService tremService = new TremService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Trem> getTrem() { return tremRepository.listarTodos(); }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchTremDto searchTrens(
            @QueryParam("page") int page,
            @QueryParam("name") Optional<String> name,
            @QueryParam("text") Optional<String> text,
            @QueryParam("direction") Optional<String> direction)
    {
        List<Trem> trens = tremRepository.listarTodos();

        var filteredTrens = trens.stream()
                .filter(e -> e.getOrigem().contains(text.orElse("")))
                .filter(e -> e.getDestino().contains(text.orElse("")))
                .sorted(direction.orElse("asc").equals("desc") ?
                        (c1, c2) -> c2.getOrigem().compareToIgnoreCase(c1.getOrigem()):
                        (c1, c2) -> c1.getDestino().compareToIgnoreCase(c2.getDestino())
                )
                .toList();

        if(filteredTrens.isEmpty())
            return null;

        var start = page > 1 ? (page - 1) * PAGE_SIZE : 0;
        var end = Math.min(start + PAGE_SIZE, filteredTrens.size());

        return start > filteredTrens.size() ?
                null : new SearchTremDto(page,
                direction.orElse("asc"),
                PAGE_SIZE,
                filteredTrens.size(),
                filteredTrens.subList(start,end));

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrem(@PathParam("id") int id){
        var trem = tremRepository.buscarPorId(id);

        return trem.map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTrem(Trem trem){
        if(!tremService.validateTrem(trem))
            return Response.status(Response.Status.BAD_REQUEST).build();

        tremRepository.adicionar(trem);
        return Response.status(Response.Status.CREATED)
                .entity(trem)
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteColaborador(@PathParam("id") int id){
        var trem = tremRepository.buscarPorId(id);

        if (trem == null)
            return Response.status(RestResponse.Status.NOT_FOUND).build();

        tremRepository.deleteById(id);
        return Response.noContent().build();
    }
}
