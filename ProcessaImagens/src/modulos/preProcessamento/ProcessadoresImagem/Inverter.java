package modulos.preProcessamento.ProcessadoresImagem;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import modulos.preProcessamento.TiposProcessadoresImagem.Filtros;

public class Inverter extends Filtros {
	private final String versaoProcessador = "1.0";

	private void ProcessaImagem(BufferedImage imagem) {
		this.IniciarProcesso();
		resultadoProcesso = new ResultadoPreProcessamento(this.getClass().toString(), versaoProcessador);
		this.imagemOriginal = imagem;
		BufferedImage imagemProcessada = new BufferedImage(imagem.getWidth(), imagem.getHeight(), imagem.getType());

		Raster rasterOriginal = imagem.getRaster();
		WritableRaster rasterProcessada = imagemProcessada.getRaster();

		switch (imagemOriginal.getType()) {
			case BufferedImage.TYPE_BYTE_GRAY: {
				for (int y = 0; y < imagem.getHeight(); y++) {
					for (int x = 0; x < imagem.getWidth(); x++) {
						rasterProcessada.setSample(x, y, iCINZA, Math.abs(rasterOriginal.getSample(x, y, iCINZA) - 255));
					}
				}
			}
			case BufferedImage.TYPE_BYTE_BINARY: {
				this.imagemProcessada = new BufferedImage(imagem.getWidth(), imagem.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
				rasterProcessada = this.imagemProcessada.getRaster();

				for (int y = 0; y < imagem.getHeight(); y++) {
					for (int x = 0; x < imagem.getWidth(); x++) {
						rasterProcessada.setSample(x, y, iCINZA, Math.abs((rasterOriginal.getSample(x, y, iCINZA) - 1) * 255));
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
}
