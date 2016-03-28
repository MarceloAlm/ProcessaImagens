package modulos.preProcessamento.TiposProcessadoresImagem;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;
import java.util.Arrays;

import modulos.ExceptionTipoImagemInvalido;

public abstract class Filtros extends ProcessadorImagem {
	@Override
	public BufferedImage Processar(BufferedImage NovaImagem) throws ExceptionTipoImagemInvalido {
		this.imagemOriginal = NovaImagem;

		return this.Processar();
	}

	public static BufferedImage convolucao(BufferedImage imagem, float[] elementos) {
		Kernel kernel = new Kernel(3, 3, elementos);
		ConvolveOp op = new ConvolveOp(kernel);

		BufferedImage imagemProcessada = new BufferedImage(imagem.getWidth(), imagem.getHeight(), imagem.getType());
		op.filter(imagem, imagemProcessada);

		return imagemProcessada;
	}

	public static BufferedImage retornaImagemCinza(BufferedImage imagem) {
		if (imagem.getType() == BufferedImage.TYPE_BYTE_GRAY)
			return imagem;
		BufferedImage imagemProcessada = new BufferedImage(imagem.getWidth(), imagem.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster rasterImagemProcessada = imagemProcessada.getRaster();

		for (int x = 0; x < rasterImagemProcessada.getWidth(); x++) {
			for (int y = 0; y < rasterImagemProcessada.getHeight(); y++) {
				int iRGB = imagem.getRGB(x, y);
				int iIntensidade = (int) (.299 * ((iRGB >> 16) & 0xff) + .587 * ((iRGB >> 8) & 0xff)
						+ .114 * ((iRGB) & 0xff));
				rasterImagemProcessada.setSample(x, y, iCINZA, iIntensidade);
			}
		}

		return imagemProcessada;
	}

	public static BufferedImage aplicaFiltroMediana(BufferedImage imagem) {
		BufferedImage imagemProcessada = new BufferedImage(imagem.getWidth(), imagem.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster rasterImagemProcessada = imagemProcessada.getRaster();

		int n = 3;
		int borda = Math.round(n / 2);
		int mediana = Math.round((n * n) / 2);
		int width = imagem.getWidth();
		int height = imagem.getHeight();
		int[] pixels = new int[n * n];

		for (int x = 0; x <= width - 1; x++) {
			for (int y = 0; y <= height - 1; y++) {
				rasterImagemProcessada.setSample(x, y, 0, imagem.getRGB(x, y) & 0xff);
			}
		}

		for (int x = borda; x <= width - borda - 1; x++) {
			for (int y = borda; y <= height - borda - 1; y++) {
				int count = 0;
				for (int k = (x - borda); k <= (x + borda); k++) {
					for (int l = (y - borda); l <= (y + borda); l++) {
						pixels[count] = (imagem.getRGB(k, l)) & 0xff;
						count++;
					}
				}
				Arrays.sort(pixels);
				rasterImagemProcessada.setSample(x, y, 0, pixels[mediana]);
			}
		}

		return imagemProcessada;
	}

	public static BufferedImage aplicaFiltroRealce(BufferedImage imagem, float fator)
			throws ExceptionTipoImagemInvalido {
		float[] elementos = { 0.0f, -1.0f, 0.0f, -1.0f, 5.f, -1.0f, 0.0f, -1.0f, 0.0f };

		if (imagem.getType() != BufferedImage.TYPE_BYTE_GRAY)
			throw new ExceptionTipoImagemInvalido("Imagem deve estar em escala de cinza");

		BufferedImage imagemRealcada;
		imagemRealcada = convolucao(imagem, elementos);

		return imagemRealcada;
	}
}
