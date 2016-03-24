package modulos.preProcessamento;

import java.awt.image.BufferedImage;

public abstract class PreProcessamentoImagens extends PreProcessamento {
	public PreProcessamentoImagens() {
		super();
	}

	@Override
	protected abstract BufferedImage processarImagem(BufferedImage Imagem) throws Exception;

	@Override
	public BufferedImage processar(BufferedImage Imagem, String CaminhoGravarImagens) throws Exception {
		GravarImagens = (CaminhoGravarImagens != null);
		if (GravarImagens) {
			CaminhoImagens = CaminhoGravarImagens;
			new java.io.File(CaminhoImagens).mkdirs();
		}

		return processarImagem(Imagem);
	}

	@Override
	public BufferedImage processar(BufferedImage Imagem) throws Exception {
		return processar(Imagem, null);
	}

}
