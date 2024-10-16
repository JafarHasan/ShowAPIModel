package com.ShowAPIs.ShowAPI.Controllers;

import com.ShowAPIs.ShowAPI.DTO.MovieDto;
import com.ShowAPIs.ShowAPI.Service.MovieService;
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


}
