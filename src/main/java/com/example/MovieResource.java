package com.example;



import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

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
    @Counted(name = "get all movies",displayName = "getAllMovie",description = "fetch  all movies")
    @Timed(name = " fetched time movie by Id ",
            displayName = "fetchTimeMovieById",
            description = "fetch time of movie by Id",
            unit = MetricUnits.MILLISECONDS )
    public Response getAll() {
        List<Movie> movieList=Movie.listAll();
        return Response.ok(movieList).build();
    }
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(name = "get movie by Id",displayName = "getById",description = "fetch  movie match Id")
    @Timed(name = " fetched time of movie by Id ",
            displayName = "fetchTimeMovieById",
            description = "fetch time of movie by Id",
    unit = MetricUnits.MILLISECONDS)
    public Response getById(@PathParam("id") Long id) {
        var movieO=Movie.findByIdOptional(id);
        return movieO.map(movie->Response.ok(movie).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());

    }

    @GET
    @Path("country/{country}")
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(name = "get movie by country",displayName = "getByCountry",description = "fetch all movie match country name")
    @Timed(name = " fetched time of movie by Country ",
            displayName = "fetchTimeMovieByCountry",
            description = "fetch time of movie by country",
            unit = MetricUnits.MILLISECONDS)
    public Response getByCountry(@PathParam("country") String country) {
        var movies=Movie.list("select u from Movie u where u.country=?1",country);
        return Response.ok(movies).build();
    }

    @GET
    @Path("title/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(name = "get movie by title",displayName = "getByTitle",description = "fetch all movie match title")
    @Timed(name = " fetched time of movie by Title ",
            displayName = "fetchTimeMovieByTitle",
            description = "fetch time of movie by title",
            unit = MetricUnits.MILLISECONDS)
    public Response getByTitle(@PathParam("title") String title) {
        var movieO=Movie.find("title",title).singleResultOptional();
        return movieO.map(movie->Response.ok(movie).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());

    }
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Counted(name = "create movie ",displayName = "insertNewMovie",
            description = "insert a new movie into db",
            unit = MetricUnits.MILLISECONDS)
    @Timed(name = " created time of movie ",displayName = "timeCreatedMovie",description = "created time of movie by Id")
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
    @Counted(name = "delete movie ",displayName = "deleteMovie",description = "delete movie by Id")
    @Timed(name = " deleted time of movie ",
            displayName = "timedeleteMovie",
            description = "delete time of movie by Id",
            unit = MetricUnits.MILLISECONDS)
    public Response delete(@PathParam("id") Long id){
       if (Movie.deleteById(id)){
           return Response.noContent().build();
       }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}