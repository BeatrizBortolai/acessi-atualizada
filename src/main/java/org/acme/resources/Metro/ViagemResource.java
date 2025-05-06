package org.acme.resources.Metro;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dtos.Metro.SearchViagemDto;
import org.acme.entities.Metro.Viagem;
import org.acme.repositories.MetroRepository.ViagemRepository;
import org.acme.services.MetroService.ViagemService;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;
import java.util.Optional;

@Path("/viagem")
public class ViagemResource {
    public static final int PAGE_SIZE = 3;
    private ViagemRepository viagemRepository = new ViagemRepository();
    private final ViagemService viagemService = new ViagemService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Viagem> getViagens() { return viagemRepository.listarTodos(); }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchViagemDto searchViagens(
            @QueryParam("page") int page,
            @QueryParam("name") Optional<String> name,
            @QueryParam("text") Optional<String> text,
            @QueryParam("direction") Optional<String> direction)
    {
        List<Viagem> viagens = viagemRepository.listarTodos();

        var filteredViagens = viagens.stream()
                .filter(e -> e.getPassageiro().getNome().contains(name.orElse("")))
                .filter(e -> e.getEstacaoDestino().getNome().contains(name.orElse("")))

                .sorted(direction.orElse("asc").equals("desc") ?
                        (c1, c2) -> c2.getPassageiro().getNome().compareToIgnoreCase(c1.getPassageiro().getNome()):
                        (c1, c2) -> c1.getEstacaoDestino().getNome().compareToIgnoreCase(c2.getEstacaoDestino().getNome())
                )
                .toList();

        if(filteredViagens.isEmpty())
            return null;

        var start = page > 1 ? (page - 1) * PAGE_SIZE : 0;
        var end = Math.min(start + PAGE_SIZE, filteredViagens.size());

        return start > filteredViagens.size() ?
                null : new SearchViagemDto(page,
                direction.orElse("asc"),
                PAGE_SIZE,
                filteredViagens.size(),
                filteredViagens.subList(start,end));

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViagem(@PathParam("id") int id){
        var viagem = viagemRepository.buscarPorId(id);

        return viagem.map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addViagem(Viagem viagem){
        if(!viagemService.validateViagem(viagem))
            return Response.status(Response.Status.BAD_REQUEST).build();

        viagemRepository.adicionar(viagem);
        return Response.status(Response.Status.CREATED)
                .entity(viagem)
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteViagem(@PathParam("id") int id){
        var viagem = viagemRepository.buscarPorId(id);

        if (viagem == null)
            return Response.status(RestResponse.Status.NOT_FOUND).build();

        viagemRepository.deleteById(id);
        return Response.noContent().build();
    }
}
