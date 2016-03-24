package modulos.preProcessamento;

import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class PreProcessamento {
	int Passo = 0;
	String CaminhoImagens;
	String PrefixoImagens;
	protected boolean GravarImagens;
	protected boolean GravarArquivosExtras;

	public String getPrefixoImagens() {
		return PrefixoImagens;
	}

	public void setPrefixoImagens(String prefixoImagens) {
		PrefixoImagens = prefixoImagens;
	}

	public String getCaminho() {
		return CaminhoImagens;
	}

	public void setCaminho(String caminho) {
		CaminhoImagens = caminho;
	}

	public boolean getGravarArquivosExtras() {
		return GravarArquivosExtras;
	}

	public void setGravarArquivosExtras(boolean gravarArquivosExtras) {
		GravarArquivosExtras = gravarArquivosExtras;
	}

	public PreProcessamento() {
		CaminhoImagens = System.getProperty("user.dir") + "/imagens/" + new SimpleDateFormat("yyyyMMdd-HHmm").format(new Date()) + "/";
		PrefixoImagens = this.getClass().getSimpleName();
		GravarImagens = false;
	}

	protected abstract Object processarImagem(BufferedImage Imagem) throws Exception;

	public Object processar(BufferedImage Imagem, String CaminhoGravarImagens) throws Exception {
		GravarImagens = (CaminhoGravarImagens != null);
		if (GravarImagens) {
			CaminhoImagens = CaminhoGravarImagens;
			new java.io.File(CaminhoImagens).mkdirs();
		}

		return processarImagem(Imagem);
	}

	public Object processar(BufferedImage Imagem) throws Exception {
		return processar(Imagem, null);
	}

}
