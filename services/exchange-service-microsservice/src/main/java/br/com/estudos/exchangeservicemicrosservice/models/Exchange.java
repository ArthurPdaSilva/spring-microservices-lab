package br.com.estudos.exchangeservicemicrosservice.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity(name = "exchange")
public class Exchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_currency", nullable = false, length = 3)
    private String from;

    @Column(name = "to_currency", nullable = false, length = 3)
    private String to;

    @Column(name = "conversion_factor")
    private BigDecimal conversionFactor;

//    //É para o atributo não ser persistido no banco de dados, ou seja, não será criado uma coluna para ele na tabela
//    @Transient
//    private BigDecimal convertedValue;

    public Exchange() {}

    public Exchange(Long id, String from, String to, BigDecimal conversionFactor) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.conversionFactor = conversionFactor;
    }

    public Long getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public BigDecimal getConversionFactor() {
        return conversionFactor;
    }
}
