package modulos.preProcessamento.TiposProcessadoresImagem;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import modulos.ExceptionTipoImagemInvalido;

public abstract class Binarizacao extends ProcessadorImagem {

	protected Binarizacao(BufferedImage imagem) throws ExceptionTipoImagemInvalido {
		if (imagem.getType() != BufferedImage.TYPE_BYTE_GRAY) throw new ExceptionTipoImagemInvalido("Imagem deve estar em escala de cinza");
		this.imagemOriginal = imagem;
	}

	public static BufferedImage retornaImagemBinarizada(BufferedImage imagem, int valorLimiar) {
		BufferedImage imagemProcessada = new BufferedImage(imagem.getWidth(), imagem.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		WritableRaster raster = imagemProcessada.getRaster();

		if (imagem.getType() == BufferedImage.TYPE_BYTE_GRAY) {
			for (int y = 0; y < imagem.getHeight(); y++) {
				for (int x = 0; x < imagem.getWidth(); x++) {
					if (imagem.getRaster().getSample(x, y, iCINZA) >= valorLimiar) {
						raster.setSample(x, y, iCINZA, 255);
					} else {
						raster.setSample(x, y, iCINZA, 0);
					}
				}
			}
		}
		return imagemProcessada;
	}
}
