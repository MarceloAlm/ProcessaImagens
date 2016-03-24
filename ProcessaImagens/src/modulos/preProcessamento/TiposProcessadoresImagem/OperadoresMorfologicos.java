package modulos.preProcessamento.TiposProcessadoresImagem;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import modulos.ExceptionTipoImagemInvalido;

public abstract class OperadoresMorfologicos extends ProcessadorImagem {
	protected final boolean[][] Mascara;
	protected final int[] pixel = new int[3];
	protected int CentroHorizontal = 0;
	protected int CentroVertical = 0;

	/** Construtor */
	public OperadoresMorfologicos(BufferedImage imagem, final boolean[][] Mascara) throws ExceptionTipoImagemInvalido {
		if (imagem.getType() != BufferedImage.TYPE_BYTE_BINARY) throw new ExceptionTipoImagemInvalido("Imagem deve ser binária");

		this.imagemOriginal = imagem;
		this.Mascara = Mascara;
		CentroHorizontal = Mascara.length / 2 + 1;
		CentroVertical = Mascara[0].length / 2 + 1;
	}

	/**
	 * Retorna uma mascara preenchida com valores verdadeiro (true)
	 * 
	 * @param width Largura da mascara
	 * @param height Altura da mascara
	 * @return Matriz representando a mascara
	 */
	public final static boolean[][] retornaMascara(final int width, final int height) {
		final boolean[][] newMask = new boolean[width][height];
		for (int i = 0; i < newMask.length; i++) {
			for (int j = 0; j < newMask[0].length; j++) {
				{
					newMask[i][j] = true;
				}
			}
		}

		return newMask;
	}

	/**
	 * Processes a postion in the image.
	 * 
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 * @param raster The output raster
	 * @param in The input raster
	 */
	protected abstract void ProcessarPonto(int x, int y, WritableRaster raster, Raster in);

	@Override
	public BufferedImage Processar() {
		imagemProcessada = new BufferedImage(imagemOriginal.getWidth(), imagemOriginal.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

		final Raster in = imagemOriginal.getRaster();
		final WritableRaster raster = imagemProcessada.getRaster();

		final int width = imagemOriginal.getWidth();
		final int height = imagemOriginal.getHeight();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if ((i < CentroHorizontal || i > (width - CentroHorizontal - 1)) || (j < CentroVertical || j > (height - CentroVertical - 1))) {
					raster.setSample(i, j, iCINZA, 255);
				} else {
					ProcessarPonto(i, j, raster, in);
				}
			}
		}

		return imagemProcessada;
	}

	@Override
	public BufferedImage Processar(BufferedImage NovaImagem) throws ExceptionTipoImagemInvalido {
		if (NovaImagem.getType() != BufferedImage.TYPE_BYTE_BINARY) throw new ExceptionTipoImagemInvalido("Imagem deve ser binária");
		this.imagemOriginal = NovaImagem;

		return this.Processar();
	}

}
