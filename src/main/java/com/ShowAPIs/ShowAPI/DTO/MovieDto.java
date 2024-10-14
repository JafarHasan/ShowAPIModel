package com.ShowAPIs.ShowAPI.DTO;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {


    //These are validations using @NotBlank
    @NotBlank(message = "please provide movie's title") //from validation dependency
    private String title;

    @NotBlank(message = "please provide movie's director") //from validation dependency
    private String director;

    @NotBlank(message = "please provide movie's studio!") //from validation dependency
    private String studio;

    private Set<String> movieCast;

    private Integer releaseYear;

    @NotBlank(message = "please provide movie's Poster") //from validation dependency
    private String poster;///to save image but in DB we will store only image name

    @NotBlank(message = "please provide movie's poster URL") //from validation dependency
    private String posterUrl;
}
