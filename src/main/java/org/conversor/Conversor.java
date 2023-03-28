package org.conversor;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Conversor extends JPanel {
    protected JLabel titulo;
    protected JTextField entradaDoUsuario;
    protected JComboBox<String> primeiraCaixaSelecao;
    protected JComboBox<String> segundaCaixaSelecao;
    protected JButton botaoConverter;
    protected JTextArea valorConvertido;

    Conversor() {
        this.setBounds(0, 0, 320, 493);
        this.setLayout(null);

        titulo = new JLabel();
        titulo.setBounds(60, 40, 320, 32);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(titulo);

        JLabel tituloEntradaDoUsuario = new JLabel("Valor");
        tituloEntradaDoUsuario.setFont(new Font("Arial", Font.PLAIN, 13));
        tituloEntradaDoUsuario.setBounds(20, 100, 120, 13);
        this.add(tituloEntradaDoUsuario);

        entradaDoUsuario = new JTextField();
        entradaDoUsuario.setPreferredSize(new Dimension(120, 42));
        entradaDoUsuario.setBounds(20, 120, 120, 42);
        entradaDoUsuario.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) converter();}
        });

        this.add(entradaDoUsuario);

        JLabel tituloPimeiraCaixaSelecao = new JLabel("De");
        tituloPimeiraCaixaSelecao.setFont(new Font("Arial", Font.PLAIN, 13));
        tituloPimeiraCaixaSelecao.setBounds(160, 100, 120, 13);
        this.add(tituloPimeiraCaixaSelecao);

        primeiraCaixaSelecao = new JComboBox<>(getSelecao());
        primeiraCaixaSelecao.setBounds(160, 120, 120, 42);
        this.add(primeiraCaixaSelecao);

        JLabel tituloSegundaCaixaSelecao = new JLabel("Para");
        tituloSegundaCaixaSelecao.setFont(new Font("Arial", Font.PLAIN, 13));
        tituloSegundaCaixaSelecao.setBounds(160, 180, 120, 13);
        this.add(tituloSegundaCaixaSelecao);

        segundaCaixaSelecao = new JComboBox<>(getSelecao());
        segundaCaixaSelecao.setBounds(160, 200, 120, 42);

        this.add(segundaCaixaSelecao);

        botaoConverter = new JButton();
        botaoConverter.setText("Converter");
        botaoConverter.setBounds(20, 200, 120, 42);
        botaoConverter.addActionListener(e -> converter());
        this.add(botaoConverter);

        valorConvertido = new JTextArea ();
        valorConvertido.setBounds(20, 280, 260, 72);
        valorConvertido.setFont(new Font("Arial", Font.BOLD, 16));
        valorConvertido.setText("0,00");
        valorConvertido.setEditable(false);
        valorConvertido.setLineWrap(true);
        valorConvertido.setWrapStyleWord(true);
        this.add(valorConvertido);
    }

    protected void setTitle(String title) {
        titulo.setText(String.format("Conversor de %s", title));
        System.out.println(title);
    }

    protected void converter() {}

    protected String[] getSelecao() {
        return new String[]{};
    }
}


class ConversorDeMoeda extends Conversor {

    ConversorDeMoeda() {
        super();
        super.setTitle("Moeda");
        primeiraCaixaSelecao.setSelectedItem("Real");
        segundaCaixaSelecao.setSelectedItem("Dolar");
    }

    @Override
    public String[] getSelecao() {
        return new String[]{"Real", "Dolar", "Euro", "Libra Esterlina", "Peso Argentino", "Peso Chileno"};
    }

    @Override
    public void converter() {
        String valorPrimeiraCaixa = Objects.requireNonNull(primeiraCaixaSelecao.getSelectedItem()).toString();
        String valorSegundaCaixa = Objects.requireNonNull(segundaCaixaSelecao.getSelectedItem()).toString();

        double valor;
        double valorCalculado;

        try {
            valor = Double.parseDouble(entradaDoUsuario.getText()) > 0 ? Double.parseDouble(entradaDoUsuario.getText()) : 0;
            valorCalculado = getCalculoConversao(valorPrimeiraCaixa, valorSegundaCaixa, valor);
        } catch (NullPointerException | NumberFormatException | ArithmeticException e) {
            valorCalculado = 0D;
        }

        String valorFormatado = String.format("%,.2f %s", valorCalculado, valorSegundaCaixa);

        valorConvertido.setText(valorFormatado);
    }

    private Double getCalculoConversao(String deMoeda, String paraMoeda, Double valor) throws NullPointerException, ArithmeticException {

        Map<String, Map<String, Double>> valoresTaxaMoedas = new HashMap<>();

        valoresTaxaMoedas.put("Dolar", new HashMap<>() {{
            put("Dolar", 1.0);
            put("Euro", 0.93);
            put("Real", 5.28);
            put("Libra Esterlina", 0.82);
            put("Peso Argentino", 200.1);
            put("Peso Chileno", 814.84);
        }});

        valoresTaxaMoedas.put("Real", new HashMap<>() {{
            put("Dolar", 0.19);
            put("Euro", 0.18);
            put("Real", 1.0);
            put("Libra Esterlina", 0.16);
            put("Peso Argentino", 37.9);
            put("Peso Chileno", 154.34);
        }});

        valoresTaxaMoedas.put("Euro", new HashMap<>() {{
            put("Dolar", 1.08);
            put("Euro", 1.0);
            put("Real", 5.56);
            put("Libra Esterlina", 0.89);
            put("Peso Argentino", 215.72);
            put("Peso Chileno", 878.44);
        }});

        valoresTaxaMoedas.put("Libra Esterlina", new HashMap<>() {{
            put("Dolar", 1.22);
            put("Euro", 1.13);
            put("Real", 6.43);
            put("Libra Esterlina", 1.0);
            put("Peso Argentino", 243.67);
            put("Peso Chileno", 992.27);
        }});

        valoresTaxaMoedas.put("Peso Argentino", new HashMap<>() {{
            put("Dolar", 0.005);
            put("Euro", 0.0046);
            put("Real", 0.026);
            put("Libra Esterlina", 0.0041);
            put("Peso Argentino", 1.0);
            put("Peso Chileno", 4.07);
        }});

        valoresTaxaMoedas.put("Peso Chileno", new HashMap<>() {{
            put("Dolar", 0.0012);
            put("Euro", 0.0011);
            put("Real", 0.0064);
            put("Libra Esterlina", 0.001);
            put("Peso Argentino", 0.24);
            put("Peso Chileno", 1.0);
        }});

        Double taxa = valoresTaxaMoedas.get(deMoeda).get(paraMoeda);
        return valor * taxa;
    }
}


class ConversorDeTemperatura extends Conversor {

    ConversorDeTemperatura() {
        super.setTitle("Temperatura");
        primeiraCaixaSelecao.setSelectedItem("Celsius");
        segundaCaixaSelecao.setSelectedItem("Fahrenheit");
    }

    @Override
    public String[] getSelecao() {
        return new String[] {"Celsius", "Fahrenheit", "Newton", "Delisle", "Kelvin", "Réaumur", "Rankine", "Romer"};
    }

    @Override
    public void converter() {

        //TODO: fazer a entrada de dados também aceitar numeros negativos

        String valorPrimeiraCaixa = Objects.requireNonNull(primeiraCaixaSelecao.getSelectedItem()).toString();
        String valorSegundaCaixa = Objects.requireNonNull(segundaCaixaSelecao.getSelectedItem()).toString();

        Double valor;
        Double valorCalculado;

        try {
            System.out.println("asdf" + entradaDoUsuario.getText());
            valor = Double.parseDouble(entradaDoUsuario.getText());
            valorCalculado = getCalculoTemperatura(valorPrimeiraCaixa, valorSegundaCaixa, valor);
        } catch (NullPointerException | NumberFormatException | ArithmeticException e) {
            valorCalculado = 0D;
        }

        String valorFormatado = String.format("%,.2f %s", valorCalculado, valorSegundaCaixa);

        valorConvertido.setText(valorFormatado);
    }

    private Double getCalculoTemperatura(String deTemperatura, String paraTemperatura, Double valor) throws NullPointerException, ArithmeticException {

        Double calculo;

        switch (deTemperatura) {
            case "Celsius":
                switch (paraTemperatura) {
                    case "Fahrenheit":
                        calculo = (valor * 9 / 5) + 32;
                        break;
                    case "Newton":
                        calculo = valor * 33 / 100;
                        break;
                    case "Delisle":
                        calculo = (100 - valor) * 3 / 2;
                        break;
                    case "Kelvin":
                        calculo = valor + 273.15;
                        break;
                    case "Réaumur":
                        calculo = valor * 4 / 5;
                        break;
                    case "Rankine":
                        calculo = (valor + 273.15) * 9 / 5;
                        break;
                    case "Romer":
                        calculo = valor * 21 / 40 + 7.5;
                        break;
                    default:
                        calculo = valor;
                }
                break;

            case "Fahrenheit":
                switch (paraTemperatura) {
                    case "Celsius":
                        calculo = (valor - 32) * 5 / 9;
                        break;
                    case "Newton":
                        calculo = (valor - 32) * 11 / 60;
                        break;
                    case "Delisle":
                        calculo = (212 - valor) * 5 / 6;
                        break;
                    case "Kelvin":
                        calculo = (valor + 459.67) * 5 / 9;
                        break;
                    case "Réaumur":
                        calculo = (valor - 32) * 4 / 9;
                        break;
                    case "Rankine":
                        calculo = valor + 459.67;
                        break;
                    case "Romer":
                        calculo = (valor - 32) * 7 / 24 + 7.5;
                        break;
                    default:
                        calculo = valor;
                }
                break;

            case "Newton":
                switch (paraTemperatura) {
                    case "Celsius":
                        calculo = valor * 100 / 33;
                        break;
                    case "Fahrenheit":
                        calculo = valor * 60 / 11 + 32;
                        break;
                    case "Delisle":
                        calculo = (33 - valor) * 50 / 11;
                        break;
                    case "Kelvin":
                        calculo = valor * 100 / 33 + 273.15;
                        break;
                    case "Réaumur":
                        calculo = valor * 80 / 33;
                        break;
                    case "Rankine":
                        //Como não é possível converter diretamente, converte para Kelvin e depois converte para Rankine.
                        double kelvin = (valor / 0.33) + 273.15;
                        calculo = kelvin * 1.8; //convertendo kelvin em Rankine
                        break;
                    case "Romer":
                        calculo = valor * 35 / 22 + 7.5;
                        break;
                    default:
                        calculo = valor;
                }
                break;

            case "Delisle":
                switch (paraTemperatura) {
                    case "Celsius":
                        calculo = 100 - valor * 2 / 3;
                        break;
                    case "Fahrenheit":
                        calculo = 212 - valor * 6 / 5;
                        break;
                    case "Newton":
                        calculo = 33 - (valor * 11.0 / 50);
                        break;
                    case "Kelvin":
                        calculo = 373.15 - valor * 2 / 3;
                        break;
                    case "Réaumur":
                        calculo = 80 - valor * 8 / 15;
                        break;
                    case "Rankine":
                        //Como não é possível converter diretamente, converte para Kelvin e depois converte para Rankine.
                        double kelvin = 373.15 - valor * 2 / 3;
                        calculo = kelvin * 1.8; //convertendo kelvin em Rankine
                        break;
                    case "Romer":
                        calculo = 60 - valor * 7 / 20;
                        break;
                    default:
                        calculo = valor;
                }
                break;

            case "Kelvin":
                switch (paraTemperatura) {
                    case "Celsius":
                        calculo = valor - 273.15;
                        break;
                    case "Fahrenheit":
                        calculo = valor * 9 / 5 - 459.67;
                        break;
                    case "Newton":
                        calculo = (valor - 273.15) * 33 / 100;
                        break;
                    case "Delisle":
                        calculo = (373.15 - valor) * 3 / 2;
                        break;
                    case "Réaumur":
                        calculo = (valor - 273.15) * 4 / 5;
                        break;
                    case "Rankine":
                        calculo = valor * 9 / 5;
                        break;
                    case "Romer":
                        calculo = (valor - 273.15) * 21 / 40 + 7.5;
                        break;
                    default:
                        calculo = valor;
                }
                break;

            case "Réaumur":
                switch (paraTemperatura) {
                    case "Celsius":
                        calculo = valor * 5 / 4;
                        break;
                    case "Fahrenheit":
                        calculo = valor * 9 / 4 + 32;
                        break;
                    case "Newton":
                        calculo = valor * 33 / 80;
                        break;
                    case "Delisle":
                        calculo = (80 - valor) * 15 / 8;
                        break;
                    case "Kelvin":
                        calculo = valor * 5 / 4 + 273.15;
                        break;
                    case "Rankine":
                        calculo = (valor * 9 / 4 + 491.67) * 5 / 9;
                        break;
                    case "Romer":
                        calculo = valor * 21 / 32 + 7.5;
                        break;
                    default:
                        calculo = valor;
                }
                break;

            case "Rankine":
                switch (paraTemperatura) {
                    case "Celsius":
                        calculo = (valor - 491.67) * 5 / 9;
                        break;
                    case "Fahrenheit":
                        calculo = valor - 459.67;
                        break;
                    case "Newton":
                        calculo = (valor - 491.67) * 11 / 60;
                        break;
                    case "Delisle":
                        calculo = (671.67 - valor) * 5 / 6;
                        break;
                    case "Kelvin":
                        calculo = valor * 5 / 9;
                        break;
                    case "Réaumur":
                        calculo = (valor - 491.67) * 4 / 9;
                        break;
                    case "Romer":
                        calculo = (valor - 491.67) * 7 / 24 + 7.5;
                        break;
                    default:
                        calculo = valor;
                }
                break;

            case "Romer":
                switch (paraTemperatura) {
                    case "Celsius":
                        calculo = (valor - 7.5) * 40 / 21;
                        break;
                    case "Fahrenheit":
                        calculo = (valor - 7.5) * 24 / 7 + 32;
                        break;
                    case "Newton":
                        calculo = (valor - 7.5) * 22 / 35;
                        break;
                    case "Delisle":
                        calculo = (60 - valor) * 20 / 7;
                        break;
                    case "Kelvin":
                        calculo = (valor - 7.5) * 40 / 21 + 273.15;
                        break;
                    case "Réaumur":
                        calculo = (valor - 7.5) * 32 / 21;
                        break;
                    case "Rankine":
                        calculo = (valor - 7.5) * 24 / 7 + 491.67;
                        break;
                    default:
                        calculo = valor;
                }
                break;

            default:
                calculo = valor;
        }
        return calculo;
    }
}

class ConversorDeDistancia extends Conversor {

    ConversorDeDistancia() {
        super.setTitle("Distancia");
        primeiraCaixaSelecao.setSelectedItem("Metros");
        segundaCaixaSelecao.setSelectedItem("Quilômetros");
    }

    @Override
    public String[] getSelecao() {
        return new String[]{"Quilômetros", "Metros", "Milhas", "Centímetros", "Milímetros", "Pés", "Anos-luz",  "Léguas", "Milhas"};
    }

    @Override
    public void converter() {
        String valorPrimeiraCaixa = Objects.requireNonNull(primeiraCaixaSelecao.getSelectedItem()).toString();
        String valorSegundaCaixa = Objects.requireNonNull(segundaCaixaSelecao.getSelectedItem()).toString();

        double valor;
        String valorCalculado;

        try {
            valor = Double.parseDouble(entradaDoUsuario.getText()) > 0 ? Double.parseDouble(entradaDoUsuario.getText()) : 0;
            valorCalculado = getCalculoDistancia(valorPrimeiraCaixa, valorSegundaCaixa, valor);
        } catch (NullPointerException | NumberFormatException | ArithmeticException e) {
            valorCalculado = "0.0";
            System.out.println("exc");
        }

        String valorFormatado;

        if (valorCalculado.contains("E")) {
            valorFormatado = String.format("%s %s", valorCalculado, valorSegundaCaixa);
        } else {
            System.out.println("dble");
            Double valorCalculadoConvertido = Double.parseDouble(valorCalculado);
            valorFormatado = String.format("%,f %s", valorCalculadoConvertido, valorSegundaCaixa);
        }

        valorConvertido.setText(valorFormatado);
    }

    private String getCalculoDistancia(String deDistancia, String paraDistancia, Double valor) throws NullPointerException, ArithmeticException {

        double resultado = 0D;
        BigDecimal resultadoAnosLuz = null;

        switch (deDistancia) {
            case "Quilômetros":
                switch (paraDistancia) {
                    case "Metros":
                        resultado = valor * 1000;
                        break;
                    case "Milhas":
                        resultado = valor * 0.621371;
                        break;
                    case "Centímetros":
                        resultado = valor * 100000;
                        break;
                    case "Milímetros":
                        resultado = valor * 1000000;
                        break;
                    case "Pés":
                        resultado = valor * 3280.84;
                        break;
                    case "Anos-luz":
                        resultadoAnosLuz = new BigDecimal(valor);
                        resultadoAnosLuz = resultadoAnosLuz.divide(BigDecimal.valueOf(9.461e+12), 20, RoundingMode.HALF_EVEN);
                        break;
                    case "Léguas":
                        resultado = valor / 4.828;
                        break;
                    default:
                        resultado = valor;
                        break;
                }
                break;
            case "Metros":
                switch (paraDistancia) {
                    case "Quilômetros":
                        resultado = valor / 1000;
                        break;
                    case "Milhas":
                        resultado = valor * 0.000621371;
                        break;
                    case "Centímetros":
                        resultado = valor * 100;
                        break;
                    case "Milímetros":
                        resultado = valor * 1000;
                        break;
                    case "Pés":
                        resultado = valor * 3.28084;
                        break;
                    case "Anos-luz":
                        resultadoAnosLuz = new BigDecimal(valor);
                        resultadoAnosLuz = resultadoAnosLuz.divide(BigDecimal.valueOf(9.461e+15), 20, RoundingMode.HALF_EVEN);
                        break;
                    case "Léguas":
                        resultado = valor / 4828;
                        break;
                    default:
                        resultado = valor;
                        break;
                }
                break;
            case "Milhas":
                switch (paraDistancia) {
                    case "Quilômetros":
                        resultado = valor / 0.621371;
                        break;
                    case "Metros":
                        resultado = valor / 0.000621371;
                        break;
                    case "Centímetros":
                        resultado = valor / 0.00000621371;
                        break;
                    case "Milímetros":
                        resultado = valor / 0.000000621371;
                        break;
                    case "Pés":
                        resultado = valor * 5280;
                        break;
                    case "Anos-luz":
                        resultadoAnosLuz = new BigDecimal(valor);
                        resultadoAnosLuz = resultadoAnosLuz.divide(BigDecimal.valueOf(5.879e+12), 20, RoundingMode.HALF_EVEN);
                        break;
                    case "Léguas":
                        resultado = valor / 3;
                        break;
                    default:
                        resultado = valor;
                        break;
                }
                break;
            case "Centímetros":
                switch (paraDistancia) {
                    case "Quilômetros":
                        resultado = valor / 100000;
                        break;
                    case "Metros":
                        resultado = valor / 100;
                        break;
                    case "Milhas":
                        resultado = valor * 0.00000621371;
                        break;
                    case "Milímetros":
                        resultado = valor * 10;
                        break;
                    case "Pés":
                        resultado = valor * 0.0328084;
                        break;
                    case "Anos-luz":
                        resultadoAnosLuz = new BigDecimal(valor);
                        resultadoAnosLuz = resultadoAnosLuz.divide(BigDecimal.valueOf(9.461e+17), 20, RoundingMode.HALF_EVEN);
                        break;
                    case "Léguas":
                        resultado = valor / 482800;
                        break;
                    default:
                        resultado = valor;
                        break;
                }
                break;
            case "Milímetros":
                switch (paraDistancia) {
                    case "Quilômetros":
                        resultado = valor / 1000000;
                        break;
                    case "Metros":
                        resultado = valor / 1000;
                        break;
                    case "Milhas":
                        resultado = valor * 6.2137e-7;
                        break;
                    case "Centímetros":
                        resultado = valor / 10;
                        break;
                    case "Pés":
                        resultado = valor * 0.00328084;
                        break;
                    case "Anos-luz":
                        resultadoAnosLuz = new BigDecimal(valor);
                        resultadoAnosLuz = resultadoAnosLuz.divide(BigDecimal.valueOf(9.461e+18), 20, RoundingMode.HALF_EVEN);
                        break;
                    case "Léguas":
                        resultado = valor / 4.828e+6;
                        break;
                    default:
                        resultado = valor;
                        break;
                }
                break;
            case "Pés":
                switch (paraDistancia) {
                    case "Quilômetros":
                        resultado = valor / 3280.84;
                        break;
                    case "Metros":
                        resultado = valor / 3.28084;
                        break;
                    case "Milhas":
                        resultado = valor / 5280;
                        break;
                    case "Centímetros":
                        resultado = valor / 0.0328084;
                        break;
                    case "Milímetros":
                        resultado = valor / 0.00328084;
                        break;
                    case "Anos-luz":
                        resultadoAnosLuz = new BigDecimal(valor);
                        resultadoAnosLuz = resultadoAnosLuz.divide(BigDecimal.valueOf(3.104e+16), 20, RoundingMode.HALF_EVEN);
                        break;
                    case "Léguas":
                        resultado = valor / 15840;
                        break;
                    default:
                        resultado = valor;
                        break;
                }
                break;
            case "Anos-luz":
                switch (paraDistancia) {
                    case "Quilômetros":
                        resultadoAnosLuz = new BigDecimal(valor);
                        resultadoAnosLuz = resultadoAnosLuz.multiply(BigDecimal.valueOf(9.461e+12));
                        break;
                    case "Metros":
                        resultadoAnosLuz = new BigDecimal(valor);
                        resultadoAnosLuz = resultadoAnosLuz.multiply(BigDecimal.valueOf(9.461e+15));
                        break;
                    case "Milhas":
                        resultadoAnosLuz = new BigDecimal(valor);
                        resultadoAnosLuz = resultadoAnosLuz.multiply(BigDecimal.valueOf(5.878e+12));
                        break;
                    case "Centímetros":
                        resultadoAnosLuz = new BigDecimal(valor);
                        resultadoAnosLuz = resultadoAnosLuz.multiply(BigDecimal.valueOf(9.461e+14));
                        break;
                    case "Milímetros":
                        resultadoAnosLuz = new BigDecimal(valor);
                        resultadoAnosLuz = resultadoAnosLuz.multiply(BigDecimal.valueOf(9.461e+17));
                        break;
                    case "Pés":
                        resultadoAnosLuz = new BigDecimal(valor);
                        resultadoAnosLuz = resultadoAnosLuz.multiply(BigDecimal.valueOf(3.104e+16));
                        break;
                    case "Léguas":
                        resultadoAnosLuz = new BigDecimal(valor);
                        resultadoAnosLuz = resultadoAnosLuz.multiply(BigDecimal.valueOf(1.96e+12));
                        break;
                    default:
                        resultado = valor;
                        break;
                }
                break;
            case "Léguas":
                switch (paraDistancia) {
                    case "Quilômetros":
                        resultado = valor * 4.828;
                        break;
                    case "Metros":
                        resultado = valor * 4828;
                        break;
                    case "Milhas":
                        resultado = valor * 3;
                        break;
                    case "Centímetros":
                        resultado = valor * 482800;
                        break;
                    case "Milímetros":
                        resultado = valor * 4.828e+6;
                        break;
                    case "Pés":
                        resultado = valor * 15840;
                        break;
                    case "Anos-luz":
                        resultadoAnosLuz = new BigDecimal(valor);
                        resultadoAnosLuz = resultadoAnosLuz.divide(BigDecimal.valueOf(1.96e+12), 20, RoundingMode.HALF_EVEN);
                        break;
                    default:
                        resultado = valor;
                        break;
                }
                break;
        }

        if (Objects.isNull(resultadoAnosLuz)) {
            return String.valueOf(resultado);
        }

        System.out.println(resultadoAnosLuz.toEngineeringString());
        return resultadoAnosLuz.toEngineeringString();
    }
}