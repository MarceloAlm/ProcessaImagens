/**
 * 
 */
package modulos.preProcessamento.ProcessadoresImagem;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

import modulos.ExceptionTipoImagemInvalido;
import modulos.preProcessamento.TiposProcessadoresImagem.Binarizacao;

/**
 * Filtro para equalização do histograma da imagem
 * 
 * @author Marcelo de Almeida
 */
public class BinarizarImagemNiblack extends Binarizacao {
	private final String versaoProcessador = "1.0";
	private final double fPeso;
	private final int iJanela;
	private final boolean GravarArquivosExtras;
	private String CaminhoArquivosExtras;

	public BinarizarImagemNiblack(BufferedImage imagem, int Janela, double Peso) throws ExceptionTipoImagemInvalido {
		super(imagem);

		iJanela = Janela;
		fPeso = Peso;
		GravarArquivosExtras = false;
	}

	public BinarizarImagemNiblack(BufferedImage imagem, int Janela, double Peso, String GerarArquivosExtras) throws ExceptionTipoImagemInvalido {
		super(imagem);

		iJanela = Janela;
		fPeso = Peso;

		GravarArquivosExtras = (GerarArquivosExtras != null);
		if (GravarArquivosExtras) {
			CaminhoArquivosExtras = GerarArquivosExtras;
			new java.io.File(CaminhoArquivosExtras).mkdirs();
		}
	}

	private void ProcessaImagem(BufferedImage imagem) {
		this.IniciarProcesso();
		resultadoProcesso = new ResultadoPreProcessamento(this.getClass().toString(), versaoProcessador);
		BufferedImage imagemProcessada = new BufferedImage(imagem.getWidth(), imagem.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		WritableRaster raster = imagemProcessada.getRaster();

		if (imagem.getType() == BufferedImage.TYPE_BYTE_GRAY) {

			if (GravarArquivosExtras) try {
				File ArquivoResultados = new File(CaminhoArquivosExtras + "/" + this.getClass().getSimpleName() + ".txt");
				FileWriter Arquivo = new FileWriter(ArquivoResultados);
				PrintWriter gravarArquivo = new PrintWriter(Arquivo);

				gravarArquivo.println("Segmento;Media;Variancia;Desvio Padrao;Limiar");
				gravarArquivo.flush();
				gravarArquivo.close();
				Arquivo.close();
			} catch (IOException ex1) {
				ex1.printStackTrace();
			}

			for (int Y = 0; Y < imagem.getHeight(); Y += iJanela) {
				for (int X = 0; X < imagem.getWidth(); X += iJanela) {
					BufferedImage subImagem = imagem.getSubimage(X, Y, ((X + iJanela >= imagem.getWidth()) ? imagem.getWidth() - X : iJanela), ((Y + iJanela >= imagem.getHeight()) ? imagem.getHeight() - Y : iJanela));

					float mediaImagem = CalcularMedia(subImagem);
					float desvioPadrao = CalcularDesvioPadrao(subImagem);

					double iLimiar = mediaImagem + (fPeso * desvioPadrao);

					for (int y = 0; y < subImagem.getHeight(); y++) {
						for (int x = 0; x < subImagem.getWidth(); x++) {

							if (imagem.getRaster().getSample(x + X, y + Y, iCINZA) >= iLimiar) {
								raster.setSample(x + X, y + Y, iCINZA, 255);
							} else {
								raster.setSample(x + X, y + Y, iCINZA, 0);
							}
						}
					}
					if (GravarArquivosExtras) try {
						float varianciaImagem = CalcularVariancia(subImagem);

						ImageIO.write(subImagem, "jpeg", new File(CaminhoArquivosExtras + "/" + this.getClass().getSimpleName() + "_Janela" + String.format("(%03d %03d)", X, Y) + "_Original.jpg"));
						ImageIO.write(imagemProcessada.getSubimage(X, Y, ((X + iJanela >= imagem.getWidth()) ? imagem.getWidth() - X : iJanela), ((Y + iJanela >= imagem.getHeight()) ? imagem.getHeight() - Y : iJanela)), "jpeg", new File(CaminhoArquivosExtras + "/" + this.getClass().getSimpleName() + "_Janela" + String.format("(%03d %03d)", X, Y) + ".jpg"));

						File ArquivoResultados = new File(CaminhoArquivosExtras + "/" + this.getClass().getSimpleName() + ".txt");
						FileWriter arq = new FileWriter(ArquivoResultados, true);
						PrintWriter gravarArq = new PrintWriter(arq);
						gravarArq.printf(" %03d %03d; %.2f; %.2f; %.2f; %.2f %n", X, Y, mediaImagem, varianciaImagem, desvioPadrao, iLimiar);
						gravarArq.flush();
						gravarArq.close();
						arq.close();

					} catch (IOException ex) {
						ex.printStackTrace();
					}

				}
			}
		}

		this.imagemProcessada = imagemProcessada;
		resultadoProcesso.setTempoProcessador(this.TerminarProcesso());
		resultadoProcesso.setImagemProcessada(this.imagemProcessada);
	}

	@Override
	public BufferedImage Processar() {
		this.ProcessaImagem(this.imagemOriginal);

		return this.imagemProcessada;
	}

	@Override
	public BufferedImage Processar(BufferedImage NovaImagem) throws ExceptionTipoImagemInvalido {
		if (NovaImagem.getType() != BufferedImage.TYPE_BYTE_GRAY) throw new ExceptionTipoImagemInvalido("Imagem deve estar em escala de cinza");
		this.imagemOriginal = NovaImagem;
		this.ProcessaImagem(this.imagemOriginal);

		return this.imagemProcessada;
	}
}
