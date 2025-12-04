package com.comexapp.DTO;

/*
A classe ShippingInstructionDTO.java é um DTO (Data Transfer Object) usado
para transportar informações de uma Instrução de Embarque (Shipping Instruction)
entre camadas da aplicação, como do frontend para o backend ou entre serviços.

Campos principais:
- origem: local de origem da carga
- destino: local de destino da carga
- shipper: remetente da mercadoria
- consignee: destinatário da mercadoria
- agente: agente responsável pelo transporte
- numeroHouse: número da House Bill of Lading (HBL)
- numeroMaster: número da Master Bill of Lading (MBL)
- ciaAerea: companhia aérea ou armador, dependendo do modal
- refCliente: referência fornecida pelo cliente
- incoterm: termos comerciais (ex: FOB, CIF)
- etd: data prevista de embarque (Estimated Time of Departure)
- eta: data prevista de chegada (Estimated Time of Arrival)
- tipo: modal do transporte ("aereo" ou "maritmo")
*/
public class ShippingInstructionDTO {
    private String origem;
    private String destino;
    private String shipper;
    private String consignee;
    private String agente;
    private String numeroHouse;
    private String numeroMaster;
    private String ciaAerea;   // ou armador, dependendo do tipo
    private String refCliente;
    private String incoterm;
    private String etd;
    private String eta;
    private String tipo; // "aereo" ou "maritmo"

    // Getters e setters
    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public String getShipper() { return shipper; }
    public void setShipper(String shipper) { this.shipper = shipper; }

    public String getConsignee() { return consignee; }
    public void setConsignee(String consignee) { this.consignee = consignee; }

    public String getAgente() { return agente; }
    public void setAgente(String agente) { this.agente = agente; }

    public String getNumeroHouse() { return numeroHouse; }
    public void setNumeroHouse(String numeroHouse) { this.numeroHouse = numeroHouse; }

    public String getNumeroMaster() { return numeroMaster; }
    public void setNumeroMaster(String numeroMaster) { this.numeroMaster = numeroMaster; }

    public String getCiaAerea() { return ciaAerea; }
    public void setCiaAerea(String ciaAerea) { this.ciaAerea = ciaAerea; }

    public String getRefCliente() { return refCliente; }
    public void setRefCliente(String refCliente) { this.refCliente = refCliente; }

    public String getIncoterm() { return incoterm; }
    public void setIncoterm(String incoterm) { this.incoterm = incoterm; }

    public String getEtd() { return etd; }
    public void setEtd(String etd) { this.etd = etd; }

    public String getEta() { return eta; }
    public void setEta(String eta) { this.eta = eta; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
