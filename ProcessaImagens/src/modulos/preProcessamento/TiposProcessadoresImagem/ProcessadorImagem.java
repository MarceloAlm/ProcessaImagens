package modulos.preProcessamento.TiposProcessadoresImagem;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import modulos.ExceptionTipoImagemInvalido;

public abstract class ProcessadorImagem {
	public static final int iCINZA = 0;
	public static final int iVERMELHO = 0;
	public static final int iVERDE = 1;
	public static final int iAZUL = 2;

	@SuppressWarnings("unused")
	private static final int iPRETO = 0xFF000000;
	@SuppressWarnings("unused")
	private static final int iBRANCO = 0xFFFFFFFF;

	/* Métodos para marcação do tempo de execução dos processos */
	private long tempoInicioProcesso;
	private long tempoTerminoProcesso;

	protected void IniciarProcesso() {
		tempoInicioProcesso = System.currentTimeMillis();
	}

	protected long TerminarProcesso() {
		tempoTerminoProcesso = System.currentTimeMillis();

		return tempoTerminoProcesso - tempoInicioProcesso;
	}

	/* Funções compartilhadas entre os tipos de processamento */
	protected float[][] CalcularHistograma(BufferedImage imagem) {
		Raster raster = imagem.getRaster();

		// Vetor que armazena as ocorrências de cada cor
		int[][] vetorOcorrencias = new int[3][256];
		// Vetor que armazena os hitogramas
		float[][] vetorHistograma = new float[3][256];
		// Número total de pixels da imagem
		int TotalPixels = 0;

		for (int y = 0; y < imagem.getHeight(); y++) {
			for (int x = 0; x < imagem.getWidth(); x++) {
				if (imagem.getType() == BufferedImage.TYPE_BYTE_GRAY) {
					vetorOcorrencias[iCINZA][raster.getSample(x, y, iCINZA)]++;
				} else {
					vetorOcorrencias[iVERMELHO][raster.getSample(x, y, iVERMELHO)]++;
					vetorOcorrencias[iVERDE][raster.getSample(x, y, iVERDE)]++;
					vetorOcorrencias[iAZUL][raster.getSample(x, y, iAZUL)]++;
				}
				TotalPixels++;
			}
		}

		// Calculo do vetor histograma
		for (int y = 0; y < 255; y++) {
			if (imagem.getType() == BufferedImage.TYPE_BYTE_GRAY) {
				vetorHistograma[iCINZA][y] = (float)vetorOcorrencias[iCINZA][y] / TotalPixels;
			} else {
				vetorHistograma[iVERMELHO][y] = (float)vetorOcorrencias[iVERMELHO][y] / TotalPixels;
				vetorHistograma[iVERDE][y] = (float)vetorOcorrencias[iVERDE][y] / TotalPixels;
				vetorHistograma[iAZUL][y] = (float)vetorOcorrencias[iAZUL][y] / TotalPixels;
			}
		}
		return vetorHistograma;
	}

	protected float[][] CalcularHistograma(BufferedImage imagemOriginal, int X, int Y, int W, int H) {
		BufferedImage imagem = imagemOriginal.getSubimage(X, Y, W, H);
		Raster raster = imagem.getRaster();

		// Vetor que armazena as ocorrências de cada cor
		int[][] vetorOcorrencias = new int[3][256];
		// Vetor que armazena os hitogramas
		float[][] vetorHistograma = new float[3][256];
		// Número total de pixels da imagem
		int TotalPixels = 0;

		for (int y = 0; y < imagem.getHeight(); y++) {
			for (int x = 0; x < imagem.getWidth(); x++) {
				if (imagem.getType() == BufferedImage.TYPE_BYTE_GRAY) {
					vetorOcorrencias[iCINZA][raster.getSample(x, y, iCINZA)]++;
				} else {
					vetorOcorrencias[iVERMELHO][raster.getSample(x, y, iVERMELHO)]++;
					vetorOcorrencias[iVERDE][raster.getSample(x, y, iVERDE)]++;
					vetorOcorrencias[iAZUL][raster.getSample(x, y, iAZUL)]++;
				}
				TotalPixels++;
			}
		}

		// Calculo do vetor histograma
		for (int y = 0; y < 255; y++) {
			if (imagem.getType() == BufferedImage.TYPE_BYTE_GRAY) {
				vetorHistograma[iCINZA][y] = (float)vetorOcorrencias[iCINZA][y] / TotalPixels;
			} else {
				vetorHistograma[iVERMELHO][y] = (float)vetorOcorrencias[iVERMELHO][y] / TotalPixels;
				vetorHistograma[iVERDE][y] = (float)vetorOcorrencias[iVERDE][y] / TotalPixels;
				vetorHistograma[iAZUL][y] = (float)vetorOcorrencias[iAZUL][y] / TotalPixels;
			}
		}
		return vetorHistograma;
	}

	public int[] CalcularFrequencia(BufferedImage imagem) throws ExceptionTipoImagemInvalido {
		if (imagem.getType() != BufferedImage.TYPE_BYTE_GRAY) throw new ExceptionTipoImagemInvalido("Imagem deve estar em escala de cinza");

		Raster raster = imagem.getRaster();

		// Vetor que armazena as ocorrências de cada cor
		int[] vetorOcorrencias = new int[256];

		for (int y = 0; y < imagem.getHeight(); y++) {
			for (int x = 0; x < imagem.getWidth(); x++) {
				vetorOcorrencias[raster.getSample(x, y, iCINZA)]++;
			}
		}
		return vetorOcorrencias;
	}

	protected float CalcularMedia(BufferedImage imagemOriginal, int X, int Y, int W, int H) {
		BufferedImage imagem = imagemOriginal.getSubimage(X, Y, W, H);
		Raster raster = imagem.getRaster();

		float fSomaImagem = 0;
		int fTotalPixels = 0;

		for (int y = 0; y < imagem.getHeight(); y++) {
			for (int x = 0; x < imagem.getWidth(); x++) {
				if (imagem.getType() == BufferedImage.TYPE_BYTE_GRAY) {
					fSomaImagem += raster.getSample(x, y, iCINZA);
					fTotalPixels++;
				}
			}
		}

		return fSomaImagem / fTotalPixels;
	}

	protected float CalcularMedia(BufferedImage imagem) {
		Raster raster = imagem.getRaster();

		float fSomaImagem = 0;
		int fTotalPixels = 0;

		for (int y = 0; y < imagem.getHeight(); y++) {
			for (int x = 0; x < imagem.getWidth(); x++) {
				if (imagem.getType() == BufferedImage.TYPE_BYTE_GRAY) {
					fSomaImagem += raster.getSample(x, y, iCINZA);
					fTotalPixels++;
				}
			}
		}

		return fSomaImagem / fTotalPixels;
	}

	//	protected float CalcularVariancia(BufferedImage imagem) {
	//		float[][] vetorHistograma = CalcularHistograma(imagem);
	//		float MediaImagem = CalcularMedia(imagem);
	//		float VarianciaImagem = 0;
	//
	//		for (int y = 0; y < 255; y++) {
	//			VarianciaImagem += Math.pow((vetorHistograma[iCINZA][y] - MediaImagem), 2);
	//		}
	//		return VarianciaImagem / 256;
	//	}
	//
	//	protected float CalcularVariancia(float[][] vetorHistograma, float MediaImagem) {
	//		float VarianciaImagem = 0;
	//
	//		for (int y = 0; y < 255; y++) {
	//			VarianciaImagem += Math.pow((vetorHistograma[iCINZA][y] - MediaImagem), 2);
	//		}
	//		return VarianciaImagem / 256;
	//	}

	protected float CalcularVariancia(BufferedImage imagem) {
		return CalcularVariancia(imagem, CalcularMedia(imagem));
	}

	protected float CalcularVariancia(BufferedImage imagem, float MediaImagem) {
		float VarianciaImagem = 0;
		int fTotalPixels = 0;
		Raster raster = imagem.getRaster();

		for (int y = 0; y < imagem.getHeight(); y++) {
			for (int x = 0; x < imagem.getWidth(); x++) {
				if (imagem.getType() == BufferedImage.TYPE_BYTE_GRAY) {
					int iPixel = raster.getSample(x, y, iCINZA);
					VarianciaImagem += Math.pow(iPixel - MediaImagem, 2);
					fTotalPixels++;
				}
			}
		}
		return VarianciaImagem / fTotalPixels;

	}

	protected float CalcularDesvioPadrao(BufferedImage imagem) {
		float DesvioPadraoImagem = 0;
		float VarianciaImagem = CalcularVariancia(imagem);

		DesvioPadraoImagem = (float)Math.sqrt(VarianciaImagem);
		return DesvioPadraoImagem;
	}

	protected float CalcularDesvioPadrao(BufferedImage imagem, float MediaImagem) {
		float DesvioPadraoImagem = 0;
		float VarianciaImagem = CalcularVariancia(imagem, MediaImagem);

		DesvioPadraoImagem = (float)Math.sqrt(VarianciaImagem);
		return DesvioPadraoImagem;
	}

	protected float CalcularDesvioPadrao(float VarianciaImagem) {
		float DesvioPadraoImagem = (float)Math.sqrt(VarianciaImagem);

		return DesvioPadraoImagem;
	}

	/* ****************************** */
	/* Métodos para retorno da imagem */
	/* ****************************** */
	protected BufferedImage imagemProcessada;
	protected BufferedImage imagemOriginal;

	/**
	 * Retorna a imagem original enviada para o processador de imagens
	 * 
	 * @return {@link BufferedImage}
	 */
	public BufferedImage getImagemProcessada() {
		return imagemProcessada;
	}

	/**
	 * Retorna a imagem resultante do processador de imagens
	 * 
	 * @return {@link BufferedImage}
	 */
	public BufferedImage getImagemOriginal() {
		return imagemOriginal;
	}

	protected ResultadoPreProcessamento resultadoProcesso;

	/**
	 * Retorna a classe do tipo ResultadoProcessamento
	 * 
	 * @return {@link ProcessadorImagem}
	 */
	public ResultadoPreProcessamento getResultadoProcessamento() {
		//		resultadoProcesso.imagemOriginal = this.imagemOriginal;
		resultadoProcesso.imagemProcessada = this.imagemProcessada;
		return resultadoProcesso;
	}

	/**
	 * Método que executa o processamento das imagens e retorna uma
	 * {@link BufferedImage} com o resultado
	 * 
	 * @return Imagem processada
	 */
	public abstract BufferedImage Processar();

	public abstract BufferedImage Processar(BufferedImage NovaImagem) throws ExceptionTipoImagemInvalido;

	/** Classe para representar os resultados de um processamento de imagem */
	public class ResultadoPreProcessamento {
		private final String nomeProcessador;
		private final String versaoProcessador;
		//		private BufferedImage imagemOriginal;
		private BufferedImage imagemProcessada;
		private long tempoProcessador;

		public ResultadoPreProcessamento(String NomeProcessador, String VersaoProcessador) {
			nomeProcessador = NomeProcessador;
			versaoProcessador = VersaoProcessador;
		}

		public String getNomeProcessador() {
			return nomeProcessador;
		}

		public String getVersaoProcessador() {
			return versaoProcessador;
		}

		public void setImagemOriginal(BufferedImage imagem) {
			imagemOriginal = imagem;
		}

		//		public BufferedImage getImagemOriginal() {
		//			return imagemOriginal;
		//		}

		public void setImagemProcessada(BufferedImage imagem) {
			imagemProcessada = imagem;
		}

		public BufferedImage getImagemProcessada() {
			return imagemProcessada;
		}

		public void setTempoProcessador(long tempo) {
			tempoProcessador = tempo;
		}

		public long getTempoProcessador() {
			return tempoProcessador;
		}
	}
}
