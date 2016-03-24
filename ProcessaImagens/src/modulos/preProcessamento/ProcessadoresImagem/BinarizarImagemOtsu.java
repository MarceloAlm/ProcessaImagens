package modulos.preProcessamento.ProcessadoresImagem;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import modulos.ExceptionTipoImagemInvalido;
import modulos.preProcessamento.TiposProcessadoresImagem.Binarizacao;

public class BinarizarImagemOtsu extends Binarizacao {
	private final String versaoProcessador = "1.0";

	public BinarizarImagemOtsu(BufferedImage imagem) throws ExceptionTipoImagemInvalido {
		super(imagem);
	}

	private void ProcessaImagem(BufferedImage imagem) {
		this.IniciarProcesso();
		resultadoProcesso = new ResultadoPreProcessamento(this.getClass().toString(), versaoProcessador);
		BufferedImage imagemProcessada = new BufferedImage(imagem.getWidth(), imagem.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		WritableRaster raster = imagemProcessada.getRaster();

		if (imagem.getType() == BufferedImage.TYPE_BYTE_GRAY) {

			// Vetor que armazena os hitogramas
			float[][] vetorHistograma = CalcularHistograma(imagem);
			int iLimiar = (int)calculaLimiarOtsu(vetorHistograma[iCINZA], imagem.getWidth() * imagem.getHeight());

			for (int y = 0; y < imagem.getHeight(); y++) {
				for (int x = 0; x < imagem.getWidth(); x++) {
					if (imagem.getRaster().getSample(x, y, iCINZA) >= iLimiar) {
						raster.setSample(x, y, iCINZA, 255);
					} else {
						raster.setSample(x, y, iCINZA, 0);
					}
				}
			}
		}
		this.imagemProcessada = imagemProcessada;
		resultadoProcesso.setTempoProcessador(this.TerminarProcesso());
		resultadoProcesso.setImagemProcessada(this.imagemProcessada);
	}

	public float calculaLimiarOtsu(float[] histogram, int total) {
		float sum = 0;
		for (int i = 0; i < 256; i++)
			sum += i * (histogram[i] * total);

		float sumB = 0;
		int wB = 0;
		int wF = 0;

		float varMax = 0;
		int threshold = 0;

		for (int i = 0; i < 256; i++) {
			wB += (histogram[i] * total);
			if (wB == 0) continue;
			wF = total - wB;

			if (wF == 0) break;

			sumB += (float)(i * (histogram[i] * total));
			float mB = sumB / wB;
			float mF = (sum - sumB) / wF;

			float varBetween = (float)((float)wB * (float)wF * Math.pow((mB - mF), 2));

			if (varBetween > varMax) {
				varMax = varBetween;
				threshold = i;
			}
		}
		return threshold;
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

//if (imagem.getType() == BufferedImage.TYPE_BYTE_GRAY) {
//
//	// Vetor que armazena os hitogramas
//	float[][] vetorHistograma = CalcularHistograma(imagem);
//	float[] vetorNovoHistograma = new float[256];
//
//	int iLimiar = 0;
//	int hmax = 0, ymax = 0;
//
//	float fLimiar = calculaLimiarOtsu(vetorHistograma[iCINZA], imagem.getWidth() * imagem.getHeight());
//
//	// calculo do maior valor do vetor h
//	float max = 0;
//	for (int i = 0; i < 255; i++) {
//		if (vetorHistograma[iCINZA][i] > max) {
//			max = vetorHistograma[iCINZA][i];
//			hmax = i;
//		}
//	}
//
//	// calculo do novo vetor y
//	for (int i = 0; i < 255; i++) {
//		vetorNovoHistograma[i] = (float)(Math.pow((i - hmax), 2) * vetorHistograma[iCINZA][i]);
//	}
//
//	// calculo do maior valor do vetor y
//	max = 0;
//	for (int i = 0; i < 255; i++) {
//		if (vetorNovoHistograma[i] > max) {
//			max = vetorNovoHistograma[i];
//			ymax = i;
//		}
//	}
//
//	// calculo do limiar
//	iLimiar = Math.round(hmax + ymax) / 2;
//	//
//	iLimiar = (int)fLimiar;
//	for (int y = 0; y < imagem.getHeight(); y++) {
//		for (int x = 0; x < imagem.getWidth(); x++) {
//			if (imagem.getRaster().getSample(x, y, iCINZA) >= iLimiar) {
//				raster.setSample(x, y, iCINZA, 255);
//			} else {
//				raster.setSample(x, y, iCINZA, 0);
//			}
//		}
//	}
//}

