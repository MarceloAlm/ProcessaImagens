package modulos.preProcessamento.TiposProcessadoresImagem;

import java.awt.image.BufferedImage;

import modulos.ExceptionTipoImagemInvalido;

public abstract class Transformacoes extends ProcessadorImagem {
	@Override
	public BufferedImage Processar(BufferedImage NovaImagem) throws ExceptionTipoImagemInvalido {
		this.imagemOriginal = NovaImagem;

		return this.Processar();
	}
}
