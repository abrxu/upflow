package com.abrxu.upflow.models.feedback;

import com.abrxu.upflow.models.department.Department;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tbl_feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 255)
    @NotNull(message = "You need to fill the input with a message.")
    @NotBlank(message = "You need to fill the input with a message.")
    @Column(name = "txt_message")
    private String message;

    @NotNull(message = "It occurred an error during the feedback creation. Please try again.")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "id_department")
    private Department department;

    @Enumerated(EnumType.STRING)
    private FeedbackType type;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
