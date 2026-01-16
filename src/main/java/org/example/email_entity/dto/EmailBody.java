package org.example.email_entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailBody {

    private String recipient;
    private String body;
    private String subject;
    private String attechment;
}
