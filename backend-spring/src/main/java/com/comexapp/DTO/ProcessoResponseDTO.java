package com.comexapp.DTO;

/*
Responsável: Eduardo Sanvido
A classe ProcessoResponseDTO.java é um DTO (Data Transfer Object) usado para
retornar informações de um processo ao frontend. Ela encapsula dados essenciais
do processo, incluindo:
- id: identificador único do processo
- codigo: código do processo
- titulo: título do processo
- tipo: tipo do processo (ex: Importação, Exportação)
- modal: modal de transporte (ex: Marítimo, Aéreo)
- observacao: observações adicionais
- responsavel: nome do responsável pelo processo
- dataCriacao: data de criação do processo
- quantidadeArquivos: número de arquivos anexados ao processo
*/
public class ProcessoResponseDTO {

    private Long id;
    private String codigo;
    private String titulo;
    private String tipo;
    private String modal;
    private String observacao;
    private String responsavel;
    private String dataCriacao;
    private int quantidadeArquivos;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getModal() { return modal; }
    public void setModal(String modal) { this.modal = modal; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }

    public String getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }

    public int getQuantidadeArquivos() { return quantidadeArquivos; }
    public void setQuantidadeArquivos(int quantidadeArquivos) { this.quantidadeArquivos = quantidadeArquivos; }
}
