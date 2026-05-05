package camel.route.repository.entity;

import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Person {

    @Id
    private Integer id;

    @NotBlank
    private String name;

    @NotNull
    @Min(10)
    @Builder.Default
    private int age = 10;

}
