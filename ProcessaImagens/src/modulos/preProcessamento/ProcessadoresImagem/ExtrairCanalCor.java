package modulos.preProcessamento.ProcessadoresImagem;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import modulos.preProcessamento.TiposProcessadoresImagem.Filtros;

public class ExtrairCanalCor extends Filtros {
	private final String versaoProcessador = "1.0";
	private final int canalCor;

	public ExtrairCanalCor(BufferedImage imagem, int Canal) {
		this.imagemOriginal = imagem;
		this.canalCor = Canal;
	}

	private void ProcessaImagem(BufferedImage imagem) {
		this.IniciarProcesso();
		resultadoProcesso = new ResultadoPreProcessamento(this.getClass().toString(), versaoProcessador);
		this.imagemOriginal = imagem;
		//this.imagemProcessada = new BufferedImage(imagem.getWidth(), imagem.getHeight(), imagem.getType());
		this.imagemProcessada = new BufferedImage(imagem.getWidth(), imagem.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		WritableRaster rasterOriginal = imagem.getRaster();
		WritableRaster rasterProcessada = this.imagemProcessada.getRaster();

		for (int y = 0; y < imagem.getHeight(); y++) {
			for (int x = 0; x < imagem.getWidth(); x++) {
				//rasterProcessada.setSample(x, y, this.canalCor, rasterOriginal.getSample(x, y, this.canalCor));
				rasterProcessada.setSample(x, y, iCINZA, rasterOriginal.getSample(x, y, this.canalCor));
			}
		}

		resultadoProcesso.setTempoProcessador(this.TerminarProcesso());
	}

	@Override
	public BufferedImage Processar() {
		this.ProcessaImagem(this.imagemOriginal);

		return this.getImagemProcessada();
	}
}
