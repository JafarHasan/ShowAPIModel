package com.ShowAPIs.ShowAPI.Controllers;

import com.ShowAPIs.ShowAPI.DTO.MovieDto;
import com.ShowAPIs.ShowAPI.DTO.MoviePageResponse;
import com.ShowAPIs.ShowAPI.Exceptions.EmptyFileException;
import com.ShowAPIs.ShowAPI.Service.MovieService;
import com.ShowAPIs.ShowAPI.utils.AppConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {
    private final MovieService movieService;
@Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovie(@RequestPart MultipartFile file,
                                             @RequestPart String movieDto) throws IOException {
        if(file.isEmpty()){
            throw new EmptyFileException("File is Empty! Please send another file");
        }
        //using Request part we cant pass movieDto obj bcz in postman (body-> rowData) there are only two options
        //text and file so we are passing movieDto as text(string)
        MovieDto dto=convertToMovieDto(movieDto);
        return new ResponseEntity<>(movieService.addMovie(dto,file), HttpStatus.CREATED);

    }
    private  MovieDto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        MovieDto movieDto=objectMapper.readValue(movieDtoObj,MovieDto.class);
        return movieDto;
    }
    @GetMapping("/{movieId}")
    public  ResponseEntity<MovieDto> getMovie(@PathVariable Integer movieId){
        return ResponseEntity.ok(movieService.getMovie(movieId));
    }
    @GetMapping("/all")
    public ResponseEntity<List<MovieDto>> getAllMovies(){
        return  ResponseEntity.ok(movieService.getAllMovies());
    }

    @PutMapping("/update/{movieId}")
    public  ResponseEntity<MovieDto> updateMovie(@PathVariable Integer movieId,
                                                 @RequestPart MultipartFile file,
                                                 @RequestPart String movieDto) throws IOException{
    if(file.isEmpty()) file=null;
        //convert movieObj to String
        MovieDto movieDto1=convertToMovieDto(movieDto);
        return ResponseEntity.ok(movieService.updateMovie(movieId,movieDto1,file));
    }

    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> deleteMovie(@PathVariable Integer movieId) throws IOException{
    return ResponseEntity.ok(movieService.deleteMovie(movieId));
    }

    @GetMapping("/addMoviePage")
    public ResponseEntity<MoviePageResponse> getMoviesWithPagination(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER)Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE ,required = false)Integer pageSize

    ){
        return ResponseEntity.ok(movieService.getAllMoviesWithPagination(pageNumber,pageSize));
    }

    @GetMapping("/addMoviePageSort")
    public ResponseEntity<MoviePageResponse> getMoviesWithPaginationAndSorting(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER)Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE ,required = false)Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR,required = false)String dir

    ){
        return ResponseEntity.ok(movieService.getAllMoviesWithPaginationAndSorting(pageNumber,pageSize,sortBy,dir));
    }


}
