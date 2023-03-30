package org.conversor;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Conversor extends JPanel {
    protected JLabel titulo;
    protected JTextField entradaDoUsuario;
    protected JComboBox<String> primeiraCaixaSelecao;
    protected JComboBox<String> segundaCaixaSelecao;
    protected JButton botaoOk;
    protected JTextArea valorConvertido;
    protected JLabel mensagemErro;
    protected JLabel tituloEntradaDoUsuario;
    protected JLabel tituloPimeiraCaixaSelecao;
    protected JLabel tituloSegundaCaixaSelecao;

    Conversor() {
        this.setBounds(0, 0, 320, 493);
        this.setLayout(null);

        titulo = new JLabel();
        titulo.setBounds(0, 40, 290, 16);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(titulo);

        tituloEntradaDoUsuario = new JLabel("Valor");
        tituloEntradaDoUsuario.setFont(new Font("Arial", Font.PLAIN, 13));
        tituloEntradaDoUsuario.setBounds(20, 100, 120, 13);
        this.add(tituloEntradaDoUsuario);

        entradaDoUsuario = new JTextField();
        entradaDoUsuario.setPreferredSize(new Dimension(120, 42));
        entradaDoUsuario.setBounds(20, 120, 120, 42);
        entradaDoUsuario.setText("0,00");
        entradaDoUsuario.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                entradaDoUsuario.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (entradaDoUsuario.getText().isEmpty()) {
                    entradaDoUsuario.setText("0,0");
                }
            }
        });
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

        mensagemErro = new JLabel();
        mensagemErro.setBounds(22, 165, 120, 13);
        mensagemErro.setForeground(new Color(0xE51B27));
        this.add(mensagemErro);

        tituloPimeiraCaixaSelecao = new JLabel("De");
        tituloPimeiraCaixaSelecao.setFont(new Font("Arial", Font.PLAIN, 13));
        tituloPimeiraCaixaSelecao.setBounds(163, 100, 120, 13);
        this.add(tituloPimeiraCaixaSelecao);

        primeiraCaixaSelecao = new JComboBox<>(getSelecao());
        primeiraCaixaSelecao.setBounds(160, 120, 120, 42);
        this.add(primeiraCaixaSelecao);

        tituloSegundaCaixaSelecao = new JLabel("Para");
        tituloSegundaCaixaSelecao.setFont(new Font("Arial", Font.PLAIN, 13));
        tituloSegundaCaixaSelecao.setBounds(163, 180, 120, 13);
        this.add(tituloSegundaCaixaSelecao);

        segundaCaixaSelecao = new JComboBox<>(getSelecao());
        segundaCaixaSelecao.setBounds(160, 200, 120, 42);
        this.add(segundaCaixaSelecao);

        botaoOk = new JButton();
        botaoOk.setText("Converter");
        botaoOk.setBounds(20, 200, 120, 42);
        botaoOk.addActionListener(e -> converter());
        botaoOk.setBackground(new Color(0x568af2));
        botaoOk.setFont(new Font("Arial", Font.PLAIN, 13));
        botaoOk.setForeground(Color.BLACK);
        botaoOk.setBorderPainted(false);
        this.add(botaoOk);

        valorConvertido = new JTextArea();
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
        titulo.setHorizontalAlignment(JLabel.CENTER);
    }

    protected void converter() {}

    protected String[] getSelecao() {
        return new String[]{};
    }
}


class ConversorDeMoeda extends Conversor {
    private JButton botaoAtualizar;
    private JFrame janelaAtualizar;
    private Map<String, Map<String, String>> moedas;
    private Map<String, BigDecimal> valoresTaxaMoedasEmDolar;
    private JLabel dataTaxaCambio;

    ConversorDeMoeda() {
        super.setTitle("Moeda");
        primeiraCaixaSelecao.setSelectedItem("Real");
        segundaCaixaSelecao.setSelectedItem("Dolar");

        valoresTaxaMoedasEmDolar = new HashMap<>(){{
            put("Dolar", new BigDecimal("1.0"));
            put("Euro", new BigDecimal("0.93"));
            put("Real", new BigDecimal("5.28"));
            put("Libra Esterlina", new BigDecimal("0.82"));
            put("Peso Argentino", new BigDecimal("200.1"));
            put("Peso Chileno", new BigDecimal("814.84"));
        }};

        botaoAtualizar = new JButton();
        ImageIcon icone = new ImageIcon(Objects.requireNonNull(Conversor.class.getResource("/refresh-freepik-gray.png")));
        botaoAtualizar.setIcon(icone);
        botaoAtualizar.setBounds(250, 15, 30, 30);
        botaoAtualizar.addActionListener(e -> janelaAtualizarValores());
        this.add(botaoAtualizar);

        dataTaxaCambio = new JLabel("Data valores: 18/03/2023");
        dataTaxaCambio.setBounds(27, 350, 200, 20);
        this.add(dataTaxaCambio);
    }

    @Override
    protected String[] getSelecao() {

        moedas = new HashMap<>();
        moedas.put("Real", new HashMap<>(){{put("BRL", "R$");}});
        moedas.put("Dolar", new HashMap<>(){{put("USD", "$");}});
        moedas.put("Euro", new HashMap<>(){{put("EUR", "€");}});
        moedas.put("Libra Esterlina", new HashMap<>(){{put("GBP", "£");}});
        moedas.put("Peso Argentino", new HashMap<>(){{put("ARS", "$");}});
        moedas.put("Peso Chileno", new HashMap<>(){{put("CLP", "$");}});

        String[] nomes = new String[6];

        int indice = 0;

        for (String nome : moedas.keySet()) {

            nomes[indice] = nome;
            indice++;
        }

        return nomes;
    }

    @Override
    protected void converter() {
        String valorPrimeiraCaixa = Objects.requireNonNull(primeiraCaixaSelecao.getSelectedItem()).toString();
        String valorSegundaCaixa = Objects.requireNonNull(segundaCaixaSelecao.getSelectedItem()).toString();

        double valor;
        double valorCalculado;

        try {
            String tempValor = entradaDoUsuario.getText().replace(".", "_");
            tempValor = tempValor.replace(",", ".");
            valor = Double.parseDouble(tempValor) > 0 ? Double.parseDouble(tempValor) : 0;
            valorCalculado = getCalculoConversao(valorPrimeiraCaixa, valorSegundaCaixa, valor);
            super.mensagemErro.setText("");
        } catch (NullPointerException | NumberFormatException | ArithmeticException e) {
            valorCalculado = 0D;
            super.mensagemErro.setText("Valor inválido.");
        }

        String simbolo = "";

        for (String simboloTemp : moedas.get(valorSegundaCaixa).values()) {
            simbolo = simboloTemp;
        }

        String valorFormatado = String.format("%s %,.2f", simbolo, valorCalculado);

        valorConvertido.setText(valorFormatado);
    }

    private Double getCalculoConversao(String deMoeda, String paraMoeda, Double valor) throws NullPointerException, ArithmeticException {

        BigDecimal taxa = new BigDecimal(valoresTaxaMoedasEmDolar.get("Dolar").toString());
        taxa = taxa.divide(valoresTaxaMoedasEmDolar.get(deMoeda), RoundingMode.HALF_EVEN);
        taxa = taxa.multiply(valoresTaxaMoedasEmDolar.get(paraMoeda));
        taxa = taxa.multiply(BigDecimal.valueOf(valor));

        return taxa.doubleValue();
    }

    private void janelaAtualizarValores() {
        janelaAtualizar = new JFrame("Atualizar preço das moedas");
        janelaAtualizar.setSize(320, 320);
        janelaAtualizar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaAtualizar.setResizable(false);
        ImageIcon icone = new ImageIcon(Objects.requireNonNull(Conversor.class.getResource("/funil-Maan-icons.png")));
        janelaAtualizar.setIconImage(icone.getImage());
        janelaAtualizar.setLayout(null);
        janelaAtualizar.setLocationRelativeTo(this.botaoAtualizar);

        JTextArea descricaoTexto = new JTextArea();
        String descricao = "Para obter a taxa de cambio atual, nós usamos a API Open Exchange Rates" +
                " para atualizar os preços das moedas.";
        descricaoTexto.setText(descricao);
        descricaoTexto.setEditable(false);
        descricaoTexto.setLineWrap(true);
        descricaoTexto.setWrapStyleWord(true);
        descricaoTexto.setBounds(43, 20, 220, 60);
        janelaAtualizar.add(descricaoTexto);

        JLabel entradaApiTitulo = new JLabel("Chave API");
        entradaApiTitulo.setFont(new Font("Arial", Font.BOLD, 13));
        entradaApiTitulo.setBounds(50, 95, 120, 16);
        janelaAtualizar.add(entradaApiTitulo);

        JTextField entradaApi = new JTextField();
        entradaApi.setBounds(47, 120, 190, 42);
        janelaAtualizar.add(entradaApi);

        JButton botaoOk = new JButton("Ok");
        botaoOk.setBounds(50, 190, 70, 42);
        botaoOk.setBackground(new Color(0x568af2));
        botaoOk.setFont(new Font("Arial", Font.PLAIN, 13));
        botaoOk.setForeground(Color.BLACK);
        botaoOk.setBorderPainted(false);
        janelaAtualizar.add(botaoOk);

        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setBounds(130, 190, 100, 42);
        botaoCancelar.addActionListener(e -> janelaAtualizar.setVisible(false));
        janelaAtualizar.add(botaoCancelar);

        JTextArea textoStatus = new JTextArea();
        textoStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        textoStatus.setBounds(45, 163, 220, 16);
        textoStatus.setEditable(false);

        botaoOk.addActionListener(e -> {
            try {
                textoStatus.setForeground(new Color(0x38b000));
                atualizarValores(entradaApi.getText());
                textoStatus.setText("Valores atualizado com sucesso!");
                janelaAtualizar.setVisible(false);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.getStackTrace();
                textoStatus.setForeground(new Color(0xE51B27));
                textoStatus.setText(ex.getMessage());
            }
        });

        janelaAtualizar.add(textoStatus);

        JLabel link = new JLabel();
        link.setText("<html><a href='https://openexchangerates.org/'>https://openexchangerates.org/</a></html>");
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        link.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://openexchangerates.org/"));
                } catch (Exception ex) {
                    ex.getStackTrace();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        link.setFont(new Font("Arial", Font.PLAIN, 13));
        link.setBounds(50, 250, 200, 20);
        janelaAtualizar.add(link);

        janelaAtualizar.setVisible(true);
    }

    private void atualizarValores(String chave) throws URISyntaxException, IOException, InterruptedException {

        if (chave.length() != 32 || chave.isBlank()) {
            throw new ConnectException("Chave API inválida.");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://openexchangerates.org/api/latest.json?symbols=BRL,USD,EUR,GBP,ARS,CLP"))
                .header("Authorization", String.format(" Token %s", chave))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();

        if (statusCode == 401) {
            throw new ConnectException("Chave API inválida.");
        } else if (statusCode == 429) {
            throw new ConnectException("Você não tem permissão para acessar essa rota.");
        } else if (statusCode == 404) {
            throw new ConnectException("Erro de conexão.");
        }else if (statusCode > 400) {
            throw new ConnectException("Erro. Por favor, tente mais tarde.");
        }

        JSONObject jsonBody = new JSONObject(response.body());
        jsonBody = jsonBody.getJSONObject("rates");

        valoresTaxaMoedasEmDolar = new HashMap<>();

        valoresTaxaMoedasEmDolar.put("Real", new BigDecimal(jsonBody.get("BRL").toString()));
        valoresTaxaMoedasEmDolar.put("Dolar", new BigDecimal("1.0"));
        valoresTaxaMoedasEmDolar.put("Euro", new BigDecimal(jsonBody.get("EUR").toString()));
        valoresTaxaMoedasEmDolar.put("Libra Esterlina", new BigDecimal(jsonBody.get("GBP").toString()));
        valoresTaxaMoedasEmDolar.put("Peso Argentino", new BigDecimal(jsonBody.get("ARS").toString()));
        valoresTaxaMoedasEmDolar.put("Peso Chileno", new BigDecimal(jsonBody.get("CLP").toString()));

        LocalDate dataDeAgora = LocalDate.now();
        String dataDeAgoraFormatado = dataDeAgora.format(DateTimeFormatter.ofPattern("dd/MM/yyy"));
        dataTaxaCambio.setText(String.format("Data valores: %s", dataDeAgoraFormatado));
    }
}


class ConversorDeTemperatura extends Conversor {

    private HashMap<String, String> temperaturas;

    ConversorDeTemperatura() {
        super.setTitle("Temperatura");

        primeiraCaixaSelecao.setSelectedItem("Celsius");
        segundaCaixaSelecao.setSelectedItem("Fahrenheit");
    }

    @Override
    protected String[] getSelecao() {

        temperaturas = new HashMap<>();
        temperaturas.put("Celsius", "°C");
        temperaturas.put("Fahrenheit", "°F");
        temperaturas.put("Newton", "°N");
        temperaturas.put("Delisle", "°D");
        temperaturas.put("Kelvin", "K");
        temperaturas.put("Réamur", "°Re");
        temperaturas.put("Rankine", "°R");
        temperaturas.put("Romer", "°Rø");

        String[] nomesTemperaturas = new String[8];

        int indice = 0;

        for (String temperatura : temperaturas.keySet()) {
            nomesTemperaturas[indice] = temperatura;
            indice++;
        }
        return nomesTemperaturas;
    }

    @Override
    protected void converter() {
        String valorPrimeiraCaixa = Objects.requireNonNull(primeiraCaixaSelecao.getSelectedItem()).toString();
        String valorSegundaCaixa = Objects.requireNonNull(segundaCaixaSelecao.getSelectedItem()).toString();

        Double valor;
        Double valorCalculado;

        try {
            String tempValor = entradaDoUsuario.getText().replace(".", "_");
            tempValor = tempValor.replace(",", ".");
            valor = Double.parseDouble(tempValor) > 0 ? Double.parseDouble(tempValor) : 0;
            super.mensagemErro.setText("");
            valorCalculado = getCalculoTemperatura(valorPrimeiraCaixa, valorSegundaCaixa, valor);
        } catch (NullPointerException | NumberFormatException | ArithmeticException e) {
            valorCalculado = 0D;
            super.mensagemErro.setText("Valor inválido.");
        }

        String valorFormatado = String.format("%,.2f %s", valorCalculado, temperaturas.get(valorSegundaCaixa));

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
        super.setTitle("Distância");
        primeiraCaixaSelecao.setSelectedItem("Metros");
        segundaCaixaSelecao.setSelectedItem("Quilômetros");
    }

    @Override
    protected String[] getSelecao() {
        return new String[]{"Quilômetros", "Metros", "Milhas", "Centímetros", "Milímetros", "Pés", "Anos-luz",  "Léguas", "Milhas"};
    }

    @Override
    protected void converter() {
        String valorPrimeiraCaixa = Objects.requireNonNull(primeiraCaixaSelecao.getSelectedItem()).toString();
        String valorSegundaCaixa = Objects.requireNonNull(segundaCaixaSelecao.getSelectedItem()).toString();

        double valor;
        String valorCalculado;

        try {
            String tempValor = entradaDoUsuario.getText().replace(".", "_");
            tempValor = tempValor.replace(",", ".");
            valor = Double.parseDouble(tempValor) > 0 ? Double.parseDouble(tempValor) : 0;
            valorCalculado = getCalculoDistancia(valorPrimeiraCaixa, valorSegundaCaixa, valor);
            super.mensagemErro.setText("");
        } catch (NullPointerException | NumberFormatException | ArithmeticException e) {
            valorCalculado = "0.0";
            super.mensagemErro.setText("Valor inválido.");
        }

        String valorFormatado;

        if (valorCalculado.contains("E")) {
            valorFormatado = String.format("%s %s", valorCalculado, valorSegundaCaixa);
        } else {
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

        return resultadoAnosLuz.toEngineeringString();
    }
}