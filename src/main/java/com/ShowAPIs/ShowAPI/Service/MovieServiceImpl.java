package com.ShowAPIs.ShowAPI.Service;

import com.ShowAPIs.ShowAPI.DTO.MovieDto;
import com.ShowAPIs.ShowAPI.Models.Movie;
import com.ShowAPIs.ShowAPI.Repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        //check if same file already exists (ensuring no duplicate file added again)
        if(Files.exists((Paths.get(path+File.separator+file.getOriginalFilename())) )){
            throw  new RuntimeException("File already exists! Please enter another file name");
        };
        String uploadedFileName=fileService.uploadFile(path,file);

        //2.set the value of poster field as fileName
        movieDto.setPoster(uploadedFileName);

        //3.map DTo to movie obj this will map movie obj with the help of All args Const
        Movie movie=new Movie(
                null, //if PK value is not exists,if value is null ,if value is being given null, it will auto generate PK
                //if we will give a PK and if exists already to save method will replace the value
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );
        //4 save movie obj
        Movie savedMovie=movieRepository.save(movie);

        //5 generate the poster url we need complete URL
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

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {
       //1 check movie exists or not by given id
        Movie mv=movieRepository.findById(movieId).orElseThrow(()->new RuntimeException("movie not found"));

        //2.if exists two situations exists

        //2.1 if file is given as null so do nothing
        //2.2 if file is not null then delete existing file associated with the record
        //and upload new file
        String fileName=mv.getPoster();
        if (file != null) {

            Files.deleteIfExists(Paths.get(path+File.separator+fileName));
            fileName=fileService.uploadFile(path,file);
        }

        //3 set movieDto's poster value a/c to step2
        movieDto.setPoster(fileName);

        //4 map it to movie Obj
        Movie movie=new Movie(
                mv.getMovieId(), //if PK value is not exists,if value is null ,if value is being given null, it will auto generate PK
                //if we will give a PK and if exists already to save method will replace the value
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        //5 saved movie obj
        Movie updatedMovie=movieRepository.save(movie);

        //6 generate poster URL
        String posterUrl=baseUrl+"/file/"+fileName;

        // 7 map it to movieDto and return it
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
    public String deleteMovie(Integer movieId) throws IOException {
        //1 check if exists
        Movie mv=movieRepository.findById(movieId).orElseThrow(()->new RuntimeException("movie not found"));
        Integer id=mv.getMovieId();
        //2 delete file
        Files.deleteIfExists(Paths.get(path+File.separator+mv.getPoster()));
        //3 delete obj
        movieRepository.delete((mv));
        return "Movie deleted with id = "+id;
    }
}
