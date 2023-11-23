# Projeto Simple OpenIA Application
Este projeto é uma aplicação Spring Boot que demonstra a integração de OCR (Reconhecimento Óptico de Caracteres), processamento de PDF e a utilização de serviços da API OpenAI para gerar respostas e imagens baseadas em texto extraído.

## Funcionalidades
* Extração de texto de arquivos PDF.
* Reconhecimento de texto em imagens (OCR) utilizando Tesseract.
* Interação com a API OpenAI para gerar respostas e imagens a partir de textos extraídos.

## Tecnologias Utilizadas
* Spring Boot
* Tesseract para OCR
* OpenAI API
* Apache PDFBox para processamento de PDFs

## Serviços
### OCRService
Responsável pela extração de texto de imagens usando Tesseract.

### PDFService
Lida com a extração de texto de arquivos PDF utilizando Apache PDFBox.

### OpenIAService
Integra com a API OpenAI, permitindo gerar respostas e imagens baseadas em texto.

## Configuração e Uso
### Pré-Requisitos
* Java 17 ou superior.
* Dependências do Maven incluídas no pom.xml do projeto.

## Configuração
1. Adicione as seguintes variáveis de ambiente ou definições no seu arquivo de propriedades (por exemplo, application.properties):

```
# Geração de API KEY - https://platform.openai.com/api-keys
openai.api.key=SUA_API_KEY

# Ação que o GPT deverá executar a partir do 2º prompt ou deixe vazio
openai.action=Resuma todo o conteúdo que irá receber

# Modelo da OpenAI a ser utilizado
# Opções: gpt-4-1106-preview, gpt-3.5-turbo-1106
# Mais modelos disponíveis em https://platform.openai.com/docs/models
openai.model=gpt-4-1106-preview

# Configuração do ITesseract (OCR)
# Linguagem do OCR
tessdata.lang=por

# Caminho para os dados do Tesseract
tessdata.prefix=C:\\Users\\Sassine\\Tesseract-OCR\\tessdata
```

2. Insira os caminhos dos arquivos PDF e de imagem nos métodos apropriados no SimpleOpenIAApplication.java.

## Execução
Execute a aplicação Spring Boot normalmente através da classe SimpleOpenIAApplication