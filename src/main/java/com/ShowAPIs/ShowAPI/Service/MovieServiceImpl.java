package com.ShowAPIs.ShowAPI.Service;

import com.ShowAPIs.ShowAPI.DTO.MovieDto;
import com.ShowAPIs.ShowAPI.Models.Movie;
import com.ShowAPIs.ShowAPI.Repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class MovieServiceImpl implements MovieService{

    private  final MovieRepository movieRepository;
    private final FileService fileService;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    //constructor for constructor Injection
    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService=fileService;
    }

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException{
        //handling file
        //1. to upload the file
        String uploadedFileName=fileService.uploadFile(path,file);

        //2.set the value of poster field as fileName
        movieDto.setPoster(uploadedFileName);

        //3.map DTo to movie obj this will map movie obj with the help of All args Const
        Movie movie=new Movie(
                movieDto.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );
        //4 save movie obj
        Movie savedMovie=movieRepository.save(movie);

        //5 generate the poster url -we need complete URL
        String posterUrl=baseUrl+"/file/"+uploadedFileName;

        //map movie obj to DTO obj using allArgsConstructor
        MovieDto response=new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );
        return response;
    }

    @Override
    public MovieDto getMovie(Integer movieId) {
        //1 check movie exists or not in DB if yes fetch data from given movieId
        Movie movie=movieRepository.findById(movieId).orElseThrow(()->new RuntimeException("movie not found"));

        //2 generate poster URL
        String posterUrl=baseUrl+"/file/"+movie.getPoster();

        //3 map to movieDto obj and return
        MovieDto response=new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );

        return response;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        List<MovieDto> movieDtos=new ArrayList<>();
        //fetch all data from DB
        List<Movie> movies=movieRepository.findAll();

        // iterate list and generate poster url of each movie obj ans map to movieDto obj
        for(Movie movie:movies){
            //generate poster URL
            String posterUrl=baseUrl+"/file/"+movie.getPoster();

            MovieDto movieDto=new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);
        }
        return movieDtos;
    }
}
