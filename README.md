![alura-logo](https://user-images.githubusercontent.com/107167711/226222830-db2f671b-3e9f-4bd5-bb1e-f339a85abe3a.png)
# Edição #4: Challenge ONE Back End - Sprint 01
Projeto para o Desafio Challenge ONE Back End.

# Introdução

Conversor de moedas, temperatura, distância e anos-luz, desenvolvido para o primeiro sprint do **Challenge One Back End**, aonde foi proposto um desafio em que tem que ser desenvolvido um projeto de Conversor de Moedas utilizando o **Java** e o seu kit de componentes GUI chamado **Swing**.

![image](https://user-images.githubusercontent.com/107167711/228851651-0307590f-5107-4dfe-bb6f-8309fec8ca6a.png)

# Tipos de Conversões

**Moeda**:
Real, Dolar, Euro, Libra Esterlina, Peso Argentino e Peso Chileno.

**Temperatura**:
Celsius, Fahrenheit, Newton, Delisle, Kelvin, Réaumur, Rankine e Romer.

**Distância**:
Quilômentro, Metro, Centímetro, Milímetro, Milha, Pé, Ano-luz e Légua.

## Atualizando os valores de câmbio
Clicando no botão do canto superior direito, agora é possível atualizar a taxa de câmbio com os valores atuais usando a chave da API [Open Exchange Rates](https://openexchangerates.org/).

É usado as classes `HttpRequest`, `HttpClient` e `HttpResponse` do pacote `java.net` do próprio Java para fazer a requisição e a biblioteca externa `org.json` para lidar com o formato JSON.

![image](https://user-images.githubusercontent.com/107167711/228851496-7c76fe97-f97c-4850-9393-1fa9c1365c01.png)

# Sobre o Projeto
Todos os nomes de váriaveis, classes, etc... são em português para facilitar o entendimento dos alunos participantes da ONE Oracle, não incluindo os nomes que se encontram na biblioteca padrão do Java. Já que é apenas um projeto para estudo, eu preferi prezar para a acessibilidade.
Ele foi desenvolvido pensando ao máximo sobre em seguir os padrões de Orientação a Objetos e o kit Swing ajuda bastante nisso, mas, caso tiver sugestões, sinta-se livre em abrir uma issue ou até mesmo me contatar.

O projeto usa o [Maven](https://maven.apache.org/) para gerenciar "builds" de projetos. Com ele é possível configurar um ambiente de desenvolvimento padronizado seguindo boas práticas, permitindo compilação, gerência de dependências e distribuição de uma aplicação editando apenas um arquivo e utilizando conceitos de convenção sobre configuração.

# Requisitos
- JDK 17.0.5
- Maven 4.0

# Referências
[Java GUI: Full Course ☕ (FREE)](https://youtu.be/Kmgo00avvEw)

[Swing Package Summary](https://docs.oracle.com/javase/7/docs/api/javax/swing/package-summary.html)

[JFC F.A.Q](https://www.oracle.com/java/technologies/foundation-classes-faq.html)

[Flatlaf Client Properties](https://www.formdev.com/flatlaf/client-properties/)

[How to Use JTabbed Panes - Oracle](https://docs.oracle.com/javase/tutorial/uiswing/components/tabbedpane.html)

[java swing JTextField set PlaceHolder](https://stackoverflow.com/questions/16213836/java-swing-jtextfield-set-placeholder)

[How To Call a REST API In Java - Simple Tutorial](https://youtu.be/9oq7Y8n1t00)

[Docs Open Exchange rates](https://docs.openexchangerates.org/reference/api-introduction)

[What data type to use for money in Java?](https://stackoverflow.com/questions/8148684/what-data-type-to-use-for-money-in-java)

# Atribuições
[FlatLaf - Flat Look and Feel](https://www.formdev.com/flatlaf/) - FlatLaf é um Look and Feel moderno multiplataforma de código aberto para aplicativos de desktop Java Swing.

[Maan Icons](https://www.flaticon.com/br/autores/maan-icons) - Icone usado para a janela.

[Freepik](https://www.flaticon.com/br/autores/freepik) - Icone usado para o botão de atualizar os valores na aba de Moedas.

[org.json](https://mvnrepository.com/artifact/org.json/json) - Biblioteca de Encoder/Decoder leve de JSON para Java.

[Open Exchange Rates](https://openexchangerates.org/) - API para consultas de taxas de câmbio entre moedas.
