/**
 * 
 */
package modulos.preProcessamento.ProcessadoresImagem;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import modulos.preProcessamento.TiposProcessadoresImagem.Transformacoes;


/**
 * Filtro para equalização do histograma da imagem
 * 
 * @author Marcelo de Almeida
 */
public class EqualizadorHistograma extends Transformacoes {
	private final String versaoProcessador = "1.0";

	/**
	 * Filtro para equalização do histograma da imagem
	 */
	public EqualizadorHistograma(BufferedImage imagem) {
		this.imagemOriginal = imagem;
	}

	private void ProcessaImagem(BufferedImage imagem) {
		this.IniciarProcesso();
		resultadoProcesso = new ResultadoPreProcessamento(this.getClass().toString(), versaoProcessador);

		WritableRaster raster = imagem.getRaster();

		// Vetor que armazena os hitogramas
		float[][] vetorHistograma = CalcularHistograma(imagem);
		// Vetor que armazena os novos valores para cada cor
		int[][] vetorConversao = new int[3][256];

		// Calculo do novo vetor histograma
		for (int i = 0; i < 255; i++) {
			float[] sum = new float[3];
			for (int j = 0; j < i; j++) {
				if (imagem.getType() == BufferedImage.TYPE_BYTE_GRAY) {
					sum[iCINZA] = sum[iCINZA] + vetorHistograma[iCINZA][j];
				} else {
					sum[iVERMELHO] = sum[iVERMELHO] + vetorHistograma[iVERMELHO][j];
					sum[iVERDE] = sum[iVERDE] + vetorHistograma[iVERDE][j];
					sum[iAZUL] = sum[iAZUL] + vetorHistograma[iAZUL][j];
				}
			}
			if (imagem.getType() == BufferedImage.TYPE_BYTE_GRAY) {
				vetorConversao[iCINZA][i] = Math.round(sum[iCINZA] * 255);
			} else {
				vetorConversao[iVERMELHO][i] = Math.round(sum[iVERMELHO] * 255);
				vetorConversao[iVERDE][i] = Math.round(sum[iVERDE] * 255);
				vetorConversao[iAZUL][i] = Math.round(sum[iAZUL] * 255);
			}
		}

		for (int y = 0; y < imagem.getHeight(); y++) {
			for (int x = 0; x < imagem.getWidth(); x++) {
				if (imagem.getType() == BufferedImage.TYPE_BYTE_GRAY) {
					raster.setSample(x, y, iCINZA, vetorConversao[iCINZA][raster.getSample(x, y, iCINZA)]);
				} else {
					raster.setSample(x, y, iVERMELHO, vetorConversao[iVERMELHO][raster.getSample(x, y, iVERMELHO)]);
					raster.setSample(x, y, iVERDE, vetorConversao[iVERDE][raster.getSample(x, y, iVERDE)]);
					raster.setSample(x, y, iAZUL, vetorConversao[iAZUL][raster.getSample(x, y, iAZUL)]);
				}
			}
		}

		this.imagemProcessada = imagem;
		resultadoProcesso.setTempoProcessador(this.TerminarProcesso());
		resultadoProcesso.setImagemProcessada(this.imagemProcessada);
	}

	@Override
	public BufferedImage Processar() {
		this.ProcessaImagem(this.imagemOriginal);

		return this.getImagemProcessada();
	}
}
