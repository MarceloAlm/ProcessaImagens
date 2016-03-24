package modulos.preProcessamento.ProcessadoresImagem;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import modulos.preProcessamento.TiposProcessadoresImagem.Filtros;


public class EscalaCinza extends Filtros {
	private final String versaoProcessador = "1.0";

	public EscalaCinza(BufferedImage imagem) {
		this.imagemOriginal = imagem;
	}

	private void ProcessaImagem(BufferedImage imagem) {
		this.IniciarProcesso();
		resultadoProcesso = new ResultadoPreProcessamento(this.getClass().toString(), versaoProcessador);
		this.imagemOriginal = imagem;

		this.imagemProcessada = new BufferedImage(imagem.getWidth(), imagem.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = this.imagemProcessada.getGraphics();
		g.drawImage(imagem, 0, 0, null);
		g.dispose();

		resultadoProcesso.setTempoProcessador(this.TerminarProcesso());
	}

	@Override
	public BufferedImage Processar() {
		this.ProcessaImagem(this.imagemOriginal);

		return this.imagemProcessada;
	}

}
