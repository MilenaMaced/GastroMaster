package br.com.ufrpeuag.gastromaster.ui;

import java.util.Map;

import br.com.ufrpeuag.gastromaster.negocio.fachada.Fachada;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class GraficoLucroAnualControlador extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Map<String, Integer> example = Fachada.getSingleton().recuperarPorAno();
		
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Anos ");

		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Valor Total R$");

		
		BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
		
		
		XYChart.Series<String, Number> dataSeries1 = new XYChart.Series<String, Number>();
		
		for (String key : example.keySet()) {
			Integer value = example.get(key);
			dataSeries1.getData().add(new XYChart.Data<String, Number>(key, value));
			
		}

		barChart.getData().add(dataSeries1);
		barChart.setTitle("Vendas por ano");
		barChart.setLegendSide(null);
        
		VBox vbox = new VBox(barChart);

		primaryStage.setTitle("Vendas por ano");
		Scene scene = new Scene(vbox, 400, 200);

		primaryStage.setScene(scene);
		primaryStage.setHeight(300);
		primaryStage.setWidth(400);

		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
