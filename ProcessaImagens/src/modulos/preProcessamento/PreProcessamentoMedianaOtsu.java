package modulos.preProcessamento;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import modulos.ExceptionTipoImagemInvalido;
import modulos.preProcessamento.ProcessadoresImagem.BinarizarImagemOtsu;
import modulos.preProcessamento.ProcessadoresImagem.EscalaCinza;
import modulos.preProcessamento.ProcessadoresImagem.ExtrairCanalCor;
import modulos.preProcessamento.TiposProcessadoresImagem.Filtros;
import modulos.preProcessamento.TiposProcessadoresImagem.ProcessadorImagem;

public class PreProcessamentoMedianaOtsu extends PreProcessamentoImagens {

	public PreProcessamentoMedianaOtsu() {
		super();
	}

	@Override
	protected BufferedImage processarImagem(BufferedImage Imagem) throws IOException, ExceptionTipoImagemInvalido {
		BufferedImage ImagemProcessada = Imagem;
		if (GravarImagens) ImageIO.write(Imagem, "jpeg", new File(CaminhoImagens + PrefixoImagens + "_passo" + String.format("%02d", ++Passo) + "_Original.jpg"));

		EscalaCinza piEscalaCinza = new EscalaCinza(ImagemProcessada);
		ImagemProcessada = piEscalaCinza.Processar();
		if (GravarImagens) ImageIO.write(piEscalaCinza.getImagemProcessada(), "jpeg", new File(CaminhoImagens + PrefixoImagens + "_passo" + String.format("%02d", ++Passo) + "_EscalaCinza.jpg"));

		if (GravarImagens && GravarArquivosExtras) {
			/* Extrai canais de cor da imagem */
			ExtrairCanalCor piExtrairCanalVermelho = new ExtrairCanalCor(Imagem, ProcessadorImagem.iVERMELHO);
			piExtrairCanalVermelho.Processar();
			ImageIO.write(piExtrairCanalVermelho.getImagemProcessada(), "jpeg", new File(CaminhoImagens + PrefixoImagens + "_passo" + String.format("%02d", ++Passo) + "_ExtrairCanalVermelho.jpg"));

			ExtrairCanalCor piExtrairCanalVerde = new ExtrairCanalCor(Imagem, ProcessadorImagem.iVERDE);
			piExtrairCanalVerde.Processar();
			ImageIO.write(piExtrairCanalVerde.getImagemProcessada(), "jpeg", new File(CaminhoImagens + PrefixoImagens + "_passo" + String.format("%02d", ++Passo) + "_ExtrairCanalVerde.jpg"));

			ExtrairCanalCor piExtrairCanalAzul = new ExtrairCanalCor(Imagem, ProcessadorImagem.iAZUL);
			piExtrairCanalAzul.Processar();
			ImageIO.write(piExtrairCanalAzul.getImagemProcessada(), "jpeg", new File(CaminhoImagens + PrefixoImagens + "_passo" + String.format("%02d", ++Passo) + "_ExtrairCanalAzul.jpg"));
		}

		/* Filtro Sharp para realce das bordas da imagem */
		ImagemProcessada = Filtros.aplicaFiltroRealce(ImagemProcessada, 0);
		if (GravarImagens) ImageIO.write(ImagemProcessada, "jpeg", new File(CaminhoImagens + PrefixoImagens + "_passo" + String.format("%02d", ++Passo) + "_FiltroSharp.jpg"));

		/* Binarização da imagem */
		BinarizarImagemOtsu piBinarizarImagem = new BinarizarImagemOtsu(ImagemProcessada);
		ImagemProcessada = piBinarizarImagem.Processar();
		if (GravarImagens) ImageIO.write(ImagemProcessada, "jpeg", new File(CaminhoImagens + PrefixoImagens + "_passo" + String.format("%02d", ++Passo) + "_BinarizarGlobal.jpg"));

		//		ImagemProcessada = Filtros.aplicaFiltroMediana(ImagemProcessada);
		//		if (GravarImagens) ImageIO.write(ImagemProcessada, "jpeg", new File(CaminhoImagens + PrefixoImagens + "_passo" + String.format("%02d", ++Passo) + "_FiltroMediana.jpg"));
		//		ImagemProcessada = Binarizacao.retornaImagemBinarizada(ImagemProcessada, 128);

		return ImagemProcessada;
	}
}
