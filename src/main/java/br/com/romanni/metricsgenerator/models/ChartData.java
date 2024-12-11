package br.com.romanni.metricsgenerator.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChartData {
    private String category_chart;
    private Double value_category_chart;

    public ChartData(String category_chart, Double value_category_chart) {
        this.category_chart = category_chart;
        this.value_category_chart = value_category_chart;
    }

}
