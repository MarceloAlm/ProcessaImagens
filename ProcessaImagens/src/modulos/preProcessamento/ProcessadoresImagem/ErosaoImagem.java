package modulos.preProcessamento.ProcessadoresImagem;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import modulos.ExceptionTipoImagemInvalido;
import modulos.preProcessamento.TiposProcessadoresImagem.OperadoresMorfologicos;

public class ErosaoImagem extends OperadoresMorfologicos {

	public ErosaoImagem(BufferedImage imagem, boolean[][] Mascara) throws ExceptionTipoImagemInvalido {
		super(imagem, Mascara);
	}

	@Override
	protected final void ProcessarPonto(final int x, final int y, final WritableRaster raster, final Raster in) {
		boolean setPixel = true;
		for (int i = 0; i < Mascara.length; i++) {
			for (int j = 0; j < Mascara[0].length; j++) {
				if (Mascara[i][j] == true) {
					in.getPixel(i + x + 1 - CentroHorizontal, j + y + 1 - CentroVertical, pixel);
					if (pixel[0] == 0) {
						setPixel = false;
						break;
					}
				}
			}
		}

		if (setPixel) {
			pixel[0] = pixel[1] = pixel[2] = 255;
			raster.setPixel(x, y, pixel);
		}
	}

}
