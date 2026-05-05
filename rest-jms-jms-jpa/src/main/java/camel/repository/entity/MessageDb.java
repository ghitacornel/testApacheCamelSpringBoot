package camel.repository.entity;

import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDb {

    @Id
    private Integer id;

    private String logs;

}
