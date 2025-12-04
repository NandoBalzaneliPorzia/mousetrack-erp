package com.comexapp.DTO;

import java.time.LocalDate;

/*
A classe EventoRastreioDTO.java é um DTO (Data Transfer Object) que representa 
um evento de rastreio de um processo ou envio. Contém os campos:
- key: identificador ou código do evento
- date: data do evento
- city: cidade onde o evento ocorreu
Esse DTO é usado para transferir informações do histórico de rastreio do backend 
para o frontend.
*/
public class EventoRastreioDTO {
    private String key;
    private LocalDate date;
    private String city;

    // Getters e setters
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}
