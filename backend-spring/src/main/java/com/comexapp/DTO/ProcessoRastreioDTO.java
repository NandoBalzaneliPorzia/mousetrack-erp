package com.comexapp.DTO;

import java.util.List;

/*
Responsável: Eduardo Sanvido
A classe ProcessoRastreioDTO.java é um DTO (Data Transfer Object) que representa 
as informações de rastreio de um processo de embarque ou transporte. 
Inclui:
- codigo: identificador do processo
- tipo: modalidade ou tipo de transporte
- status: status atual do processo
- historico: lista de eventos do rastreio (objetos EventoRastreioDTO), 
  contendo datas e cidades de movimentação
*/
public class ProcessoRastreioDTO {
    private String codigo;
    private String tipo;
    private String status;
    private List<EventoRastreioDTO> historico;

    // Getters e setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<EventoRastreioDTO> getHistorico() { return historico; }
    public void setHistorico(List<EventoRastreioDTO> historico) { this.historico = historico; }
}
