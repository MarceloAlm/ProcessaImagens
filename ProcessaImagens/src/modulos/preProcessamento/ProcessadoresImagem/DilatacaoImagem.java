package modulos.preProcessamento.ProcessadoresImagem;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import modulos.ExceptionTipoImagemInvalido;
import modulos.preProcessamento.TiposProcessadoresImagem.OperadoresMorfologicos;

public class DilatacaoImagem extends OperadoresMorfologicos {
	public DilatacaoImagem(BufferedImage imagem, boolean[][] Mascara) throws ExceptionTipoImagemInvalido {
		super(imagem, Mascara);
	}

	@Override
	protected final void ProcessarPonto(final int x, final int y, final WritableRaster raster, final Raster in) {
		for (int i = 0; i < Mascara.length; i++) {
			for (int j = 0; j < Mascara[0].length; j++) {
				if (Mascara[i][j] == true) {
					if (in.getSample(i + x + 1 - CentroHorizontal, j + y + 1 - CentroVertical, iCINZA) != 0) {
						raster.setSample(x, y, iCINZA, 255);
						break;
					}
				}
			}
		}
	}

}
