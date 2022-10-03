package com.example;



import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;


@Path("/movies")
public class MovieResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Movie> movieList=Movie.listAll();
        return Response.ok(movieList).build();
    }
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        var movieO=Movie.findByIdOptional(id);
        return movieO.map(movie->Response.ok(movie).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());

    }

    @GET
    @Path("country/{country}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByCountry(@PathParam("country") String country) {
        var movies=Movie.list("select u from Movie u where u.country=?1",country);
        return Response.ok(movies).build();
    }

    @GET
    @Path("title/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByTitle(@PathParam("title") String title) {
        var movieO=Movie.find("title",title).singleResultOptional();
        return movieO.map(movie->Response.ok(movie).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());

    }
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Movie movie) {
       Movie.persist(movie);
       if (movie.isPersistent()){
           return Response.created(URI.create("/movies"+movie.id)).build();
       }else {
           return Response.status(Response.Status.BAD_REQUEST).build();
       }
    }
    @DELETE
    @Transactional
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id){
       if (Movie.deleteById(id)){
           return Response.noContent().build();
       }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}