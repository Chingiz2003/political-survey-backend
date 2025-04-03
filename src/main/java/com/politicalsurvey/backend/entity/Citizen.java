package com.politicalsurvey.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "citizens")
@Data
@RequiredArgsConstructor
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName; // ФИО пользователя

    @Column(name = "iin", nullable = false, unique = true, length = 12)
    private String iin; // ИИН гражданина (уникальный)

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate; // Дата рождения

    @Column(name = "place_of_birth", nullable = false, length = 255)
    private String birthPlace; // Место рождения

    @Column(name = "nationality", nullable = false, length = 50)
    private String nationality; // Гражданство (например, "Казахстан")

    // Важно! Удаляем @Lob и устанавливаем columnDefinition="bytea"
    @Column(name = "face_image", nullable = false, columnDefinition = "bytea")
    private byte[] faceImage; // Фото лица пользователя (хранится как bytea в PostgreSQL)
}