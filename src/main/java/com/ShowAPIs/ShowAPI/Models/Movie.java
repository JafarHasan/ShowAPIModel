package com.ShowAPIs.ShowAPI.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CollectionId;

import java.util.Set;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Movie {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer movieId;

    @Column(nullable = false)//cant be null
    //These are validations @NotBlank
    @NotBlank(message = "please provide movie's title") //from validation dependency
    private String title;

    @Column(nullable = false)//cant be null
    @NotBlank(message = "please provide movie's director") //from validation dependency
    private String director;

    @Column(nullable = false)//cant be null
    @NotBlank(message = "please provide movie's studio!") //from validation dependency
    private String studio;

    @ElementCollection
    @CollectionTable(name="movie_cast")
    private Set<String> movieCast;

    @Column(nullable = false)//cant be null
    @NotBlank(message = "please provide movie's release Year") //from validation dependency
    private Integer releaseYear;

    @NotBlank(message = "please provide movie's Poster") //from validation dependen
    private String poster;///to save image but in DB we will store only image name
}
